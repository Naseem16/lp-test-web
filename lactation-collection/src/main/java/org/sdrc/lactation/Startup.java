/**
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 12th February 2018 2057.
 * This Configuration class can declare one or more
 * Bean methods and also triggers EnableAutoConfiguration
 * and component scanning. This class will be triggered as soon as the project is deployed in the server.
 * 
 */

package org.sdrc.lactation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@EnableTransactionManagement
@PropertySource("file:${catalina.base}/conf/lactation_application_dev.properties")
public class Startup extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(Startup.class, args);
	}
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Startup.class);
    }

	/**
	 * 
	 * @author Naseem Akhtar (naseem@sdrc.co.in) on 12th February 2018 2057.
	 * In this method we are creating a bean of {@link MessageDigestPasswordEncoder} with MD5 algorithm.
	 * We are going to use this for the online users who are going to register themselves.
	 */
	@Bean
	public MessageDigestPasswordEncoder passwordEncoder() {
		return new MessageDigestPasswordEncoder("MD5");
	}

}
