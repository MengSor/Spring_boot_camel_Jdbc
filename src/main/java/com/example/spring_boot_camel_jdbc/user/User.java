package com.example.spring_boot_camel_jdbc.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "users")
public class User {
    private Long id;
    private String name;
    private String email;
}
