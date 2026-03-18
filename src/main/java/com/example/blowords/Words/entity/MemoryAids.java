package com.example.blowords.Words.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemoryAids {
    private VisualScene visualScene;
    private ArrayList<MnemonicDevices> mnemonicDevices;
    private ArrayList<String> wordAssociations;
}
