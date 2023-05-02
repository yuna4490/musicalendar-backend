//package Musicalendar.musicalendarproject.service;
//
//import Musicalendar.musicalendarproject.domain.Member;
//import Musicalendar.musicalendarproject.dto.SignUpMemberDto;
//import Musicalendar.musicalendarproject.dto.TokenDto;
//import Musicalendar.musicalendarproject.exception.CustomException;
//import Musicalendar.musicalendarproject.repository.MemberRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
//import org.springframework.stereotype.Service;
//
//import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.ProtocolException;
//import java.net.URL;
//import java.util.HashMap;
//import java.util.Map;
//
//import static Musicalendar.musicalendarproject.exception.ErrorCode.DUPLICATE_MEMBER;
//
//@Service
//@RequiredArgsConstructor
//public class AuthService {
//
//    private final KakaoService kakaoService;
//    private final MemberRepository memberRepository;
//
//    public TokenDto kakaoLogin(String code){
//
//        TokenDto tokenDto=kakaoService.getKakaoAccessToken(code);
//
//        Map<String, Object> memberInfo=kakaoService.getMemberInfo(tokenDto.getAccessToken());
//
//        System.out.println("회원 정보 출력: "+memberInfo);
//
////        String username=(String) memberInfo.get("id");
//        String nickname=(String) memberInfo.get("nickname");
//        String email=(String) memberInfo.get("email");
////        String provider=(String) memberInfo.get""
//
//        kakaoSignup(nickname, email, "kakao", "asdf");
//
//        // 액세스 토큰 + 리프레시 토큰
//        return tokenDto;
//    }
//
//    public void kakaoSignup(String nickname, String email, String provider, String provideId){
//
//        // DB 에 회원정보 없으면 회원가입
//        if (memberRepository.existsByEmail(email)){
//            System.out.println("이미 가입된 이메일");
//            throw new CustomException(DUPLICATE_MEMBER);
//        }
//        System.out.println("회원가입");
//        Member member=new Member(nickname, email, provider, provideId);
//        memberRepository.save(member);
//    }
//
//    public String kakaoLogout(TokenDto tokenDto){
//        kakaoService.kakaoLogout(tokenDto.getAccessToken());
//        return "로그아웃 되었습니다.";
//    }
//}
