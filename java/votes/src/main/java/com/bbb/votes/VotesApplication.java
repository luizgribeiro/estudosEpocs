package com.bbb.votes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VotesApplication {
	@Value("server.port")
	static String teste;

	public static void main(String[] args) {
		SpringApplication.run(VotesApplication.class, args);
	}


}
