package com.example.blowords.Words.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IrregularForms {
    private String plural;
    private String pastTense;
    private String pastParticiple;
    private String presentParticiple;
    private String comparative;
    private String superlative;
}
