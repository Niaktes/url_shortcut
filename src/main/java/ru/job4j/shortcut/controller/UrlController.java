package ru.job4j.shortcut.controller;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.shortcut.dto.ConversionResultDto;
import ru.job4j.shortcut.dto.UrlConversionDto;
import ru.job4j.shortcut.model.Site;
import ru.job4j.shortcut.model.Url;
import ru.job4j.shortcut.service.SiteService;
import ru.job4j.shortcut.service.UrlService;

@RestController
@AllArgsConstructor
@Validated
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

    @GetMapping("/redirect/{code}")
    public void redirect(@Pattern(regexp = "^[a-zA-Z0-9]{8}$", message = "Code must be 8 char/number long")
                             @PathVariable
                             String code,
                         HttpServletResponse response) throws IOException {
        Optional<Url> foundUrl = urlService.findByCode(code);
        if (foundUrl.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No url associated to this code");
        }
        response.sendRedirect(foundUrl.get().getName());
    }

}