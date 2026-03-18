package com.example.blowords.Words.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CulturalContext {
    private String culturalSignificance;
    private ArrayList<RegionalVariations> regionalVariations;
    private String historicalContext;
}
