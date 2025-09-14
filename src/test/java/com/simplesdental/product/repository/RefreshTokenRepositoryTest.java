package com.simplesdental.product.repository;

import com.simplesdental.product.model.RefreshToken;
import com.simplesdental.product.model.User;
import com.simplesdental.product.Enums.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Deve salvar e buscar RefreshToken por token")
    void testSaveAndFindByToken() {
        User user = new User();
        user.setName("João Silva");
        user.setEmail("joao@email.com");
        user.setPassword("123456");
        user.setRole(Role.USER);
        user = userRepository.save(user);

        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setToken("token123");
        token.setExpiryDate(new Date(System.currentTimeMillis() + 3600_000));
        refreshTokenRepository.save(token);

        Optional<RefreshToken> found = refreshTokenRepository.findByToken("token123");
        assertTrue(found.isPresent());
        assertEquals(user.getId(), found.get().getUser().getId());
    }

    @Test
    @DisplayName("Deve verificar se o RefreshToken expirou")
    void testIsExpired() {
        User user = new User();
        user.setName("Maria Souza");
        user.setEmail("maria@email.com");
        user.setPassword("654321");
        user.setRole(Role.ADMIN);
        user = userRepository.save(user);

        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setToken("expiredToken");
        token.setExpiryDate(new Date(System.currentTimeMillis() - 1000));
        refreshTokenRepository.save(token);

        Optional<RefreshToken> found = refreshTokenRepository.findByToken("expiredToken");
        assertTrue(found.isPresent());
        assertTrue(found.get().isExpired());
    }
}
