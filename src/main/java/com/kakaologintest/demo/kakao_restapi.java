package com.kakaologintest.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import net.minidev.json.JSONObject;
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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;

public class kakao_restapi {
    private String client_id = "1d898da97758eca206989c9aa2654296";

    //액세스 토큰을 받아오기 위한 REST API
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

    //받아온 access_token을 이용해서 사용자 정보를 받아오는 부분
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

    //access_token을 이용해서 자신에게 메시지를 보내는 부분
    public JsonNode sendTextMsgToMe(JsonNode access_token)
    {
        final String RequestUrl = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

        final HttpClient client = HttpClientBuilder.create().build();
        final HttpPost post = new HttpPost(RequestUrl);

        JsonNode returnNode = null;
        post.addHeader("Authorization", "Bearer " + access_token);
        final Collection<NameValuePair> postParams = new ArrayList<NameValuePair>();
        JSONObject template_object = new JSONObject();
        template_object.put("object_type","text");
        template_object.put("text", "한글 메시지 테스트 Test");
        JSONObject link = new JSONObject();
        link.put("web_url", "http://localhost:8080/messagelink");
        link.put("mobile_web_url","http://localhost:8080/messagelink");
        template_object.put("link",link);
        template_object.put("button_title", "버튼 Press");
        postParams.add(new BasicNameValuePair("template_object",template_object.toString()));

        try {
            post.setEntity(new UrlEncodedFormEntity(postParams, Charset.forName("utf-8")));
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
    /*

    curl -v -X POST "https://kapi.kakao.com/v2/api/talk/memo/default/send" \
    -H "Authorization: Bearer xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" \
    -d 'template_object={
        "object_type": "text",
        "text": "텍스트 영역입니다. 최대 200자 표시 가능합니다.",
        "link": {
            "web_url": "https://developers.kakao.com",
            "mobile_web_url": "https://developers.kakao.com"
        },
        "button_title": "바로 확인"
    }'
     */

}
