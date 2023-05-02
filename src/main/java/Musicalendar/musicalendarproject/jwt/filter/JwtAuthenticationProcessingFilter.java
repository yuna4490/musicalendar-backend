package Musicalendar.musicalendarproject.jwt.filter;

import Musicalendar.musicalendarproject.domain.Member;
import Musicalendar.musicalendarproject.jwt.service.JwtService;
import Musicalendar.musicalendarproject.repository.MemberRepository;
import Musicalendar.musicalendarproject.jwt.util.PasswordUtil;
import Musicalendar.musicalendarproject.domain.User;
import Musicalendar.musicalendarproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private static final String NO_CHECK_URL = "/login"; //'/login'으로 들어오는 요청에는 filter 작동 안함

    private final JwtService jwtService;
    //private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getRequestURI().equals(NO_CHECK_URL)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 사용자 요청 헤더에서 RefreshToken 추출 -> 없거나 유효하지 않다면 null 반환
        // 사용자 요청 헤더에 RefreshToken이 있는 경우는 AccessToken이 만료되어 요청한 경우만 존재
        String refreshToken = jwtService.extractRefreshToken(request)
                .filter(jwtService::isTokenValid) // 유효한 리프레시 토큰을 반환
                .orElse(null);

        // 리프레시 토큰이 요청 헤더에 있다면, 엑세스 토큰이 만료된 것이므로,
        // 리프레시토큰이 디비의 것과 일치하는지 확인한 후 엑세스 토큰을 재발급!!
        // 이 경우 엑세스 토큰을 재발급하고 인증 처리는 하지 않기 위해 바로 반환해 필터 진행 막음
        if (refreshToken != null) {
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
            return;
        }

        // 리프레시토큰이 없거나 유효하지 않다면, 엑세스 토큰을 검사하고 인증을 처리
        // 엑세스토큰 없거나 유효하지 않으면 403, 유효하면 인증성공
        // 엑세스 토큰이 유효하다면, 인증 객체가 담긴 상태로 다음 필터로 넘어가기 때문에 인증 성공
        if (refreshToken == null) {
            checkAccessTokenAndAuthentication(request, response, filterChain);
        }
    }

    /*
    * [리프레시 토큰으로 유저정보 찾기 & 엑세스 토큰, 리프레시 토큰 재발급 메서드>]
    * 파라미터로 들어온 헤더에서 추출한 리프레시 토큰으로 DB에서 유저를 찾고,
    * 해당 유저가 있다면 JwtService.CreateAccessToken()으로 엑세스 토큰 생성
    * reIssueRefreshToken()으로 리프레시 토큰 재발급, DB에 리프레시 토큰 업데이트 메서드 호출
    * JwtService.sendAccessTokenAndRefreshToken()으로 응답 헤더에 보내기
    */
    public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
        userRepository.findByRefreshToken(refreshToken)
                .ifPresent(user -> {
                    String reIssuedRefreshToken = reIssueRefreshToken(user);
                    jwtService.sendAccessAndRefreshToken(response, jwtService.createAccessToken(user.getEmail()),
                            reIssuedRefreshToken);
                });
    }

    /*
    * [리프레시 토큰 재발급 & DB에 리프레시 토큰 업데이트 메서드]
    * jwtService.createRefreshToken()으로 리프레시 토큰 재발급 후
    * DB에 재발급한 리프레시 토큰 업데이트 후 Flush
    * */
    private String reIssueRefreshToken(User user) {
        String reIssuedRefreshToken = jwtService.createRefreshToken();
        user.updateRefreshToken(reIssuedRefreshToken);
        userRepository.saveAndFlush(user);
        return reIssuedRefreshToken;
    }

    /*
    * [액세스 토큰 체크 & 인증 처리 메서드]
    * request에서 엑세스 토큰 추출 후, 유효한 토큰인지 검증
    * 유효한 토큰이면, 엑게스 토큰에서 이메일 추출 후 findByEmail()로 해당 이메일을 사용하는 멤버 객체 반환
    * 그 후 유저 객체를 saveAuthentication()으로 인증 처리하여
    * 인증 허가 처리된 객체를 SecurityContextHolder에 담기
    * 그 후 다음 인증 필터로 진행
    * */
    public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("checkAccessTokenAndAuthentication() 호출");
        jwtService.extractAccessToken(request) //추출
                .filter(jwtService::isTokenValid) //검증
                .ifPresent(accessToken -> jwtService.extractEmail(accessToken)
                        .ifPresent(email -> userRepository.findByEmail(email)
                                .ifPresent(this::saveAuthentication)));
        filterChain.doFilter(request, response);
    }

    /*
    * [인증 허가 메서드]
    * UsernamePasswordAuthenticationToken의 파라미터
    * 1. UserDetailsUser 객체 -> memberDetailMember (유저 정보)
    * 2. credential(보통 비밀번호로 함, 인증 시에는 null로 제거)
    * 3. Collection < ? extends GrantedAuthority >로
    * UserDetails의 User 객체 안에 Set<GrantedAuthority> authorities가 있어서 getter로 호출한 후에,
    * new NullAuthoritiesMapper()로 GrantedAuthoritiesMapper 객체 생성하고 mapAuthorities()에 담기
    *
    * SecurityContextHolder.getContext()로 SecurityContext를 꺼낸 후,
    * setAuthentication()을 이용하여 Authentication 객체에 대한 인증 허가 처리
    * */
    public void saveAuthentication(User myUser) {
        String password = myUser.getPassword();
        if (password == null) {
            password = PasswordUtil.generateRandomPassword();
        }

        UserDetails userDetailsUser = org.springframework.security.core.userdetails.User.builder()
                .username(myUser.getEmail())
                .password(password)
                .roles(myUser.getRole().name())
                .build();

        Authentication authentication = // 인증 객체인 Authentication 객체 생성
                new UsernamePasswordAuthenticationToken(userDetailsUser, null,
                        authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
