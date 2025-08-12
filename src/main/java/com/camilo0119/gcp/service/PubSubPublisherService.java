package com.camilo0119.gcp.service;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class PubSubPublisherService {

    private static final String TOPIC_ID = "my-topic";

    @Value("${gcp.project.id}")
    private String PROJECT_ID;

    public void publishMessage(String message) throws Exception {
        ProjectTopicName topicName = ProjectTopicName.of(PROJECT_ID, TOPIC_ID);
        Publisher publisher = null;

        try {
            publisher = Publisher.newBuilder(topicName).build();
            ByteString data = ByteString.copyFrom(message, StandardCharsets.UTF_8);
            com.google.pubsub.v1.PubsubMessage pubsubMessage =
                    com.google.pubsub.v1.PubsubMessage.newBuilder().setData(data).build();

            ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);

            ApiFutures.addCallback(
                    messageIdFuture,
                    new com.google.api.core.ApiFutureCallback<>() {
                        @Override
                        public void onFailure(Throwable t) {
                            log.error("Error al publicar mensaje: {}", t.getMessage(), t);
                        }

                        @Override
                        public void onSuccess(String messageId) {
                            log.info("Mensaje publicado con ID: {}", messageId);
                        }
                    },
                    Runnable::run
            );

        } finally {
            if (publisher != null) {
                publisher.shutdown();
                publisher.awaitTermination(1, TimeUnit.MINUTES);
            }
        }
    }
}
