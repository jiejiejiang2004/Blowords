package com.example.blowords.Words.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DifficultyAnalysis {
    private String overallLevel;
    private String pronunciationDifficulty;
    private String spellingDifficulty;
    private String meaningComplexity;
    private String usageComplexity;
    private ArrayList<String> learningTips;
}
