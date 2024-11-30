package com.bank.apigateway.controller;

import com.bank.apigateway.model.dto.request.UserRequest;
import com.bank.apigateway.model.dto.response.OperationResponse;
import com.bank.apigateway.model.dto.response.TokenResponse;
import com.bank.apigateway.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<OperationResponse> save(@RequestBody @Valid UserRequest userRequest) {
        return userService.save(userRequest);
    }

    @PutMapping("{userId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<OperationResponse> update(@PathVariable String userId, @RequestBody @Valid UserRequest userRequest) {
        return userService.update(userId, userRequest);
    }

    @DeleteMapping("{userId}")
    @ResponseStatus(value = HttpStatus.OK)
    public Mono<OperationResponse> delete(@PathVariable String userId) {
        return userService.delete(userId);
    }

    @PostMapping("/sign-in")
    public Mono<TokenResponse> signIn(@RequestBody @Valid UserRequest userRequest) {
        return userService.signIn(userRequest);
    }
}
