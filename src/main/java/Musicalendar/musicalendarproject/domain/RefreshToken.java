package Musicalendar.musicalendarproject.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

// ****************** Token 만료 시 자동 삭제 위해 추후 Redis 로 바꾸기
// ****************** RDB -> 생성/수정 시간 컬럼을 추가하여 배치 작업으로 만료된 토큰들을 삭제해야 함 ㄱ-
@Getter
@NoArgsConstructor
@Entity
@Table
public class RefreshToken {

    @Id
    @Column(name = "rt_key")
    private Long key; // Member ID

    @Column(name = "rt_value")
    private String value;   // RefreshToken

    @Column
    private Long expirationTime;

    @Builder
    public RefreshToken(Long key, String value, Long expirationTime) {
        this.key = key;
        this.value = value;
        this.expirationTime=expirationTime;
    }

    public RefreshToken updateValue(String token) {
        this.value = token;
        return this;
    }
}
