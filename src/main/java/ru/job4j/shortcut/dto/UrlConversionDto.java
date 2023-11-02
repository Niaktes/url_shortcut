package ru.job4j.shortcut.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@NoArgsConstructor
public class UrlConversionDto {

    @URL
    private String url;

}