/**
 * @naseem please put author and purpose of this class
 */

package org.sdrc.lactation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@SpringBootApplication
@EnableWebMvc
@EnableTransactionManagement
public class Startup {
	
	public static void main(String[] args) {
		SpringApplication.run(Startup.class, args);
	}
	
	/**
	 * 
	 * @naseem, why do we need the following method?
	 */
	@Bean
	public MessageDigestPasswordEncoder passwordEncoder() {
		return new MessageDigestPasswordEncoder("MD5");
	}

}
