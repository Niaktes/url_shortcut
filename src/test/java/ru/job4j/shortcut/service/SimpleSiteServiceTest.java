package ru.job4j.shortcut.service;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import ru.job4j.shortcut.UrlShortcutApplication;
import ru.job4j.shortcut.dto.RegistrationResultDto;
import ru.job4j.shortcut.dto.SiteRegistrationDto;
import ru.job4j.shortcut.dto.StatisticResultDto;
import ru.job4j.shortcut.model.Site;
import ru.job4j.shortcut.model.Url;
import ru.job4j.shortcut.repository.SiteRepository;
import ru.job4j.shortcut.repository.UrlRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = UrlShortcutApplication.class)
class SimpleSiteServiceTest {

    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private SimpleSiteService siteService;
    @MockBean
    private SiteRepository siteRepository;
    @MockBean
    private UrlRepository urlRepository;

    @Test
    void whenSaveSiteThenGetRegistrationResult() {
        SiteRegistrationDto regDto = new SiteRegistrationDto();
        regDto.setSite("site.ru");

        ArgumentCaptor<Site> captor = ArgumentCaptor.forClass(Site.class);
        Optional<RegistrationResultDto> result = siteService.save(regDto);

        assertThat(result).isPresent();
        verify(siteRepository).save(captor.capture());
        assertThat(captor.getValue().getDomain()).isEqualTo(regDto.getSite());
        assertThat(captor.getValue().getLogin()).isEqualTo(result.get().login());
        assertThat(encoder.matches(result.get().password(), captor.getValue().getPassword())).isTrue();
    }

    @Test
    void whenSaveWrongSiteThenGetOptionalEmpty() {
        SiteRegistrationDto regDto = new SiteRegistrationDto();
        regDto.setSite("site.ru");

        when(siteRepository.save(any(Site.class))).thenThrow(IllegalArgumentException.class);

        assertThat(siteService.save(regDto)).isEmpty();
    }

    @Test
    @WithMockUser
    void whenGetStatisticThenGetListOfStatisticResultDto() {
        Site site = new Site();
        site.setId(1);
        Url first = new Url();
        first.setName("first");
        first.setCallNumber(1);
        Url second = new Url();
        second.setName("second");
        second.setCallNumber(2);

        when(siteRepository.findByLogin(any(String.class))).thenReturn(Optional.of(site));
        when(urlRepository.getUrlsBySiteId(any(Integer.class))).thenReturn(List.of(first, second));
        List<StatisticResultDto> expected = List.of(
                new StatisticResultDto("first", 1), new StatisticResultDto("second", 2));

        assertThat(siteService.getStatistic()).isEqualTo(expected);
    }

    @Test
    void whenFindByLoginThenGetSiteOptional() {
        Site site = new Site();
        site.setId(1);

        when(siteRepository.findByLogin(any(String.class))).thenReturn(Optional.of(site));

        assertThat(siteService.findByLogin("login")).isPresent();
        assertThat(siteService.findByLogin("login")).isEqualTo(Optional.of(site));
    }

    @Test
    void whenFindByWrongLoginThenGetOptionalEmpty() {
        when(siteRepository.findByLogin(any(String.class))).thenReturn(Optional.empty());

        assertThat(siteService.findByLogin("login")).isEmpty();
    }

}