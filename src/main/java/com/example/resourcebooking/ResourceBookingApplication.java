package com.example.resourcebooking;




import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ResourceBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResourceBookingApplication.class, args);
	}
	
	
	 @Bean
	    CommandLineRunner generatePasswordHash(PasswordEncoder passwordEncoder) {
	        return args -> {
	            System.out.println("======================================");
	            System.out.println("BCrypt Hash for password:");
	            System.out.println(passwordEncoder.encode("password"));
	            System.out.println("======================================");
	        };
	    }
	

}
