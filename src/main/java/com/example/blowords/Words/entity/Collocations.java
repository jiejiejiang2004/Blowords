package com.example.blowords.Words.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Collocations {
    private String pattern;
    private ArrayList<String> examples;
    private String strength;
}
