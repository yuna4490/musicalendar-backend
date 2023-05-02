package Musicalendar.musicalendarproject.auth.handler;

import Musicalendar.musicalendarproject.auth.CookieAuthorizationRequestRepository;
import Musicalendar.musicalendarproject.auth.CustomUserDetails;
import Musicalendar.musicalendarproject.auth.jwt.TokenProvider;
import Musicalendar.musicalendarproject.domain.RefreshToken;
import Musicalendar.musicalendarproject.dto.AccessTokenDto;
import Musicalendar.musicalendarproject.dto.TokenDto;
import Musicalendar.musicalendarproject.repository.RefreshTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

//    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
//    private String redirectUrl;
//    @Value("${auth.token.refresh-cookie-key}")
    private final String COOKIE_REFRESH_TOKEN_KEY="CookieThatIMade";
    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper=new ObjectMapper();
//    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("카카오 로그인 성공");

        try{
            System.out.println(authentication.getPrincipal());
//            CustomUserDetails oAuth2User=(CustomUserDetails) authentication.getPrincipal();

            // 3. 인증 정보를 기반으로 JWT 토큰 생성 (Access Token + Refresh Token)
            TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

            // 리프레시 토큰 DB 저장
            RefreshToken refreshToken= RefreshToken.builder()
                    .key(Long.parseLong(authentication.getName()))
                    .value(tokenDto.getRefreshToken())
                    .expirationTime(tokenDto.getRefreshTokenExpirationTime())
                    .build();
            refreshTokenRepository.save(refreshToken);

            ResponseCookie cookie=ResponseCookie.from(COOKIE_REFRESH_TOKEN_KEY, tokenDto.getRefreshToken())
                    .httpOnly(true)
                    .secure(true)   // https 옵션 설정
                    .sameSite("Lax")
                    .maxAge(tokenDto.getRefreshTokenExpirationTime()/1000)
                    .path("/")  // 모든 곳에서 쿠키 열람 가능
                    .build();
            response.addHeader("Set-Cookie", cookie.toString());

            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            String tokenResponse=objectMapper.writeValueAsString(tokenDto.getAccessTokenDto());
            response.getOutputStream().println(tokenResponse);
            System.out.println(response.getHeader("Set-Cookie").toString());
        }catch (Exception e){
            throw e;
        }
    }
}
