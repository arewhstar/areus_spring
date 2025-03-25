package com.example.demo.Entites;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Firstname is required")
    @Size(min = 2, max = 50, message = "Firstname must be between 2 and 50 characters")
    private String firstname;

    @NotBlank(message = "Lastname is required")
    @Size(min = 2, max = 50, message = "Lastname must be between 2 and 50 characters")
    private String lastname;

    @Positive(message = "Age must be a positive whole number")
    private int age;

    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address must be at most 255 characters")
    private String address;
}
