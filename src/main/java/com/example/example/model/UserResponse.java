package com.example.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse
{
    private long id;
    private String username;
    private String password;
    private List<NewsResponse> newsList = new ArrayList<>();
    private List<CommentResponse> comments = new ArrayList<>();
}
