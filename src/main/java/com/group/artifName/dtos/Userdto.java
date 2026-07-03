package com.group.artifName.dtos;

import jakarta.persistence.*;
import lombok.Data;

@Data
public class UserDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID autoincremental (1, 2, 3...)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING) // Guarda el rol en la BD como texto ("USER", "ADMIN")
    @Column(nullable = false)
    private String role ;
}
