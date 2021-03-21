package com.banzo.auth.service;

import com.banzo.auth.model.User;
import com.banzo.auth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void findByUsername() {

        User user = new User();
        given(userRepository.findByUsername(anyString())).willReturn(Optional.of(user));

        Optional<User> foundUser = userService.findByUsername("admin");
        assertThat(foundUser).isPresent();

        then(userRepository).should().findByUsername("admin");
    }

    @Test
    void saveOrUpdate() {

        User user = new User();
        userService.saveOrUpdate(user);
        userService.saveOrUpdate(user);

        then(userRepository).should(times(2)).save(user);
    }

    @Test
    void findAll() {

        User user = new User();
        List<User> userList = new ArrayList<>();
        userList.add(user);

        given(userRepository.findAll()).willReturn(userList);
        Iterable<User> users = userService.findAll();
        assertThat(users).isNotNull();

        then(userRepository).should().findAll();
    }
}