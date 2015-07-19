package com.anarcho.examples.apache.httpcomponents;

import com.anarcho.models.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;


/**
 * Created by alpa on 7/18/15.
 */
public class BaseMethods {

    private String host = "anarcho.pp.ciklum.com";
    private String schema = "http";
    private String userName = "testUser@mail.com";
    private String password = "testUser";


    protected String executeGet(String path, int statusCode) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String url = createURI(path);
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("x-auth-token", getToken());
        CloseableHttpResponse response = httpclient.execute(httpGet);
        int code = response.getStatusLine().getStatusCode();
        String res = null;
        if (response.getEntity() != null) {
            res = EntityUtils.toString(response.getEntity());
        }
        if (statusCode != code) {
            throw new AssertionError(String.format("\nGET: %s\nCODE: %s\nRESPONSE: %s", url, code, res));
        }
        return res;
    }

    protected String executePost(String path, Object body, int statusCode) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String url = createURI(path);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("x-auth-token", getToken());
        HttpEntity entity = new StringEntity(body.toString(), ContentType.APPLICATION_JSON);
        httpPost.setEntity(entity);
        CloseableHttpResponse response = httpclient.execute(httpPost);
        int code = response.getStatusLine().getStatusCode();
        String res = null;
        if (response.getEntity() != null) {
            res = EntityUtils.toString(response.getEntity());
        }
        if (statusCode != code) {
            throw new AssertionError(String.format("\nPOST: %s\nCODE: %s\nRESPONSE: %s", url, code, res));
        }
        return res;
    }

    private String createURI(String path) {
        return String.format("%s://%s:/api%s", schema, host, path);
    }

    private String getToken() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(createURI("/login"));

        User user = new User();
        user.setEmail(userName);
        user.setPassword(password);

        Gson gson = new Gson();
        HttpEntity entity = new StringEntity(gson.toJson(user), ContentType.APPLICATION_JSON);
        httpPost.setEntity(entity);
        CloseableHttpResponse response = httpclient.execute(httpPost);
        int code = response.getStatusLine().getStatusCode();
        String res = null;
        if (response.getEntity() != null) {
            res = new Gson().fromJson(EntityUtils.toString(
                    response.getEntity()), JsonObject.class).get("authToken").getAsString();
        }
        if (code != 200) {
            throw new AssertionError(String.format("CODE: %s\nRESPONSE: %s", code, res));
        }
        return res;

    }
}
