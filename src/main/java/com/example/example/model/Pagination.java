package com.example.example.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Pagination
{
    private Integer PageSize;

    private Integer PageNumber;
}
