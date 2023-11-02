package ru.job4j.shortcut.service;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.job4j.shortcut.dto.ConversionResultDto;
import ru.job4j.shortcut.dto.UrlConversionDto;
import ru.job4j.shortcut.model.Site;
import ru.job4j.shortcut.model.Url;
import ru.job4j.shortcut.repository.UrlRepository;
import ru.job4j.shortcut.util.RandomStrGenerator;

@Service
@AllArgsConstructor
@Slf4j
public class SimpleUrlService implements UrlService {

    private final UrlRepository urlRepository;
    private final SiteService siteService;

    @Override
    public Optional<ConversionResultDto> convert(UrlConversionDto urlDto) {
        Optional<ConversionResultDto> resultOptional = Optional.empty();
        try {
            String userLogin = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<Site> site = siteService.findByLogin(userLogin);
            if (site.isEmpty() || !urlDto.getUrl().contains(site.get().getDomain())) {
                return resultOptional;
            }

            ConversionResultDto result = new ConversionResultDto(RandomStrGenerator.generate(8));
            Url url = new Url();
            url.setName(urlDto.getUrl());
            url.setCode(result.code());
            url.setSite(site.get());
            urlRepository.save(url);
            resultOptional = Optional.of(result);
        } catch (DataAccessException | IllegalArgumentException e) {
            log.error(e.getMessage(), e);
        }
        return resultOptional;
    }

    @Override
    public Optional<Url> findByCode(String code) {
        Optional<Url> urlOptional = urlRepository.findByCode(code);
        urlOptional.ifPresent(url -> urlRepository.incrementCount(url.getId()));
        return urlOptional;
    }

}