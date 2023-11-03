package ru.job4j.shortcut.controller;

import java.util.List;
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
import ru.job4j.shortcut.dto.RegistrationResultDto;
import ru.job4j.shortcut.dto.SiteRegistrationDto;
import ru.job4j.shortcut.dto.StatisticResultDto;
import ru.job4j.shortcut.service.SiteService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = UrlShortcutApplication.class)
@AutoConfigureMockMvc
class SiteControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SiteService siteService;

    private final Gson gson = new GsonBuilder().create();

    @Test
    void whenPostRegistrationThenResponseOkWithRegistrationResult() throws Exception {
        SiteRegistrationDto regDto = new SiteRegistrationDto();
        regDto.setSite("job4j.ru");
        RegistrationResultDto regResult = new RegistrationResultDto(true, "testLogin", "testPass");

        when(siteService.save(regDto)).thenReturn(Optional.of(regResult));

        MvcResult mvcResult = mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(regDto)))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(mvcResult.getResponse().getContentType()).isEqualTo("application/json");
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo(gson.toJson(regResult));
    }

    @Test
    void whenPostRegistrationWrongSiteThenResponseConflictWithRegistrationResultFalse() throws Exception {
        RegistrationResultDto regResult = new RegistrationResultDto(false, null, null);

        when(siteService.save(any(SiteRegistrationDto.class))).thenReturn(Optional.empty());

        MvcResult mvcResult = mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(new SiteRegistrationDto())))
                .andExpect(status().isConflict())
                .andReturn();
        assertThat(mvcResult.getResponse().getContentType()).isEqualTo("application/json");
        assertThat(mvcResult.getResponse().getContentAsString())
                .isEqualTo("{\"registration\":false,\"login\":null,\"password\":null}");
    }

    @Test
    @WithMockUser
    void whenGetStatisticThenResponseOkWithStatisticListJson() throws Exception {
        StatisticResultDto first = new StatisticResultDto("first", 1);
        StatisticResultDto second = new StatisticResultDto("second", 2);

        when(siteService.getStatistic()).thenReturn(List.of(first, second));

        MvcResult mvcResult = mockMvc.perform(get("/statistic"))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(mvcResult.getResponse().getContentType()).isEqualTo("application/json");
        assertThat(mvcResult.getResponse().getContentAsString())
                .isEqualTo(gson.toJson(List.of(first, second)));
    }

}