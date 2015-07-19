package com.anarcho.examples.apache.httpcomponents;

import com.anarcho.models.*;
import com.google.gson.Gson;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Created by alpa on 7/18/15.
 */
public class ApacheHttpTest extends BaseMethods {

    private static final String TEST_APP_NAME = "rate";

    @BeforeClass
    public void setUp() throws Exception {
        System.out.println("Start ApacheHttpTest");
        User user = new User();
        user.setEmail("testUser@mail.com");
        user.setPassword("testUser");

        AuthToken auth = new Gson().fromJson(executePost(
                "/login", new Gson().toJson(user), 200), AuthToken.class);

        String authToken = auth.getAuthToken();

        assertThat(authToken,is(notNullValue()));
        assertThat(authToken, not(isEmptyOrNullString()));
    }

    @AfterClass
    public void tearDown() {
        System.out.println("Finish ApacheHttpTest");
    }

    @Test
    public void userShouldNotBeNull() throws Exception {
        User user = new Gson().fromJson(executeGet("/user", 200), User.class);

        assertThat(user.getId(), not(isEmptyOrNullString()));

        assertThat(user.getName(), not(isEmptyOrNullString()));
    }

    @Test
    public void appListShouldNotBeEmpty() throws Exception {
        List<App> appList = new Gson().fromJson(executeGet("/apps", 200), AppList.class).getList();
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
        List<Team> teamList = new Gson().fromJson(executeGet("/permission/" + rateApp.getAppKey(), 200), TeamList.class).getList();
        for (Team team : teamList) {
            assertThat(team.getEmail(), not(isEmptyOrNullString()));
            assertThat(team.getPermission(), not(isEmptyOrNullString()));
        }
    }


    private App getAppByName(String appName) throws Exception {
        List<App> appList = new Gson().fromJson(executeGet("/apps", 200), AppList.class).getList();
        for (App app : appList) {
            if (appName.equalsIgnoreCase(app.getName())) {
                return app;
            }
        }
        throw new Exception(appName + " not found!");
    }

    private List<Build> getBuildList(String appKey) throws Exception {
        return new Gson().fromJson(
                executeGet(String.format("/apps/%s/builds", appKey), 200), BuildList.class).getList();
    }

    private Build getBuildInfo(String appKey, int buildId) throws Exception {
        return new Gson().fromJson(
                executeGet(String.format("/apps/%s/%d", appKey, buildId), 200), Build.class);
    }

}
