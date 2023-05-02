package Musicalendar.musicalendarproject.auth.user_info;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo{

    private String id;
//    private String nickname;
//    private String e
    private Map<String, Object> kakaoAccount;

    public KakaoUserInfo(Map<String, Object> attributes, String id){
        this.kakaoAccount=attributes;
        this.id=id;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProvideId() {
        return "kakao_"+id;
    }

    @Override
    public String getEmail() {
        return (String) kakaoAccount.get("email");
    }

    @Override
    public String getNickname() {
        Map<String, Object> nickname = (Map) kakaoAccount.get("profile");
        return (String) nickname.get("nickname");
    }
}
