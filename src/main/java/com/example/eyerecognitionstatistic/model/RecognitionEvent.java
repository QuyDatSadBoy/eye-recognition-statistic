package com.example.eyerecognitionstatistic.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecognitionEvent {
    private Integer id;
    private String imageLink;
    private Integer recognitionModelId;
    private Integer eyeDetectionModelId;
    private String cameraName;
    private Date timeVerify;
    private Integer isSuccessful;
    private Float accuracy;
    private Integer memberId;
}
