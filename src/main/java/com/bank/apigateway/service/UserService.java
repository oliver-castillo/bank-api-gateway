package com.bank.apigateway.service;

import com.bank.apigateway.model.dto.request.UserRequest;
import com.bank.apigateway.model.dto.response.OperationResponse;
import com.bank.apigateway.model.dto.response.TokenResponse;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<OperationResponse> save(UserRequest userRequest);

    Mono<OperationResponse> update(String id, UserRequest userRequest);

    Mono<OperationResponse> delete(String id);

    Mono<TokenResponse> signIn(UserRequest request);
}
