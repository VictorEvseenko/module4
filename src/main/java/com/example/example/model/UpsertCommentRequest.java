package com.example.example.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpsertCommentRequest
{
    private Long userId;

    @NotNull(message = "Id новости должно быть указано!")
    @Positive(message = "Id новости должно быть больше нуля!")
    private Long newsId;

    @NotBlank(message = "Комментарий не должен быть пустым!")
    private String text;
}
