package be.xplore.conference.rest.controller;

import be.xplore.conference.excpetion.RoomAlreadyRegisteredException;
import be.xplore.conference.excpetion.RoomNotFoundException;
import be.xplore.conference.model.Client;
import be.xplore.conference.model.Room;
import be.xplore.conference.rest.dto.ClientDto;
import be.xplore.conference.rest.dto.ClientHeartbeatDto;
import be.xplore.conference.service.ClientService;
import be.xplore.conference.service.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RoomService roomService;

    @Autowired
    private ClientService clientService;

    private Room room;
    private ClientDto clientDto;

    @Before
    public void setUp() {
        room = new Room("testRoom", "Test room", 850, "setup");
        roomService.save(room);
        Date registeredDate = new Date();
        clientDto = new ClientDto(room, registeredDate);

    }

    @Test
    @WithMockUser("xploreAdmin")
    public void testRegisterClient() throws Exception {
        mockMvc.perform(post("/api/client")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(clientDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("testRoom")));
    }

    @Test
    @WithMockUser("xploreAdmin")
    public void testUnRegisterClient() throws Exception {
        registerClientForTesting();
        mockMvc.perform(delete("/api/client")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("id", clientDto.getRoom().getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("1")));
    }

    @Test
    @WithMockUser("xploreAdmin")
    public void testGetAllClients() throws Exception {
        mockMvc.perform(get("/api/client")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect((content().string(containsString("[]"))));
    }

    @Test
    public void testUpdateHeartbeat() throws Exception {
        Date newDate = new Date();
        registerClientForTesting();
        ClientHeartbeatDto clientHeartbeatDto = new ClientHeartbeatDto(clientDto.getRoom().getId(), newDate);
        mockMvc.perform(patch("/api/client")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(clientHeartbeatDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("testRoom")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastConnected").value(newDate));
    }

    @Test
    public void testAuthorizationNeeded() throws Exception {
        mockMvc.perform(get("/api/client")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized());
    }

    private void registerClientForTesting() throws RoomAlreadyRegisteredException, RoomNotFoundException {
        clientService.save(modelMapper.map(clientDto, Client.class));
    }
}
