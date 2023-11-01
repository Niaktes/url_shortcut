package ru.job4j.shortcut.dto;

import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SiteRegistrationDto {

    @Pattern(regexp = "^((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,6}$",
            message = "Wrong domain name")
    private String site;

}