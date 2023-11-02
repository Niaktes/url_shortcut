package ru.job4j.shortcut.service;

import java.util.Optional;
import ru.job4j.shortcut.dto.ConversionResultDto;
import ru.job4j.shortcut.dto.UrlConversionDto;

public interface UrlService {

    Optional<ConversionResultDto> convert(UrlConversionDto urlDto);

}