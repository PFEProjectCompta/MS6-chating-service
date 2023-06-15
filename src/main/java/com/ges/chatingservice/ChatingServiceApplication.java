package com.ges.chatingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@SpringBootApplication
public class ChatingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatingServiceApplication.class, args);
	}

}
