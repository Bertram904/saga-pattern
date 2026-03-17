package com.tuananh.rideservice;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Full {@link org.springframework.boot.test.context.SpringBootTest} requires Postgres + Kafka (see compose.yaml).
 */
class RideServiceApplicationTests {

    @Test
    void applicationClassPresent() {
        assertThat(RideServiceApplication.class.getSimpleName()).isEqualTo("RideServiceApplication");
    }
}
