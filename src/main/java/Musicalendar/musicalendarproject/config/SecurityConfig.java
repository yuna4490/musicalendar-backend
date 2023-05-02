package Musicalendar.musicalendarproject.config;

import Musicalendar.musicalendarproject.jwt.filter.JwtAuthenticationProcessingFilter;
import Musicalendar.musicalendarproject.login.filter.CustomJsonMembernamePasswordAuthenticationFilter;
import Musicalendar.musicalendarproject.login.filter.CustomJsonUsernamePasswordAuthenticationFilter;
import Musicalendar.musicalendarproject.login.handler.LoginSuccessHandler;
import Musicalendar.musicalendarproject.login.handler.LoginFailureHandler;
import Musicalendar.musicalendarproject.login.service.LoginService;
import Musicalendar.musicalendarproject.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import Musicalendar.musicalendarproject.jwt.service.JwtService;
import Musicalendar.musicalendarproject.oauth2.handler.OAuth2LoginFailureHandler;
import Musicalendar.musicalendarproject.oauth2.handler.OAuth2LoginSuccessHandler;
import Musicalendar.musicalendarproject.oauth2.service.CustomOAuth2UserService;
import Musicalendar.musicalendarproject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;


/*
* 인증은 CustomJsonMembernamePasswordAuthenticationFilter에서 authenticate()로 인증된 사용자로 처리
* JwtAuthenticationProcessingFilter는 AccessToken, RefreshToken 재발급
* */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final LoginService loginService;
    private final JwtService jwtService;
    //private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .formLogin().disable() //FormLogin 사용 안함
                .httpBasic().disable() //httpBasic 사용 안함
                .csrf().disable()// csrf 보안 사용 안함
                .headers().frameOptions().disable()
                .and()

                // 세션 사용하지 않으므로 STATELESS로 설정
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()

                // ==URL별 권한 관리 옵션== //
                .authorizeRequests()

                // 아이콘, css, js 관련
                // 기본 페이지, css, image, js 하위 폴더에 있는 자료들은 모두 접근 가능, h2-console에 접근 가능
                .antMatchers("/","/css","/images/**","/js/**","/favicon.ico","/h2-console/**").permitAll()
                .antMatchers("/sign-up").permitAll() // 회원가입 인증없이 접근 가능
                .anyRequest().authenticated() // 위 경로 제외 인증된 사용자만 접근 가능
                .and()

                // ==소셜 로그인 설정== //
                .oauth2Login()
                .successHandler(oAuth2LoginSuccessHandler) // 동의하고 계속하기 눌렀을 때 Handler 설정
                .failureHandler(oAuth2LoginFailureHandler) // 스셜 로그인 실패 시 핸들러 설정
                .userInfoEndpoint().userService(customOAuth2UserService); // customUserService 설정

        http.addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class); //addFilterAfter(A, B) B필터 이후에 A필터 동작
        http.addFilterBefore(jwtAuthenticationProcessingFilter(), CustomJsonUsernamePasswordAuthenticationFilter.class); // B필터 이전에 A필터 동작

        return http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /*
    * AuthenticationManager 설정 후 등록
    * PasswordEncoder를 사용하는 AuthenticationProvider 지정
    * FormLogin(기존 스프링 시큐리티 로그인)과 동일하게 DadAuthenticationProvider 사용
    * UserDetailsService 커스텀 LoginService 등록
    * 또한 FormLogin과 동일하게 AutheticationManager로는 구현체인 ProviderManager 사용(return ProviderManager)
    * */
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(loginService);
        return new ProviderManager(provider);
    }

    /*
    * 로그인 성공 시 호출되는 LoginSuccessJWTProviderHandler 빈 등록
    * */
    @Bean
    public LoginSuccessHandler loginSuccessHandler() {

        return new LoginSuccessHandler(jwtService, userRepository);
    }

    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }
    /*
    * CustomJsonMembernamePasswordAuthenticationFilter 빈 등록
    * 커스텀 필터를 사용하기 위해 만든 커스텀 필터를 빈으로 등록
    * setAuthenticationManager(authenticationManager())로 위에서 등록한 AuthenticationManager(ProviderManager) 설정
    * 로그인 성공 시 호출할 handler 실패 시 호출할 handler로 위에서 등록한 handler 설정
    * */
    @Bean
    public CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter() {
        CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordLoginFilter
                = new CustomJsonUsernamePasswordAuthenticationFilter(objectMapper);
        customJsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
        customJsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        customJsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
        return customJsonUsernamePasswordLoginFilter;
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        JwtAuthenticationProcessingFilter jwtAuthenticationFilter = new JwtAuthenticationProcessingFilter(jwtService, userRepository);
        return jwtAuthenticationFilter;
    }


}
