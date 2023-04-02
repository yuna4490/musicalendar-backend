package Musicalender.musicalenderproject.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "bookMark")
public class BookMark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id", unique = true, nullable = false)
    private Long bookmark_id;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="show_id")
    private Show show;

    @Builder
    public BookMark(Member member, Show show){
        this.member = member;
        this.show = show;
    }

}