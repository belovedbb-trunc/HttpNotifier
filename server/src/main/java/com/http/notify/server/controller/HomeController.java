package com.http.notify.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;

@RestController
@RequestMapping("/notify")
public class HomeController {
    //controller for endpoint /notify POST
    @PostMapping
    public ResponseEntity<String> bounceMessage(@RequestBody String message) {
        return ResponseEntity
            .created( URI.create("/notify"))
            .body(message);
    }
}
