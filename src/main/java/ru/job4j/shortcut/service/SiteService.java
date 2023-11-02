package ru.job4j.shortcut.service;

import java.util.List;
import java.util.Optional;
import ru.job4j.shortcut.dto.RegistrationResultDto;
import ru.job4j.shortcut.dto.SiteRegistrationDto;
import ru.job4j.shortcut.dto.StatisticResultDto;
import ru.job4j.shortcut.model.Site;

public interface SiteService {

    Optional<RegistrationResultDto> save(SiteRegistrationDto regDto);

    List<StatisticResultDto> getStatistic();

    Optional<Site> findByLogin(String login);

}