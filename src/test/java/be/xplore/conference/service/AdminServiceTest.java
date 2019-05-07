package be.xplore.conference.service;

import be.xplore.conference.exception.AdminNameAlreadyExistsException;
import be.xplore.conference.exception.EmailAlreadyExistsException;
import be.xplore.conference.model.Admin;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class AdminServiceTest {

    private static final String ADMIN_NAME = "xploreAdmin";

    @Autowired
    private AdminService service;

    @Test
    public void testSave() {
        Admin admin = new Admin("testAdmin", "TestAdmin@test.com", "test123");
        Admin savedAdmin = service.save(admin);
        assertEquals(savedAdmin.getAdminName(), admin.getAdminName());
        assertEquals(savedAdmin.getEmail(), admin.getEmail());
        assertEquals(savedAdmin.getPassword(), admin.getPassword());
        assertTrue(savedAdmin.getId() > 0);
    }

    @Test
    public void testLoadUserByUsername() {
        Admin admin = service.loadAdminByAdminNameOrEmail(ADMIN_NAME);
        assertNotNull(admin);
        assertEquals(admin.getAdminName(), ADMIN_NAME);
    }

    @Test(expected = AdminNameAlreadyExistsException.class)
    public void testRegisterExistingAdminWithAdminNameThrowsException() throws EmailAlreadyExistsException, AdminNameAlreadyExistsException {
        Admin admin = service.loadAdminByAdminNameOrEmail(ADMIN_NAME);
        assertNotNull(admin);
        service.register(admin);
    }

    @Test(expected = EmailAlreadyExistsException.class)
    public void testRegisterExistingAdminWithEmailThrowsException() throws EmailAlreadyExistsException, AdminNameAlreadyExistsException {
        Admin admin = new Admin("empty", "test.admin@xplore.com", "empty");
        service.register(admin);
        Admin adminWithSameEmail = new Admin("empty2", "test.admin@xplore.com", "empty");
        service.register(adminWithSameEmail);
    }

    @Test
    public void testRegister() {
        Admin admin = new Admin("new", "new", "new");
        Admin registeredAdmin = service.save(admin);
        assertNotNull(registeredAdmin);
        assertEquals(admin, registeredAdmin);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadAdminThatHasPasswordThrowsException() {
        service.loadAdminThatHasPassword("");
    }

    //TODO: look at this test
/*    @Test(expected = UsernameNotFoundException.class)
    public void testLoadAdminThatHasPasswordThrowsExceptionWithMessage() {
        try {
            service.loadAdminThatHasPassword("");
        } catch (UsernameNotFoundException e) {
            assertEquals("No admin with that name or email were found", e.);
            throw e;
        }
    }*/

    @Test()
    public void testLoadAdminThatHasPassword() {
        Admin admin = service.loadAdminThatHasPassword(ADMIN_NAME);
        assertNotNull(admin);
        assertEquals(admin.getAdminName(), ADMIN_NAME);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadAdminByAdminNameOrEmailThrowsException() {
        service.loadAdminByAdminNameOrEmail("");
    }

    @Test()
    public void testLoadAdminByAdminNameOrEmail() {
        Admin admin = service.loadAdminByAdminNameOrEmail(ADMIN_NAME);
        assertNotNull(admin);
        assertEquals(admin.getAdminName(), ADMIN_NAME);
    }

    @Test
    public void testLoadAllAdmins(){
        List<Admin> allAdmins = service.loadAllAdmins();
        assertNotNull(allAdmins);
        assertEquals(1,allAdmins.size());
        assertEquals(ADMIN_NAME,allAdmins.get(0).getAdminName());
    }
}
