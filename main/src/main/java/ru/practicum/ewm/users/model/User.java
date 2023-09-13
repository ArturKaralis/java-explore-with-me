package ru.practicum.ewm.users.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "email")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull(message = "User name can't be Null")
    @NotBlank(message = "User name can't be Blank")
    @Column(nullable = false)
    String name;

    @NotNull(message = "User email can't be Null")
    @NotBlank(message = "user email can't be Blank")
    @Email(message = "Invalid email format")
    @Column(nullable = false, unique = true)
    String email;

    @Column(name = "likes")
    Integer likes;

    @Column(name = "dislikes")
    Integer dislikes;

    @Column(name = "rate")
    Double rate;
}