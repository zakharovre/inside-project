package ru.zakharovre.inside.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.zakharovre.inside.entity.Role;
import ru.zakharovre.inside.entity.User;
import ru.zakharovre.inside.repository.RoleRepository;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    public UserService userService;

    @Autowired
    public RoleRepository roleRepository;

    private final User user1 = new User();

    @BeforeEach
    public void setUp() {
        Role role = new Role();
        role.setId(1L);
        role.setRole("TEST");
        roleRepository.save(role);
        user1.setId(1L);
        user1.setUsername("username1");
        user1.setPassword("password");
        user1.setRoles(new HashSet<>(Collections.singletonList(role)));
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("username2");
        user2.setPassword("password");
        userService.addUser(user1);
        userService.addUser(user2);
    }

    @Test
    public void findAllTest() {
        assertEquals(2, userService.findAll().size());
        userService.deleteAll();
        assertEquals(0, userService.findAll().size());
    }

    @Test
    public void loadUserByUsernameTest() {
        List<SimpleGrantedAuthority> authorities = user1.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole()))
                .collect(Collectors.toList());
        org.springframework.security.core.userdetails.User testUser = new org.springframework.security.core.userdetails.User(
                user1.getUsername(), user1.getPassword(), authorities);
        assertEquals(testUser, userService.loadUserByUsername(user1.getUsername()));

    }

    @Test
    public void loadUserByUsernameShouldThrowExceptionWhenUserNotExist() {
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("nonExistedUser"));
    }

    @AfterEach
    public void tearDown() {
        userService.deleteAll();
    }
}
