package com.simplesdental.product.repository;

import com.simplesdental.product.Enums.Role;
import com.simplesdental.product.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Deve salvar e buscar usuário por email")
    void testSaveAndFindByEmail() {
        User user = new User();
        user.setName("João da Silva");
        user.setEmail("joao@email.com");
        user.setPassword("senha123");
        user.setRole(Role.USER);
        userRepository.save(user);

        Optional<User> found = userRepository.findByEmail("joao@email.com");
        assertTrue(found.isPresent());
        assertEquals("João da Silva", found.get().getName());
        assertEquals(Role.USER, found.get().getRole());
    }

    @Test
    @DisplayName("Deve retornar vazio para email inexistente")
    void testFindByEmailNotFound() {
        Optional<User> found = userRepository.findByEmail("naoexiste@email.com");
        assertTrue(found.isEmpty());
    }
}
