package practice.reactiveWeb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.thymeleaf.TemplateEngine;
import reactor.blockhound.BlockHound;

@SpringBootApplication
public class ReactiveWebApplication {

	public static void main(String[] args) {
		// instrument byte code before run
		BlockHound.builder()
				  .allowBlockingCallsInside(TemplateEngine.class.getCanonicalName(), "process")
				  .install();
		SpringApplication.run(ReactiveWebApplication.class, args);
	}
}
