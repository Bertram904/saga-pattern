package com.tuananh.rideservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

/** Ride aggregate API + saga state + RIDE_REQUESTED / RELEASE_DRIVER outbox. */
@SpringBootApplication
@EnableKafka
@EnableScheduling
public class RideServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RideServiceApplication.class, args);
    }
}
