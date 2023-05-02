package Musicalendar.musicalendarproject.auth.handler;

import Musicalendar.musicalendarproject.auth.CookieAuthorizationRequestRepository;
import Musicalendar.musicalendarproject.auth.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//import static Musicalendar.musicalendarproject.auth.CookieAuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {

    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//        String targetUrl= CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
//                        .map(Cookie::getValue)
//                                .orElse("/");
//
//        targetUrl= UriComponentsBuilder.fromUriString(targetUrl)
//                        .queryParam("error", exception.getLocalizedMessage())
//                                .build().toUriString();
        cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
//        getRedirectStrategy().sendRedirect(request, response, targetUrl);

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write("소셜 로그인 실패..");
        log.info("소셜 로그인 실패, 에러 메시지 출력: {}", exception.getMessage());
    }
}
