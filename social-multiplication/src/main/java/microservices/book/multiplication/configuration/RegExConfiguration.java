package microservices.book.multiplication.configuration;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RegExConfiguration {
	@Bean("invalidProperyPattern")
	Pattern pattern(@Value("(?<=\\\\\\\").*?(?=\\\\\\\")") String regex) {
		return Pattern.compile(regex);
	}
}
