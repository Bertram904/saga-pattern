package com.tuananh.driverservice;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DriverServiceApplicationTests {

    @Test
    void applicationClassPresent() {
        assertThat(DriverServiceApplication.class.getSimpleName()).isEqualTo("DriverServiceApplication");
    }
}
