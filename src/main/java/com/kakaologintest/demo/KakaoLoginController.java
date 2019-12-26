package com.kakaologintest.demo;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class KakaoLoginController {

    private JsonNode access_token  = null;
    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/messagelink")
    public String messagelink() {
        return "MessageLink";
    }

    @RequestMapping(value = "/oauth", produces = "application/json")
    public String kakaoLogin(@RequestParam("code") String code, Model model, HttpSession session) {
        System.out.println("로그인 할 때 임시 코드값");
        System.out.println(code);
        System.out.println("로그인 후 결과값");

        kakao_restapi kr = new kakao_restapi();
        JsonNode node = kr.getAccessToken(code);

        System.out.println(node);
        access_token = node.get("access_token");

        session.setAttribute("token", access_token.asText());

        JsonNode userInfo = kr.getUserInfo(access_token);

        String id = userInfo.get("id").toString();
        String nickname = null;
        String thumbnailImage = null;
        String profileImage = null;
        String email = null;

        JsonNode properties = userInfo.path("properties");
        if (properties.isMissingNode()){

        } else {
            nickname = properties.get("nickname").asText();
            thumbnailImage = properties.get("thumbnail_image").asText();
            profileImage = properties.get("profile_image").asText();

            System.out.println("nickname : " + nickname);


            System.out.println("thumbnailImage : " + thumbnailImage);

            System.out.println("profileImage : " + profileImage);
        }
        JsonNode kakao_account = userInfo.path("kakao_account");

        if (!kakao_account.get("email_needs_agreement").asBoolean())
        {
            email = kakao_account.get("email").asText();
        }
        else
        {
            email = "사용자 동의 필요!!";
        }

        session.setAttribute("id", id);
        session.setAttribute("nickname", nickname);
        session.setAttribute("thumbnailImage",thumbnailImage);
        session.setAttribute("profileImage",profileImage);
        session.setAttribute("email",email);

        JsonNode result = kr.sendTextMsgToMe(access_token);

        return "logininfo";
    }


}
