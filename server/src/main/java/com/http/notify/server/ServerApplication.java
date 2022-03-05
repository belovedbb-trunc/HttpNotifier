package com.http.notify.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerApplication {

    //Entry point for spring injection
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
