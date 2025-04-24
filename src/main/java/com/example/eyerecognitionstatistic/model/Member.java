package com.example.eyerecognitionstatistic.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    private Integer id;
    private String username;
    private String email;
    private Integer phoneNumber;
    private String department;
    private FullName fullName;
    private Role role;
}
