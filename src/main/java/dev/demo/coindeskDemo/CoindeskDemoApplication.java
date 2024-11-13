package dev.demo.coindeskDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CoindeskDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoindeskDemoApplication.class, args);
	}

}
