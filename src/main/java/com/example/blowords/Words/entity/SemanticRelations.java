package com.example.blowords.Words.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SemanticRelations {
    private ArrayList<SynonymsWords> synonyms;
    private ArrayList<AntonymsWords> antonyms;
    private ArrayList<String> hypernyms;
    private ArrayList<String> hyponyms;
    private ArrayList<Collocations> collocations;
}
