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
public class UpsertNewsRequest
{
    @NotNull(message = "Id пользователя должно быть указано!")
    @Positive(message = "Id пользователя должно быть больше нуля!")
    private Long userId;

    @NotNull(message = "Id категории должно быть указано!")
    @Positive(message = "Id категории должно быть больше нуля!")
    private Long categoryId;

    @NotBlank(message = "Название новости должно быть заполнено!")
    private String title;

    @NotBlank(message = "Текст новости не должен быть пустым")
    private String text;
}
