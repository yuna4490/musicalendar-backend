package Musicalender.musicalenderproject.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "showSchedule")
public class ShowSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ss_id", unique = true, nullable = false)
    private Long ss_id;

    @Column(nullable = false)
    private Boolean preCheck;

    @Column(length = 30, nullable = false)
    private String title;

    @Column(length = 20, nullable = false)
    private String date; //type 확인 필요

    @Enumerated(EnumType.STRING)
    private Site site;

    @Column(length = 200)
    private String image;

    @ManyToOne
    @JoinColumn(name="show_id")
    private Show show;

    @Builder
    public ShowSchedule(Boolean preCheck, String title, String date, Site site, String image, Show show){
        this.preCheck = preCheck;
        this.title = title;
        this.date = date;
        this.site = site;
        this.image = image;
        this.show = show;
    }
}
