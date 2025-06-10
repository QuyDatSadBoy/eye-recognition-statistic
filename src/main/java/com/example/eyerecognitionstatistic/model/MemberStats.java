package com.example.eyerecognitionstatistic.model;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberStats {
    private Integer memberId;
    private String username;
    private String firstName;
    private String lastName;
    private String fullName;
    private String department;
    private String email;
    private Integer successfulRecognitions;
    private Float averageAccuracy;
}
