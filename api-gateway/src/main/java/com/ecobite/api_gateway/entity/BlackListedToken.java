package com.ecobite.api_gateway.entity;


import jakarta.persistence.*;

@Entity
public class BlackListedToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
}
