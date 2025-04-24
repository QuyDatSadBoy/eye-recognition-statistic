package com.example.eyerecognitionstatistic.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    private Integer id;
    private String roleName;
    private String description;
}
