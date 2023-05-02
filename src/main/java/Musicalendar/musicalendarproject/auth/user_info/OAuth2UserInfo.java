package Musicalendar.musicalendarproject.auth.user_info;

public interface OAuth2UserInfo {

    String getProvider();
    String getProvideId();
    String getEmail();
    String getNickname();
}
