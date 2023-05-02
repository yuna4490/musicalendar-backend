package Musicalendar.musicalendarproject.auth.service;

import Musicalendar.musicalendarproject.auth.CustomUserDetails;
import Musicalendar.musicalendarproject.auth.jwt.TokenProvider;
import Musicalendar.musicalendarproject.domain.RefreshToken;
import Musicalendar.musicalendarproject.dto.TokenDto;
import Musicalendar.musicalendarproject.exception.CustomException;
import Musicalendar.musicalendarproject.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;

import static Musicalendar.musicalendarproject.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public void reissue(String accessToken, String refreshToken){

        // 1. Refresh Token 만료 여부 검증
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new CustomException(INVALID_REFRESH_TOKEN);
            // 로그아웃 시켜야 함.
        }

        // 2. Access Token 복호화 -> Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
//        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
//
//        Long id=Long.valueOf(user.getName());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken storedRefreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new CustomException(REFRESH_TOKEN_NOT_FOUND));

        // 4. 클라이언트의 Refresh Token 일치하는지 검사
        if (!storedRefreshToken.getValue().equals(refreshToken)) {
            throw new CustomException(MISMATCH_REFRESH_TOKEN);
        }

        // 5. (일치할 경우) 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트 (이전의 Refresh Token 을 사용할 수 없도록)
        RefreshToken newRefreshToken = storedRefreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);
    }

}
