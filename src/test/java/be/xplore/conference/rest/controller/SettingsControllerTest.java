package be.xplore.conference.rest.controller;

import be.xplore.conference.rest.dto.MainSettingsDto;
import be.xplore.conference.rest.dto.NotificationsDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SettingsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser("xploreAdmin")
    public void testGetSettings() throws Exception {
        mockMvc.perform(get("/api/settings")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("5")))
                .andExpect(content().string(containsString("false")))
                .andExpect(content().string(containsString("false")))
                .andExpect(content().string(containsString("")));
    }

    @Test
    @WithMockUser("xploreAdmin")
    public void testChangeSettings() throws Exception {
        var dto = new MainSettingsDto(7,30, false);
        mockMvc.perform(put("/api/settings")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("7")))
                .andExpect(content().string(containsString("false")));
    }

    @Test
    @WithMockUser("xploreAdmin")
    public void testChangeNotificationSettings() throws Exception {
        var dto = new NotificationsDto(true, "This is a test message");
        mockMvc.perform(put("/api/settings/notifications")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("true")))
                .andExpect(content().string(containsString("This is a test message")));
    }
}
