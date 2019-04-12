package be.xplore.conference.service;

import be.xplore.conference.excpetion.AdminNameAlreadyExistsException;
import be.xplore.conference.excpetion.EmailAlreadyExistsException;
import be.xplore.conference.model.Admin;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AdminServiceTest {

    @Autowired
    private AdminService service;

    @Test
    public void testSave() {
        Admin admin = new Admin("testAdmin","TestAdmin@test.com","test123");
        Admin savedAdmin = service.save(admin);
        assertEquals(savedAdmin.getAdminName(),admin.getAdminName());
        assertEquals(savedAdmin.getEmail(),admin.getEmail());
        assertEquals(savedAdmin.getPassword(),admin.getPassword());
        assertTrue(savedAdmin.getId() > 0);
    }

    @Test
    public void testLoadUserByUsername() {
        Admin admin = service.loadAdminByAdminNameOrEmail("xploreAdmin");
        assertNotNull(admin);
        assertEquals(admin.getAdminName(),"xploreAdmin");
    }

    @Test(expected = AdminNameAlreadyExistsException.class)
    public void testRegisterExistingAdminWithAdminNameThrowsException() throws EmailAlreadyExistsException, AdminNameAlreadyExistsException {
        Admin admin = service.loadAdminByAdminNameOrEmail("xploreAdmin");
        assertNotNull(admin);
        service.register(admin);
    }

    @Test(expected = EmailAlreadyExistsException.class)
    public void testRegisterExistingAdminWithEmailThrowsException() throws EmailAlreadyExistsException, AdminNameAlreadyExistsException {
        Admin admin = new Admin("empty", "admin@xplore.com","empty");
        service.register(admin);
    }

    @Test
    public void testRegister() {
        Admin admin = new Admin("new","new","new");
        Admin registeredAdmin = service.save(admin);
        assertNotNull(registeredAdmin);
        assertEquals(admin,registeredAdmin);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadAdminThatHasPasswordThrowsException() {
        service.loadAdminThatHasPassword("");
    }

    @Test()
    public void testLoadAdminThatHasPassword() {
        Admin admin = service.loadAdminThatHasPassword("xploreAdmin");
        assertNotNull(admin);
        assertEquals(admin.getAdminName(),"xploreAdmin");
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadAdminByAdminNameOrEmailThrowsException() {
        service.loadAdminByAdminNameOrEmail("");
    }

    @Test()
    public void testLoadAdminByAdminNameOrEmail() {
        Admin admin = service.loadAdminByAdminNameOrEmail("xploreAdmin");
        assertNotNull(admin);
        assertEquals(admin.getAdminName(),"xploreAdmin");
    }
}
