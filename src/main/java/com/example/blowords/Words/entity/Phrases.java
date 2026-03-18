package com.example.blowords.Words.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Phrases {
    private String phrase;
    private String meaning;
    private String example;
    private String exampleTranslation;
    private String frequency;
}
