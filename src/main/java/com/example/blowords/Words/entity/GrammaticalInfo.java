package com.example.blowords.Words.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrammaticalInfo {
    private IrregularForms  irregularForms;
    private ArrayList<SyntacticPatterns> syntacticPatterns;
    private ArrayList<CommonMistakes>  commonMistakes;
}
