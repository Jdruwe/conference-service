package be.xplore.conference.rest.jwt;

import be.xplore.conference.model.Admin;
import be.xplore.conference.service.AdminService;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test(expected = MalformedJwtException.class)
    public void testGetAdminNameFromTokenWithWrongToken() {
        jwtTokenProvider.getAdminNameFromToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9");
    }

    @Test
    public void testGetAdminNameFromToken() {
        String adminNameFromToken = jwtTokenProvider.getAdminNameFromToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ4cGxvcmVBZG1pbiIsImlhdCI6MTU1NzQ5MDU4MSwiZXhwIjoxNTU4MzMwNTgxfQ.H5UeHbQFDNsY70tm0B4l96A7qFnvDpZd3T8QGXC26rbWnMRNCQ4LvYObUJoaAFhyfH3QY8a_d_SB5on_41t7Pg");
        Assert.assertEquals(adminNameFromToken,"xploreAdmin");
    }
}
