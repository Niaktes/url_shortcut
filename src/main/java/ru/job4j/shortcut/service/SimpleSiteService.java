package ru.job4j.shortcut.service;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.job4j.shortcut.dto.RegistrationResultDto;
import ru.job4j.shortcut.dto.SiteRegistrationDto;
import ru.job4j.shortcut.model.Site;
import ru.job4j.shortcut.repository.SiteRepository;
import ru.job4j.shortcut.util.RandomStrGenerator;

@Service
@AllArgsConstructor
@Slf4j
public class SimpleSiteService implements SiteService {

    private final SiteRepository siteRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<RegistrationResultDto> save(SiteRegistrationDto regDto) {
        Optional<RegistrationResultDto> resultOptional = Optional.empty();
        try {
            RegistrationResultDto result = new RegistrationResultDto(
                    true,
                    RandomStrGenerator.generate(12),
                    RandomStrGenerator.generate(20)
            );

            Site site = new Site();
            site.setDomain(regDto.getSite());
            site.setLogin(result.login());
            site.setPassword(passwordEncoder.encode(result.password()));
            siteRepository.save(site);
            resultOptional = Optional.of(result);
        } catch (DataAccessException | IllegalArgumentException e) {
            log.error(e.getMessage(), e);
        }
        return resultOptional;
    }

    @Override
    public Optional<Site> findByLogin(String login) {
        return siteRepository.findByLogin(login);
    }

}