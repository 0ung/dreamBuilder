package codehows.dream.dreambulider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableJpaAuditing
public class DreamBuliderApplication {

	public static void main(String[] args) {
		SpringApplication.run(DreamBuliderApplication.class, args);
	}

}
