package Musicalendar.musicalendarproject.auth.service;

import Musicalendar.musicalendarproject.auth.CustomUserDetails;
import Musicalendar.musicalendarproject.auth.user_info.KakaoUserInfo;
import Musicalendar.musicalendarproject.auth.user_info.OAuth2UserInfo;
import Musicalendar.musicalendarproject.domain.Authority;
import Musicalendar.musicalendarproject.domain.Member;
import Musicalendar.musicalendarproject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        System.out.println("카카오? 구글? = " + provider);
        OAuth2UserInfo oAuth2UserInfo=null;

        switch (provider){
            case "google":
//                oAuth2UserInfo=new GoogleUserInfo(oAuth2User.getAttributes());
                break;
            case "kakao":
                oAuth2UserInfo=new KakaoUserInfo((Map)oAuth2User.getAttributes().get("kakao_account"), String.valueOf(oAuth2User.getAttributes().get("id")));
////                oAuth2UserInfo=new KakaoUserInfo((Map)oAuth2User.getAttributes().get("kakao_account"), String.valueOf(oAuth2User.getAttributes().get("id")));
                System.out.println("oAuth2User.getAttributes() "+oAuth2User.getAttributes());
                System.out.println("oAuth2User.getAttributes().get(\"kakao_account\") "+oAuth2User.getAttributes().get("kakao_account"));
                System.out.println("이메일 "+oAuth2UserInfo.getEmail());
                System.out.println("닉네임 "+oAuth2UserInfo.getNickname());
                break;
            default:
                System.out.println("지원하지 않는 로그인 서비스임");
        }

        Member member=saveOrUpdate(oAuth2UserInfo);

        return new CustomUserDetails(member, oAuth2User.getAttributes());
    }

    private Member saveOrUpdate(OAuth2UserInfo oAuth2UserInfo){
        // 이미 저장된 회원이라면 정보 업데이트, 아니면 회원가입
        Member member=memberRepository.findByEmail(oAuth2UserInfo.getEmail());
        if (member==null){
            member=Member.builder()
                    .nickname(oAuth2UserInfo.getNickname())
                    .email(oAuth2UserInfo.getEmail())
                    .provider(oAuth2UserInfo.getProvider())
                    .provideId(oAuth2UserInfo.getProvideId())
                    .authority(Authority.ROLE_USER)
                    .build();
//            member=new Member(oAuth2UserInfo.getNickname(), oAuth2UserInfo.getEmail(), oAuth2UserInfo.getProvider(), oAuth2UserInfo.getProvideId());
        } else{
            member.update(oAuth2UserInfo.getNickname());
        }

        return memberRepository.save(member);
    }
}
