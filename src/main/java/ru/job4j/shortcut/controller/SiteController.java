package ru.job4j.shortcut.controller;

import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.job4j.shortcut.dto.RegistrationResultDto;
import ru.job4j.shortcut.dto.SiteRegistrationDto;
import ru.job4j.shortcut.service.SiteService;

@RestController
@AllArgsConstructor
public class SiteController {

    private final SiteService siteService;

    @PostMapping("/registration")
    public ResponseEntity<RegistrationResultDto> register(@Valid @RequestBody SiteRegistrationDto regDto) {
        return siteService.save(regDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(new RegistrationResultDto(false, null, null),
                        HttpStatus.CONFLICT));
    }

}