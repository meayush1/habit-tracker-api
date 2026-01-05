package com.ayush.habittracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Myapp1Application {
	static {
	    io.github.cdimascio.dotenv.Dotenv.configure().ignoreIfMissing().load();
	}

	public static void main(String[] args) {
		SpringApplication.run(Myapp1Application.class, args);
	}

}
