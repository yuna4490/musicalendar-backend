package Musicalendar.musicalendarproject.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

//    @ElementCollection
////    @Enumerated(EnumType.STRING)
//    private List<String> site=new ArrayList<>();;

//    @ElementCollection
//    @Column(length=1000)
//    private List<String> image=new ArrayList<>(); // add 시 nullPointException 방지


//     값타입 컬렉션 -> 일대다 단방향
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name="ss_id")
//    @JsonBackReference //이거 JsonManagedReference로 해서 에러 떴었음
//    private List<ImageEntity> image=new ArrayList<>();
    private Set<ImageEntity> image = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "ss_id")
//    private List<SiteEntity> site=new ArrayList<>();
    private Set<SiteEntity> site = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name="show_id")
    @JsonManagedReference
    private Show show;

    @Builder
    public ShowSchedule(Boolean preCheck, String date, List<SiteEntity> site, List<ImageEntity> image, Show show){
        this.preCheck = preCheck;
        this.date = date;
        this.site = (Set<SiteEntity>) site;
        this.image = (Set<ImageEntity>) image;
        this.show = show;
    }
}
