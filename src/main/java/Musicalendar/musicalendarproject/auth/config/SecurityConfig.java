package Musicalendar.musicalendarproject.auth.config;

import Musicalendar.musicalendarproject.auth.CookieAuthorizationRequestRepository;
import Musicalendar.musicalendarproject.auth.service.AuthService;
import Musicalendar.musicalendarproject.auth.service.CustomOAuth2UserService;
import Musicalendar.musicalendarproject.auth.handler.OAuth2LoginFailureHandler;
import Musicalendar.musicalendarproject.auth.handler.OAuth2LoginSuccessHandler;
import Musicalendar.musicalendarproject.auth.jwt.TokenProvider;
import Musicalendar.musicalendarproject.auth.jwt.handler.JwtAccessDeniedHandler;
import Musicalendar.musicalendarproject.auth.jwt.handler.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig{

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final TokenProvider tokenProvider;
    private final AuthService authService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;

//    @Bean
//    public BCryptPasswordEncoder encode(){
//        return new BCryptPasswordEncoder();
//    }

    @Bean   // ignoring() 대상 요청에 대해 스프링 시큐리티 적용 X
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web -> web.ignoring().antMatchers("/resources/**", "/js/**", "/h2-console/**", "/favicon.ico"));
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception{
        // CORS & CSRF 설정 Disable
        http.cors().and().csrf().disable()  // CORS (ON) & CSRF (OFF)
                .formLogin().disable()
                // exception handling 할 때 Custom 클래스를 추가
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)  // 유저 정보 없이 접근 (401)
                .accessDeniedHandler(jwtAccessDeniedHandler)    // 권한 없이 접근 (403)

//                // exception handling 할 때 Custom 클래스를 추가
//                .exceptionHandling()
//                .authenticationEntryPoint(jwtAuthenticationEntryPoint)  // 유저 정보 없이 접근
//                .accessDeniedHandler(jwtAccessDeniedHandler)    // 권한 없이 접근

                // h2-console 설정 추가
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                // 시큐리티는 기본적으로 세션을 사용
                // 세션을 사용 X -> Stateless 로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 로그인, 회원가입 API 는 토큰이 없는 상태에서 요청이 들어오기 때문에 permitAll 설정
                .and()
                .authorizeRequests()
//                .antMatchers("/", "/**").permitAll()
                .antMatchers("/", "/login/**", "/oauth2/**", "/hello").permitAll()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/h2-console/**", "/favicon.ico").permitAll()
                .anyRequest().authenticated()   // 나머지 API 는 전부 인증 필요
                .and()
//                .logout()
//                .logoutSuccessUrl("/")
//                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/login")   // 소셜 로그인 요청 보내는 URI
                .authorizationRequestRepository(cookieAuthorizationRequestRepository)   // 세션 -> 쿠키
                .and()
                .redirectionEndpoint()
                .baseUri("/oauth2/login/callback/**")
                .and()
                .userInfoEndpoint() // OAuth2 로그인 성공 이후 사용자 정보를 가져올 때 설정을 저장
                .userService(customOAuth2UserService)  // 로그인 성공 시, 유저 정보로 후처리 (aT + 사용자 정보)
                .and()
                .successHandler(oAuth2LoginSuccessHandler)  // 로그인 성공 후처리
                .failureHandler(oAuth2LoginFailureHandler);  // 로그인 실패 후처리

        // JwtFilter 를 addFilterBefore 로 등록했던 JwtConfig 클래스를 적용
        http.apply(new JwtConfig(tokenProvider, authService));

        return http.build();
    }
}
