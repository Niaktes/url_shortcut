package ru.job4j.shortcut.service;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import ru.job4j.shortcut.UrlShortcutApplication;
import ru.job4j.shortcut.dto.ConversionResultDto;
import ru.job4j.shortcut.dto.UrlConversionDto;
import ru.job4j.shortcut.model.Site;
import ru.job4j.shortcut.model.Url;
import ru.job4j.shortcut.repository.SiteRepository;
import ru.job4j.shortcut.repository.UrlRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = UrlShortcutApplication.class)
class SimpleUrlServiceTest {

    @Autowired
    private SimpleUrlService urlService;
    @MockBean
    private SiteRepository siteRepository;
    @MockBean
    private UrlRepository urlRepository;

    @Test
    @WithMockUser
    void whenConvertUrlThenGetConversionResult() {
        UrlConversionDto urlDto = new UrlConversionDto();
        urlDto.setUrl("http://job4j.ru/exercise");
        Site site = new Site();
        site.setDomain("job4j.ru");

        when(siteRepository.findByLogin(any(String.class))).thenReturn(Optional.of(site));
        ArgumentCaptor<Url> captor = ArgumentCaptor.forClass(Url.class);
        Optional<ConversionResultDto> result = urlService.convert(urlDto);

        assertThat(result).isPresent();
        verify(urlRepository).save(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo(urlDto.getUrl());
        assertThat(captor.getValue().getSite()).isEqualTo(site);
        assertThat(captor.getValue().getCode()).isEqualTo(result.get().code());
    }

    @Test
    @WithMockUser
    void whenConvertUrlWithWrongDomainThenGetOptionalEmpty() {
        UrlConversionDto urlDto = new UrlConversionDto();
        urlDto.setUrl("http://job4j.com/exercise");
        Site site = new Site();
        site.setDomain("job4j.ru");

        when(siteRepository.findByLogin(any(String.class))).thenReturn(Optional.of(site));

        assertThat(urlService.convert(urlDto)).isEmpty();
        verify(urlRepository, times(0)).save(any(Url.class));
    }

    @Test
    @WithMockUser
    void whenConvertUrlWithoutSiteThenGetOptionalEmpty() {
        UrlConversionDto urlDto = new UrlConversionDto();
        urlDto.setUrl("http://job4j.ru/exercise");

        when(siteRepository.findByLogin(any(String.class))).thenReturn(Optional.empty());

        assertThat(urlService.convert(urlDto)).isEmpty();
        verify(urlRepository, times(0)).save(any(Url.class));
    }

    @Test
    @WithMockUser
    void whenConvertWrongOrExistedUrlThenGetOptionalEmpty() {
        UrlConversionDto urlDto = new UrlConversionDto();
        urlDto.setUrl("http://job4j.ru/exercise");
        Site site = new Site();
        site.setDomain("job4j.ru");

        when(siteRepository.findByLogin(any(String.class))).thenReturn(Optional.of(site));
        when(urlRepository.save(any(Url.class))).thenThrow(IllegalArgumentException.class);

        assertThat(urlService.convert(urlDto)).isEmpty();
    }

    @Test
    void whenFindByCodeThenGetUrlOptionalAndIncrementCount() {
        Url url = new Url();
        url.setId(1);

        when(urlRepository.findByCode(any(String.class))).thenReturn(Optional.of(url));
        Optional<Url> actual = urlService.findByCode("code");

        assertThat(actual).isPresent().isEqualTo(Optional.of(url));
        verify(urlRepository).incrementCount(any(Integer.class));
    }

    @Test
    void whenFindByWrongCodeThenGetOptionalEmptyAndNoIncrementCount() {
        when(urlRepository.findByCode(any(String.class))).thenReturn(Optional.empty());

        assertThat(urlService.findByCode("code")).isEmpty();
        verify(urlRepository, times(0)).incrementCount(any(Integer.class));
    }

}