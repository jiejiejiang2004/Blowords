package com.example.blowords.Words.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SyntacticPatterns {
    private String pattern;
    private String description;
    private ArrayList<String> examples;
}
