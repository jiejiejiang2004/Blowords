package com.example.blowords.Words.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Definitions {
    private String partOfSpeech;
    private String definition;
    private String chineseTranslation;
    private String level;
    private String frequency;
    private String register;
}
