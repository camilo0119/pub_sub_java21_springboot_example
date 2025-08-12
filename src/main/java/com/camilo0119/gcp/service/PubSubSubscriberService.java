package com.camilo0119.gcp.service;

import com.google.api.core.ApiService;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
@Slf4j
public class PubSubSubscriberService {

    private static final String SUBSCRIPTION_ID = "my-sub";

    @Value("${gcp.project.id}")
    private String PROJECT_ID;

    private Subscriber subscriber;

    @PostConstruct
    public void startSubscriber() {
        ProjectSubscriptionName subscriptionName =
                ProjectSubscriptionName.of(PROJECT_ID, SUBSCRIPTION_ID);

        subscriber = Subscriber.newBuilder(subscriptionName, this::receiveMessage).build();

        subscriber.addListener(new ApiService.Listener() {
            @Override
            public void failed(ApiService.State from, Throwable failure) {
                log.error("Error en el subscriber: {}", failure.getMessage(), failure);
            }
        }, Runnable::run);

        subscriber.startAsync().awaitRunning();
        log.info("Subscriber iniciado, escuchando en {}", subscriptionName);
    }

    private void receiveMessage(PubsubMessage message, AckReplyConsumer consumer) {
        String data = message.getData().toStringUtf8();
        log.info("📩 Mensaje recibido: {}", data);

        // Aquí procesas tu mensaje
        procesarMensaje(data);

        consumer.ack(); // confirmamos a Pub/Sub que el mensaje fue procesado
    }

    private void procesarMensaje(String contenido) {
        log.info("Procesando mensaje: {}", contenido);
        // Ejemplo: podrías guardar en BD, llamar otro servicio, etc.
    }

    @PreDestroy
    public void shutdown() {
        if (subscriber != null) {
            subscriber.stopAsync();
        }
    }
}
