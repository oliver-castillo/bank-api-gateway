package com.bank.apigateway.config;

import com.bank.apigateway.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class CustomUserDetails implements ReactiveUserDetailsService {
    private final UserRepository repository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return repository.findByUsername(username)
                .map(user -> new User(user.getUsername(), user.getPassword(), Set.of()));
    }
}
