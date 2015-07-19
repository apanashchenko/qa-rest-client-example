package com.anarcho.examples.rest.assured;

import com.anarcho.models.*;
import com.google.gson.Gson;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Created by alpa on 7/18/15.
 */
public class RestAssuredTest {

    private static final String BASE_URL = "http://anarcho.pp.ciklum.com/api";
    private static final String X_AUTH_TOKEN = "x-auth-token";
    private static final String TEST_APP_NAME = "rate";
    private static final String LOGIN = "/login";
    private static final String USERS = "/user";
    private static final String APPS = "/apps";
    private final String PERMISSION = "/permission";
    private String email = "testUser@mail.com";
    private String password = "testUser";
    private String authToken;

    @BeforeClass
    public void setUp() throws Exception {
        System.out.println("Start RestAssuredTest");
        RestAssured.baseURI = BASE_URL;

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        Response response =
                given().log().method().log().path()
                        .request()
                        .contentType(ContentType.JSON)
                        .body(new Gson().toJson(user))
                        .when()
                        .post(LOGIN);
        response.then()
                .statusCode(200)
                .body("authToken", not(isEmptyOrNullString()));

        AuthToken auth = new Gson().fromJson(response.getBody().asString(), AuthToken.class);

        authToken = auth.getAuthToken();

        assertThat(authToken, is(notNullValue()));
        assertThat(authToken, not(isEmptyOrNullString()));
    }

    @AfterClass
    public void tearDown() {
        System.out.println("Finish RestAssuredTest");
    }

    @Test
    public void userShouldNotBeNull() throws Exception {
        Response response =
                given().log().method().log().path()
                        .header(X_AUTH_TOKEN, authToken)
                        .when()
                        .get(USERS);
        response
                .then().statusCode(200);

        User user = new Gson().fromJson(response.getBody().asString(), User.class);

        assertThat(user.getId(), not(isEmptyOrNullString()));

        assertThat(user.getName(), not(isEmptyOrNullString()));
    }

    @Test
    public void appListShouldNotBeEmpty() throws Exception {
        Response response =
                given().log().method().log().path()
                        .header(X_AUTH_TOKEN, authToken)
                        .when()
                        .get(APPS);
        response
                .then().statusCode(200);

        List<App> appList = new Gson().fromJson(response.getBody().asString(), AppList.class).getList();
        assertThat(appList.size(), is(not(0)));
    }

    @Test
    public void appShouldNotBeNull() throws Exception {
        App rateApp = getAppByName(TEST_APP_NAME);

        assertThat(rateApp.getAppKey(), not(isEmptyOrNullString()));

        assertThat(rateApp.getAppType(), is("andr"));

        assertThat(rateApp.getCreatedOn(), notNullValue());

        assertThat(rateApp.getIconUrl(), not(isEmptyOrNullString()));

        assertThat(rateApp.getPermission(), is("r"));
    }

    @Test
    public void appBuildListShouldNotBeEmpty() throws Exception {
        App rateApp = getAppByName(TEST_APP_NAME);
        List<Build> buildList = getBuildList(rateApp.getAppKey());

        assertThat(buildList.size(), is(not(0)));
    }

    @Test
    public void buildInfoShouldNotBeNull() throws Exception {
        App rateApp = getAppByName(TEST_APP_NAME);
        List<Build> buildList = getBuildList(rateApp.getAppKey());
        for (Build build : buildList) {
            assertThat(build.getCreatedOn(), notNullValue());

            assertThat(build.getId(), notNullValue());

            assertThat(build.getReleaseNotes(), not(isEmptyOrNullString()));

            assertThat(build.getVersionCode(), not(isEmptyOrNullString()));

            assertThat(build.getVersionName(), not(isEmptyOrNullString()));
        }
    }

    @Test
    public void buildDetailInfoShouldNotBeNull() throws Exception {
        App rateApp = getAppByName(TEST_APP_NAME);
        List<Build> buildList = getBuildList(rateApp.getAppKey());
        for (Build build : buildList) {
            Build currentBuild = getBuildInfo(rateApp.getAppKey(), build.getId());

            assertThat(currentBuild.getBuildUrl(), not(isEmptyOrNullString()));

            assertThat(currentBuild.getCreatedOn(), notNullValue());

            assertThat(currentBuild.getId(), notNullValue());

            assertThat(currentBuild.getReleaseNotes(), not(isEmptyOrNullString()));

            assertThat(currentBuild.getVersionCode(), not(isEmptyOrNullString()));

            assertThat(currentBuild.getVersionName(), not(isEmptyOrNullString()));
        }
    }

    @Test
    public void teamPermissionShouldHaveUser() throws Exception {
        App rateApp = getAppByName(TEST_APP_NAME);
        Response response =
                given().log().method().log().path()
                        .header(X_AUTH_TOKEN, authToken)
                        .when()
                        .get(PERMISSION + "/" + rateApp.getAppKey());
        response
                .then().statusCode(200);

        List<Team> teamList = new Gson().fromJson(response.getBody().asString(), TeamList.class).getList();
        for (Team team : teamList) {
            assertThat(team.getEmail(), not(isEmptyOrNullString()));
            assertThat(team.getPermission(), not(isEmptyOrNullString()));
        }
    }


    private App getAppByName(String appName) throws Exception {
        Response response =
                given().log().method().log().path()
                        .header(X_AUTH_TOKEN, authToken)
                        .when()
                        .get(APPS);
        response
                .then().statusCode(200);

        List<App> appList = new Gson().fromJson(response.getBody().asString(), AppList.class).getList();
        for (App app : appList) {
            if (appName.equalsIgnoreCase(app.getName())) {
                return app;
            }
        }
        throw new Exception(appName + " not found!");
    }

    private List<Build> getBuildList(String appKey) throws Exception {
        Response response =
                given().log().method().log().path()
                        .header(X_AUTH_TOKEN, authToken)
                        .when()
                        .get(String.format("/apps/%s/builds", appKey));
        response
                .then().statusCode(200);

        return new Gson().fromJson(response.getBody().asString(), BuildList.class).getList();
    }

    private Build getBuildInfo(String appKey, int buildId) throws Exception {
        Response response =
                given().log().method().log().path()
                        .header(X_AUTH_TOKEN, authToken)
                        .when()
                        .get(String.format("/apps/%s/%d", appKey, buildId));
        response
                .then().statusCode(200);

        return new Gson().fromJson(response.getBody().asString(), Build.class);
    }

}
