package ru.job4j.shortcut.controller;

import java.util.Optional;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.job4j.shortcut.UrlShortcutApplication;
import ru.job4j.shortcut.dto.ConversionResultDto;
import ru.job4j.shortcut.dto.UrlConversionDto;
import ru.job4j.shortcut.model.Site;
import ru.job4j.shortcut.model.Url;
import ru.job4j.shortcut.service.SiteService;
import ru.job4j.shortcut.service.UrlService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = UrlShortcutApplication.class)
@AutoConfigureMockMvc
class UrlControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SiteService siteService;
    @MockBean
    private UrlService urlService;

    private final Gson gson = new GsonBuilder().create();

    @Test
    @WithMockUser
    void whenPostConvertThenResponseOkWithConversionCode() throws Exception {
        Site site = new Site();
        site.setDomain("domain");
        UrlConversionDto urlDto = new UrlConversionDto();
        urlDto.setUrl("http://test.url");
        ConversionResultDto conversionResult = new ConversionResultDto("code");

        when(urlService.convert(any(UrlConversionDto.class))).thenReturn(Optional.of(conversionResult));
        when(siteService.findByLogin(any(String.class))).thenReturn(Optional.of(site));

        MvcResult mvcResult = mockMvc.perform(post("/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(urlDto)))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(mvcResult.getResponse().getContentType()).isEqualTo("application/json");
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo(gson.toJson(conversionResult));
    }

    @Test
    @WithMockUser
    void whenPostConvertWithWrongUrlThenResponseBadRequest() throws Exception {
        Site site = new Site();
        site.setDomain("domain");
        UrlConversionDto urlDto = new UrlConversionDto();
        urlDto.setUrl("http://test.url");
        String exceptionString = "Url already converted or wrong url for your domain: " + site.getDomain();

        when(urlService.convert(any(UrlConversionDto.class))).thenReturn(Optional.empty());
        when(siteService.findByLogin(any(String.class))).thenReturn(Optional.of(site));

        MvcResult mvcResult = mockMvc.perform(post("/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(urlDto)))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertThat(mvcResult.getResponse().getErrorMessage()).isEqualTo(exceptionString);
    }

    @Test
    void whenGetRedirectThenGetRedirectedToAssociatedPage() throws Exception {
        Url url = new Url();
        url.setName("name.ru");

        when(urlService.findByCode(any(String.class))).thenReturn(Optional.of(url));

        this.mockMvc.perform(get("/redirect/{code}", "8charcod"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(url.getName()));
    }

    @Test
    void whenGetRedirectWithWrongCodeThenGetRedirectedToAssociatedPage() throws Exception {
        when(urlService.findByCode(any(String.class))).thenReturn(Optional.empty());

        MvcResult mvcResult = mockMvc.perform(get("/redirect/{code}", "wroncode"))
                .andExpect(status().isNotFound())
                .andReturn();
        assertThat(mvcResult.getResponse().getErrorMessage()).isEqualTo("No url associated to this code");
    }

}