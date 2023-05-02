package Musicalendar.musicalendarproject.controller;

//import Musicalendar.musicalendarproject.service.AuthService;
import Musicalendar.musicalendarproject.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

//    private final AuthService authService;
//
//    @GetMapping("/login/kakao")
//    public ResponseEntity<TokenDto> login(@RequestParam String code){
//        return ResponseEntity.ok(authService.kakaoLogin(code));
//    }

//    @PostMapping("/refresh")
//    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
////        token.replace("Bearer ", "");
//
//        System.out.println("되니..?");
//        System.out.println(tokenRequestDto.getAccessToken());
//        System.out.println(tokenRequestDto.getRefreshToken());
//
////        TokenRequestDto tokenRequestDto=TokenRequestDto.builder()
////                .accessToken(accessToken)
////                .refreshToken(token)
////                .build();
//        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
//    }

    @ResponseBody
    @GetMapping("/test/oauth/login")
    public String testLogin(
            Authentication authentication,
            @AuthenticationPrincipal OAuth2User oauth) { //세션 정보 받아오기 (DI 의존성 주입)

//방법 1
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authentication: " + oAuth2User.getAttributes());

        //방법 2
        System.out.println("OAuth2User:" + oauth.getAttributes());

        return "OAuth 세션 정보 확인";
    }

//    @PostMapping("/member/logout")
//    public ResponseEntity<?> logout(@RequestBody TokenDto tokenDto){
//        return ResponseEntity.ok(authService.kakaoLogout(tokenDto));
//    }

//    @PostMapping("/member/refreshToken")
//    public ResponseEntity<TokenDto> reissueKakaoToken(TokenDto tokenDto){
//        return ResponseEntity.ok(authService.reissue(tokenDto));
//    }
}
