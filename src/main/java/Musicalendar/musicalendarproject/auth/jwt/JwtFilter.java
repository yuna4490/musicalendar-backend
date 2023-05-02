package Musicalendar.musicalendarproject.auth.jwt;

import Musicalendar.musicalendarproject.auth.service.AuthService;
import Musicalendar.musicalendarproject.auth.util.CookieUtil;
import Musicalendar.musicalendarproject.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static Musicalendar.musicalendarproject.exception.ErrorCode.INVALID_REFRESH_TOKEN;
import static Musicalendar.musicalendarproject.exception.ErrorCode.REFRESH_TOKEN_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    @Value("${auth.token.refresh-cookie-key}")
    private String cookieKey;

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private final TokenProvider tokenProvider;
    private final AuthService authService;

    // 실제 필터링 로직은 doFilterInternal 에 들어감
    // JWT 토큰의 인증 정보를 현재 쓰레드의 SecurityContext 에 저장하는 역할 수행
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        // 가입/로그인/재발급을 제외한 모든 Request 요청은 doFilterInternal 거침
        // 요청이 Controller 에 도착하면 SecurityContext 에 Member ID 존재함이 보장
        // 탈퇴로 인해 Member ID 가 DB 에 없는 경우 등 예외 상황은 Service 단에서 고려해야 함

        // 1. Request Header 에서 토큰을 꺼냄
        String jwt = resolveToken(request); // 액세스 토큰

        // 2. validateToken 으로 토큰 유효성 검사 (TokenProvider)
        // 정상 토큰이면 해당 토큰으로 Authentication 을 가져와서 SecurityContext 에 저장
        if (StringUtils.hasText(jwt)) {
            if (tokenProvider.validateToken(jwt)){
                Authentication authentication = tokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {    // 액세스 토큰 만료
                String refreshToken= CookieUtil.getCookie(request, cookieKey)
                                .map(Cookie::getValue).orElseThrow(()->new CustomException(REFRESH_TOKEN_NOT_FOUND));
                authService.reissue(jwt, refreshToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    // Request Header 에서 토큰 정보를 꺼내오기
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
