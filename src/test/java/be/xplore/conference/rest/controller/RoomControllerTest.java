package be.xplore.conference.rest.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void testGetScheduleForRoom() throws Exception {
        mockMvc.perform(get("/api/room")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Room8")))
                .andExpect(content().string(containsString("Room5")))
                .andExpect(content().string(containsString("ExhibitionHall")))
                .andExpect(content().string(containsString("Room3")))
                .andExpect(content().string(containsString("Room9")))
                .andExpect(content().string(containsString("BOF2")))
                .andExpect(content().string(containsString("Room6")))
                .andExpect(content().string(containsString("Room10")))
                .andExpect(content().string(containsString("Room7")))
                .andExpect(content().string(containsString("BOF1")))
                .andExpect(content().string(containsString("Room4")));

    }

}
