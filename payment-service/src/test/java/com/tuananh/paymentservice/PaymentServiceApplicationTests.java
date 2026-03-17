package com.tuananh.paymentservice;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentServiceApplicationTests {

    @Test
    void applicationClassPresent() {
        assertThat(PaymentServiceApplication.class.getSimpleName()).isEqualTo("PaymentServiceApplication");
    }
}
