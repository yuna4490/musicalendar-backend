package Musicalendar.musicalendarproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
//@EnableJpaRepositories(basePackages = {"Musicalendar.class"}) // com.my.jpa.repository 하위에 있는 jpaRepository를 상속한 repository scan
//@EntityScan(basePackages = {"Musicalendar.musicalendarproject.domain"}) // com.my.jpa.entity 하위에 있는 @Entity 클래스 scan
public class MusicalendarProjectApplication {
	@PostConstruct
	public void started() {
		//timezone UTC 셋팅
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
	public static void main(String[] args) {

		SpringApplication.run(MusicalendarProjectApplication.class, args);

	}

}
