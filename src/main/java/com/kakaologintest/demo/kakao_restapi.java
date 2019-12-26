package com.kakaologintest.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;

public class kakao_restapi {
    private String client_id = "1d898da97758eca206989c9aa2654296";

    public JsonNode getAccessToken(String autorize_code)
    {
        final String RequestUrl = "https://kauth.kakao.com/oauth/token";
        final Collection<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("grant_type","authorization_code"));
        postParams.add(new BasicNameValuePair("client_id", client_id));
        postParams.add(new BasicNameValuePair("redirect_uri","http://localhost:8080/oauth"));
        postParams.add(new BasicNameValuePair("code", autorize_code));

        final HttpClient client = HttpClientBuilder.create().build();
        final HttpPost post = new HttpPost(RequestUrl);

        JsonNode returnNode = null;

        try {
            post.setEntity(new UrlEncodedFormEntity(postParams));
            final HttpResponse response = client.execute(post);
            ObjectMapper mapper = new ObjectMapper();
            returnNode = mapper.readTree(response.getEntity().getContent());

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally {

        }
        return returnNode;
    }
    //https://kauth.kakao.com/oauth/authorize?client_id=1d898da97758eca206989c9aa2654296&redirect_uri=http://localhost:8080/oauth&response_type=code


    public JsonNode getUserInfo(JsonNode access_token)
    {
        final String RequestUrl = "https://kapi.kakao.com/v2/user/me";

        final HttpClient client = HttpClientBuilder.create().build();
        final HttpPost post = new HttpPost(RequestUrl);

        JsonNode returnNode = null;
        post.addHeader("Authorization", "Bearer " + access_token);

        try {
            final HttpResponse response = client.execute(post);
            ObjectMapper mapper = new ObjectMapper();
            returnNode = mapper.readTree(response.getEntity().getContent());

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally {

        }
        return returnNode;
    }
    //https://kapi.kakao.com/v2/user/me


}
