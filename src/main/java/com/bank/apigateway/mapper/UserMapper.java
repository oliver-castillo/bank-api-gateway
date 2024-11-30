package com.bank.apigateway.mapper;

import com.bank.apigateway.model.document.User;
import com.bank.apigateway.model.dto.request.UserRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toDocument(UserRequest request);
}
