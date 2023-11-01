package ru.job4j.shortcut.service;

import java.util.Optional;
import ru.job4j.shortcut.dto.RegistrationResultDto;
import ru.job4j.shortcut.dto.SiteRegistrationDto;

public interface SiteService {

    Optional<RegistrationResultDto> save(SiteRegistrationDto regDto);

}