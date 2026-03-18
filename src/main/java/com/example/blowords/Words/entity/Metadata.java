package com.example.blowords.Words.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Metadata {
    private String frequency;
    private ArrayList<String> domains;
    private ArrayList<String> tags;
    private Date lastUpdated;
    private ArrayList<String> sources;
}
