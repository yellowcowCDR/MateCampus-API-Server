package com.litCitrus.zamongcampusServer;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.io.FileInputStream;
import java.io.IOException;

@SpringBootApplication
@EnableJpaAuditing
public class ZamongcampusServerApplication {

	@Bean
	FirebaseMessaging firebaseMessaging() throws IOException {
		GoogleCredentials googleCredentials = GoogleCredentials
				.fromStream(new FileInputStream("/app/config/zamongcampus-server/firebase-service-account.json"));
		FirebaseOptions firebaseOptions = FirebaseOptions
				.builder()
				.setCredentials(googleCredentials)
				.build();
		FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions, "my-app");
		return FirebaseMessaging.getInstance(app);
	}

//    public static void main(String[] args) {
//        SpringApplication.run(ServerApplication.class, args);
//    }


	public static final String APPLICATION_LOCATIONS = "spring.config.location="
			+ "classpath:application.yml,"
			+ "/app/config/zamongcampus-server/application-dev.yml,"
			+ "/app/config/zamongcampus-server/application-prod.yml,"
			+ "/app/config/zamongcampus-server/aws.yml";

	public static void main(String[] args) {
		new SpringApplicationBuilder(ZamongcampusServerApplication.class)
				.properties(APPLICATION_LOCATIONS)
				.run(args);
	}



}

