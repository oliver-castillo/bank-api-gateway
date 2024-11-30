package com.bank.apigateway.service;

import com.bank.apigateway.exception.NotFoundException;
import com.bank.apigateway.mapper.UserMapper;
import com.bank.apigateway.model.dto.request.UserRequest;
import com.bank.apigateway.model.dto.response.OperationResponse;
import com.bank.apigateway.model.dto.response.TokenResponse;
import com.bank.apigateway.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class DefaultUserService implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ReactiveAuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Override
    public Mono<OperationResponse> save(UserRequest userRequest) {
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        return userRepository.save(userMapper.toDocument(userRequest))
                .map(user -> new OperationResponse("Usuario guardado correctamente", HttpStatus.CREATED))
                .doOnSuccess(user -> log.info("Usuario guardado correctamente"))
                .doOnError(throwable -> log.error("Error al guardar el usuario: {}", throwable.getMessage()))
                .doOnTerminate(() -> log.info("Operación de guardar usuario finalizada"));
    }

    @Override
    public Mono<OperationResponse> update(String id, UserRequest userRequest) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Usuario no encontrado")))
                .flatMap(user -> {
                    user.setId(id);
                    user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
                    userMapper.toDocument(userRequest);
                    return userRepository.save(user);
                })
                .map(user -> new OperationResponse("Usuario actualizado correctamente", HttpStatus.OK))
                .doOnSuccess(user -> log.info("Usuario actualizado correctamente"))
                .doOnError(throwable -> log.error("Error al actualizar el usuario: {}", throwable.getMessage()))
                .doOnTerminate(() -> log.info("Operación de actualizar usuario finalizada"));
    }

    @Override
    public Mono<OperationResponse> delete(String id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Usuario no encontrado")))
                .flatMap(userRepository::delete)
                .then(Mono.fromCallable(() -> new OperationResponse("Usuario eliminado correctamente", HttpStatus.OK)))
                .doOnSuccess(user -> log.info("Usuario eliminado correctamente"))
                .doOnError(throwable -> log.error("Error al eliminar el usuario: {}", throwable.getMessage()))
                .doOnTerminate(() -> log.info("Operación de eliminar usuario finalizada"));
    }

    @Override
    public Mono<TokenResponse> signIn(UserRequest request) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        ).map(authentication -> {
            String token = tokenService.generateToken(authentication);
            return new TokenResponse(token);
        });
    }
}
