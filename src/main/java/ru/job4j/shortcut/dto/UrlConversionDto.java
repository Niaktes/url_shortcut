package ru.job4j.shortcut.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@NoArgsConstructor
public class UrlConversionDto {

    @URL(message = "Please use URL address.")
    private String url;

}