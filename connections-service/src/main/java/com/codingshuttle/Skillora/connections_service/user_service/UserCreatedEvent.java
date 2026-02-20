package com.codingshuttle.Skillora.connections_service.user_service;

import lombok.Data;

@Data
public class UserCreatedEvent {
    private String name;
    private  Long user_id;
}
