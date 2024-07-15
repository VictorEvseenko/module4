package com.example.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewsFilter
{
    private Long userId;

    private Long categoryId;
}
