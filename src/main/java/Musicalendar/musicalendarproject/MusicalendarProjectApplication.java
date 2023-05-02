package Musicalendar.musicalendarproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableCaching
@EnableJpaAuditing // JPA Auditing 활성화
@SpringBootApplication
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
