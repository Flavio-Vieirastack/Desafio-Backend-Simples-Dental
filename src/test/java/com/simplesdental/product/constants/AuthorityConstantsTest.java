package com.simplesdental.product.constants;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorityConstantsTest {

    @Test
    void testConstantsValues() {
        assertThat(AuthorityConstants.ADMIN).isEqualTo("ADMIN");
        assertThat(AuthorityConstants.USER).isEqualTo("USER");
    }
}
