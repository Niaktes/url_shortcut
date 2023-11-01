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
import ru.job4j.shortcut.util.RandomGenerator;

@Service
@AllArgsConstructor
@Slf4j
public class SimpleSiteService implements SiteService {

    private final SiteRepository siteRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<RegistrationResultDto> save(SiteRegistrationDto regDto) {
        Optional<RegistrationResultDto> result = Optional.empty();
        try {
            RegistrationResultDto regResult = new RegistrationResultDto(
                    true,
                    RandomGenerator.generate(12),
                    RandomGenerator.generate(20)
            );

            Site site = new Site();
            site.setDomain(regDto.getSite());
            site.setLogin(regResult.login());
            site.setPassword(passwordEncoder.encode(regResult.password()));
            siteRepository.save(site);

            result = Optional.of(regResult);
        } catch (DataAccessException | IllegalArgumentException e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

}