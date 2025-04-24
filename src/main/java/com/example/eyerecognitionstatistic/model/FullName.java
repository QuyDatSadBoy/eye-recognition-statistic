package com.example.eyerecognitionstatistic.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FullName {
    private Integer id;
    private String firstName;
    private String lastName;
}