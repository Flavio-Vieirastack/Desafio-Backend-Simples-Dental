package com.simplesdental.product.constants;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorityConstantsTest {

    @Test
    void testConstantsValues() {
        assertThat(AuthorityConstants.ADMIN).isEqualTo("hasAuthority('SCOPE_ADMIN')");
        assertThat(AuthorityConstants.USER).isEqualTo("hasAuthority('SCOPE_USER')");
    }
}
