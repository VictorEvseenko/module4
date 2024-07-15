package com.example.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsResponseWithCountComments
{
    private Long id;
    private String title;
    private String text;
    private int countComments;
}
