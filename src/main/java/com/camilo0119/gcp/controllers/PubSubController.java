package com.camilo0119.gcp.controllers;

import com.camilo0119.gcp.service.PubSubPublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pubsub")
@RequiredArgsConstructor
public class PubSubController {

    private final PubSubPublisherService publisherService;

    @PostMapping("/publish")
    public String publish(@RequestParam String message) throws Exception {
        publisherService.publishMessage(message);
        return "Mensaje enviado: " + message;
    }
}