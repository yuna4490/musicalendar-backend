package Musicalender.musicalenderproject.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShowSchedule {

    @Id
    @Column(name = "ss_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(nullable = false)
    private Boolean preCheck;

    @Column(length = 20, nullable = false)
    private String date; //type 확인 필요

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<Site> site=new ArrayList<>();;

    @ElementCollection
    @Column(length=1000)
    private List<String> image=new ArrayList<>(); // add 시 nullPointException 방지

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name="show_id")
    @JsonManagedReference
    private Show show;

    @Builder
    public ShowSchedule(Boolean preCheck, String date, List<Site> site, List<String> image, Show show){
        this.preCheck = preCheck;
        this.date = date;
        this.site = site;
        this.image = image;
        this.show = show;
    }
}
