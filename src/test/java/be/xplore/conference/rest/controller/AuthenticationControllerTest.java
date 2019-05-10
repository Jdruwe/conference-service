package be.xplore.conference.rest.controller;

import be.xplore.conference.rest.dto.LoginDto;
import be.xplore.conference.rest.dto.RegisterDto;
import be.xplore.conference.rest.dto.TokenDto;
import be.xplore.conference.rest.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private String convertedRegisterDTO;
    private String convertedLoginWithNameDTO;
    private String convertedLoginWithEmailDTO;

    @Before
    public void setUp() throws Exception {
        RegisterDto registerDto = new RegisterDto("Pieter-Jan", "pieterjan.noe@student.kdg.be", "password123");
        convertedRegisterDTO = objectMapper.writeValueAsString(registerDto);
        LoginDto loginWithNameDTO = new LoginDto(registerDto.getAdminName(), registerDto.getPassword());
        convertedLoginWithNameDTO = objectMapper.writeValueAsString(loginWithNameDTO);
        LoginDto loginWithEmailDTO = new LoginDto(registerDto.getEmail(), registerDto.getPassword());
        convertedLoginWithEmailDTO = objectMapper.writeValueAsString(loginWithEmailDTO);
    }

    @Test
    @WithMockUser("xploreAdmin")
    public void testRegisterAndLogIn() throws Exception {
        register();
        TokenDto loginWithNameTokenDTO = objectMapper.readValue(logIn(convertedLoginWithNameDTO, HttpStatus.OK), TokenDto.class);
        TokenDto loginWithEmailTokenDTO = objectMapper.readValue(logIn(convertedLoginWithEmailDTO, HttpStatus.OK), TokenDto.class);
        Assert.assertTrue(jwtTokenProvider.validateToken(loginWithNameTokenDTO.getToken()));
        Assert.assertTrue(jwtTokenProvider.validateToken(loginWithEmailTokenDTO.getToken()));
    }

    private void register() throws Exception {
        mockMvc.perform(post("/api/authentication/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(convertedRegisterDTO))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("Pieter-Jan")));
    }

    private String logIn(String jsonString, HttpStatus expectedStatus) throws Exception {
        if (expectedStatus.equals(HttpStatus.OK)) {
            return mockMvc.perform(post("/api/authentication/login")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(jsonString))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
        } else if (expectedStatus.equals(HttpStatus.UNAUTHORIZED)) {
            return mockMvc.perform(post("/api/authentication/login")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(jsonString))
                    .andExpect(status().isUnauthorized())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
        } else if (expectedStatus.equals(HttpStatus.FORBIDDEN)) {
            return mockMvc.perform(post("/api/authentication/login")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(jsonString))
                    .andExpect(status().isForbidden())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
        } else
            throw new HttpServerErrorException(expectedStatus);
    }
}
