package com.example.blowords.Words.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;

@Document(collection = "words")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Words{
    private String _id;
    private String word;
    private Phonetics phonetics;
    private ArrayList<Definitions> definitions;
    private ArrayList<Phrases> phrases;
    private ArrayList<Examples> examples;
    private Etymology etymology;
    private DifficultyAnalysis difficultyAnalysis;
    private SemanticRelations  semanticRelations;
    private CulturalContext culturalContext;
    private MemoryAids memoryAids;
    private GrammaticalInfo  grammaticalInfo;
    private Metadata metadata;
    private Integer word_id;
}
