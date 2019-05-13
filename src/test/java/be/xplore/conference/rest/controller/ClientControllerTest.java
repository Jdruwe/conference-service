package be.xplore.conference.rest.controller;

import be.xplore.conference.exception.RoomAlreadyRegisteredException;
import be.xplore.conference.exception.RoomNotFoundException;
import be.xplore.conference.model.Client;
import be.xplore.conference.model.Room;
import be.xplore.conference.notifications.EmailSender;
import be.xplore.conference.rest.dto.ClientDto;
import be.xplore.conference.rest.dto.ClientHeartbeatDto;
import be.xplore.conference.rest.dto.ClientInfoDto;
import be.xplore.conference.service.ClientService;
import be.xplore.conference.service.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class ClientControllerTest {

    private static final String URL = "/api/client";
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssz");

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
    @Autowired
    private ClientController clientController;

    @MockBean
    private EmailSender emailSender;

    private ClientInfoDto clientInfoDto;
    private Client savedClient;
    private Room room;

    @Before
    public void setUp() {
        room = Room.builder()
                .id("testRoom")
                .name("Test room")
                .capacity(850)
                .setup("setup")
                .build();
        roomService.save(room);
        ZonedDateTime registeredDate = ZonedDateTime.now();
        clientInfoDto = new ClientInfoDto(room, registeredDate.toLocalDateTime());
    }

    @Test
    @WithMockUser("xploreAdmin")
    public void testRegisterClient() throws Exception {
        var dto = new RegisterClientTestDto(
                clientInfoDto.getRoom(),
                TIME_FORMATTER.format(
                        ZonedDateTime.of(clientInfoDto.getLastConnected(), ZoneId.of("Europe/Brussels"))));
        mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("testRoom")));
    }

    @Test
    @WithMockUser("xploreAdmin")
    public void testUnRegisterClient() throws Exception {
        registerClientForTesting();
        mockMvc.perform(delete(URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("id", String.valueOf(savedClient.getId())))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("1")));
    }

    @Test
    @WithMockUser("xploreAdmin")
    public void testGetAllClients() throws Exception {
        mockMvc.perform(get(URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("[]")));
    }

    @Test
    public void testUpdateHeartbeat() throws Exception {
        ZonedDateTime newDate = ZonedDateTime.now();
        registerClientForTesting();
        UpdateHeartbeatTestDto dto = new UpdateHeartbeatTestDto(savedClient.getId(), TIME_FORMATTER.format(newDate));
        mockMvc.perform(patch(URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("testRoom")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastConnected")
                        .value(newDate
                                .toLocalDateTime()
                                .minusNanos(newDate.getNano())
                                .toString()));
    }

    @Test
    public void testAuthorizationNeeded() throws Exception {
        mockMvc.perform(get(URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized());
    }

    private void registerClientForTesting() throws RoomAlreadyRegisteredException, RoomNotFoundException {
        savedClient = clientService.save(modelMapper.map(clientInfoDto, Client.class));
    }

    private class UpdateHeartbeatTestDto {
        public int clientId;
        public String newDate;

        private UpdateHeartbeatTestDto(int clientId, String newDate) {
            this.clientId = clientId;
            this.newDate = newDate;
        }
    }

    private class RegisterClientTestDto {
        public Room room;
        public String lastConnected;

        private RegisterClientTestDto(Room room, String lastConnected) {
            this.room = room;
            this.lastConnected = lastConnected;
        }
    }
}
