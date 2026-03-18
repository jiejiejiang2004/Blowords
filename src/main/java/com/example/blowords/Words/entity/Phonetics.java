package com.example.blowords.Words.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

//@Document(collection = "words.phonetics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Phonetics {
    private String british;
    private String american;
}
