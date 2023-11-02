package ru.job4j.shortcut.controller;

import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.shortcut.dto.ConversionResultDto;
import ru.job4j.shortcut.dto.UrlConversionDto;
import ru.job4j.shortcut.model.Site;
import ru.job4j.shortcut.service.SiteService;
import ru.job4j.shortcut.service.UrlService;

@RestController
@AllArgsConstructor
public class UrlController {

    private final UrlService urlService;
    private final SiteService siteService;

    @PostMapping("/convert")
    public ResponseEntity<ConversionResultDto> convert(@Valid @RequestBody UrlConversionDto urlDto) {
        Site site = siteService.findByLogin(
                SecurityContextHolder.getContext().getAuthentication().getName()).get();
        return urlService.convert(urlDto)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Url already converted or wrong url for your domain: " + site.getDomain()));
    }

}