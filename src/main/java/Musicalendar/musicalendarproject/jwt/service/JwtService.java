package Musicalendar.musicalendarproject.jwt.service;

import Musicalendar.musicalendarproject.repository.UserRepository;
import com.auth0.jwt.JWT;
import Musicalendar.musicalendarproject.repository.MemberRepository;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtService {

    //application-jwt.yml의 프로퍼티를 주입
    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    /*
    * JWT의 subject와 claim으로 email 사용 -> claim의 name을 'email'으로 설정
    * */
    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String EMAIL_CLAIM = "email";
    private static final String BEARER = "Bearer";

    //private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    /*
    * AccessToken 생성 메서드
    * */
    public String createAccessToken(String email) {
        Date now = new Date();
        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT) //JWT subject : AccessToken
                .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod)) // 토큰 만료 시간
                .withClaim(EMAIL_CLAIM, email) // email만 클레임으로 사용
                .sign(Algorithm.HMAC512(secretKey));
    }

    /*
    * RefreshToken 생성
    * RefreshToken은 Claim에 email도 넣지 않으므로 withClaim() X
    * */
    public String createRefreshToken() {
        Date now = new Date();
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
                .sign(Algorithm.HMAC512(secretKey));
    }

    /*
    * AccessToken 헤더에 실어서 보내기
    * */
    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        response.setHeader(accessHeader, accessToken);
        log.info("재발급된 Access Token : {}", accessToken);
    }

    /*
    * AccessToken + RefreshToken 헤더에 실어서 보내기
    * */
    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        setAccessTokenHeader(response, accessToken);
        setRefreshTokenHeader(response, refreshToken);
        log.info("Access Token, Refresh Token 헤더 설정 완료");
    }

    /*
    * 헤더에서 RefreshToken 추출
    * 토큰 형식 : Bearer XXX에서 Bearer를 제외하고 순수 토큰만 가져오기 위해, 헤더 가져온 후 Bearer삭제
    * */
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));

    }

    /*
    * 헤더에서 AccessToken 추출
    * */
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    /*
    * AccessToken에서 email 추출
    * 추출 전에 JWT.require()로 검증기 생성
    * verify로 AccessToken 검증 후 유효하다면 getClaim()으로 email 추출
    * */
    public Optional<String> extractEmail(String accessToken) {
        try {
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build() //반환된 빌더로 JWT verifier 생성
                    .verify(accessToken)
                    .getClaim(EMAIL_CLAIM)
                    .asString()); //토큰이 유효한지 확인한 후에 클레임(이메일)을 꺼내 String으로 변환하여 반환
        } catch (Exception e) {
            log.error("엑세스 토큰이 유효하지 않습니다."); // 토큰이 유효하지 않다면 빈 값을 반환
            return Optional.empty();
        }
    }

    /*
    * AccessToken 헤더 설정
    * */
    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, accessToken);
    }

    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(refreshHeader, refreshToken);
    }

    /*
    * RefreshToken DB 저장(업데이트)
    * 회원가입시 RefreshToken은 null
    * 로그인 하면서 RefreshToken 발급 -> DB에 저장하는 메서드
    * */
    public void updateRefreshToken(String email, String refreshToken) {
        userRepository.findByEmail(email)
                .ifPresentOrElse(
                        user -> user.updateRefreshToken(refreshToken),
                        () -> new Exception("일치하는 회원이 없습니다.")
                );
    }

    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            return true;
        } catch (Exception e) {
            log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
            return false;
        }
    }


}
