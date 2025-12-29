package com.example.reviews_service;

import com.example.reviews_service.dto.ReviewDto;
import com.example.reviews_service.repository.ReviewRepository;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(partitions = 1, topics = {"review-created"})
@ActiveProfiles("test")
public class ReviewIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    private static KafkaConsumer<String, String> consumer;

    @BeforeAll
    static void setupConsumer() {
        // consumer will be initialized in test because EmbeddedKafkaBroker is not static-injectable here
    }

    @AfterAll
    static void teardown() {
        if (consumer != null) consumer.close();
    }

    @Test
    public void createReview_publishesEvent_and_savesToDb() throws Exception {
        ReviewDto dto = new ReviewDto();
        dto.userId = 1L;
        dto.restaurantId = 1L;
        dto.rating = 5;
        dto.comment = "Great!";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ReviewDto> req = new HttpEntity<>(dto, headers);

        ResponseEntity<ReviewDto> resp = restTemplate.postForEntity("/api/reviews", req, ReviewDto.class);
        assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();

        // check DB
        assertThat(reviewRepository.findAll()).isNotEmpty();

        // setup consumer
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafkaBroker.getBrokersAsString());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("review-created"));

        ConsumerRecord<String, String> record = null;
        long end = System.currentTimeMillis() + 5000;
        while (System.currentTimeMillis() < end) {
            var recs = consumer.poll(Duration.ofMillis(200));
            if (!recs.isEmpty()) {
                record = recs.iterator().next();
                break;
            }
        }
        assertThat(record).isNotNull();
        assertThat(record.value()).contains("Great!");
    }
}

