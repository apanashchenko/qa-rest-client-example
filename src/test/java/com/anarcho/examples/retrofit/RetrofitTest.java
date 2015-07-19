package com.anarcho.examples.retrofit;

import com.anarcho.examples.retrofit.services.*;
import com.anarcho.models.App;
import com.anarcho.models.Build;
import com.anarcho.models.Team;
import com.anarcho.models.User;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Created by alpa on 7/18/15.
 */
public class RetrofitTest {

    private static final String TEST_APP_NAME = "rate";
    private UserService auth = BaseService.createService(UserService.class);
    private UserService userService;
    private AppService appService;
    private BuildService buildService;
    private TeamService teamService;

    @BeforeClass
    public void setUp() throws Exception {
        System.out.println("Start RetrofitTest");
        User user = new User();
        user.setEmail("testUser@mail.com");
        user.setPassword("testUser");

        String authToken = auth.login(user).getAuthToken();

        assertThat(authToken,is(notNullValue()));
        assertThat(authToken, not(isEmptyOrNullString()));

        userService = BaseService.createService(UserService.class, authToken);
        appService = BaseService.createService(AppService.class, authToken);
        buildService = BaseService.createService(BuildService.class, authToken);
        teamService = BaseService.createService(TeamService.class, authToken);
    }

    @AfterClass
    public void tearDown() {
        System.out.println("Finish RetrofitTest");
    }


    @Test
    public void userShouldNotBeNull() throws Exception {
        User user = userService.getUser();

        assertThat(user.getId(), not(isEmptyOrNullString()));

        assertThat(user.getName(), not(isEmptyOrNullString()));
    }

    @Test
    public void appListShouldNotBeEmpty() throws Exception {
        List<App> appList = appService.getAppList().getList();
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
        List<Team> teamList = teamService.getTeamList(rateApp.getAppKey()).getList();
        for (Team team : teamList) {
            assertThat(team.getEmail(), not(isEmptyOrNullString()));
            assertThat(team.getPermission(), not(isEmptyOrNullString()));
        }
    }


    private App getAppByName(String appName) throws Exception {
        List<App> appList = appService.getAppList().getList();
        for (App app : appList) {
            if (appName.equalsIgnoreCase(app.getName())) {
                return app;
            }
        }
        throw new Exception(appName + " not found!");
    }

    private List<Build> getBuildList(String appKey) throws Exception {
        return buildService.getBuildList(appKey).getList();
    }

    private Build getBuildInfo(String appKey, int buildId) throws Exception {
        return buildService.getBuild(appKey, buildId);
    }

}
