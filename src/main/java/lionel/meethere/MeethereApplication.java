package lionel.meethere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class MeethereApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeethereApplication.class, args);
    }

}
