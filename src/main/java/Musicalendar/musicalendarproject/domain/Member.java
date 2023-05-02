package Musicalendar.musicalendarproject.domain;

import lombok.*;
import org.hibernate.query.ImmutableEntityUpdateQueryHandlingMode;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Getter
@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "member_id")
    private Long id;

    @Column(length = 60, nullable = false)
    private String nickname;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 60, nullable = false)
    private String password;

    //@Column(length = 20, nullable = false)
    //private String phoneNumber;

    //@Enumerated(EnumType.STRING)
    //private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType; // KAKAO or GOOGLE

    @Enumerated(EnumType.STRING)
    private Role role;

    private String socialId; //로그인한 소셜 타입의 식별자 값

    private String refreshToken;

    //사용자 권한 설정 메서드
    public void authorizeMember() {

        this.role = Role.MEMBER;
    }

    //비밀번호 암호화 메서드
    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }

    @Builder
    public Member(String nickname, String email, String password, Role role){
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        //this.phoneNumber = phoneNumber;
        this.role = role;
    }


}
