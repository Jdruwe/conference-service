package be.xplore.conference.service;

import be.xplore.conference.exception.AdminNameAlreadyExistsException;
import be.xplore.conference.exception.EmailAlreadyExistsException;
import be.xplore.conference.model.Admin;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        Assert.assertEquals(savedAdmin.getAdminName(), admin.getAdminName());
        Assert.assertEquals(savedAdmin.getEmail(), admin.getEmail());
        Assert.assertEquals(savedAdmin.getPassword(), admin.getPassword());
        Assert.assertTrue(savedAdmin.getId() > 0);
    }

    @Test
    public void testLoadUserByUsername() {
        Admin admin = service.loadAdminByAdminNameOrEmail(ADMIN_NAME);
        Assert.assertNotNull(admin);
        Assert.assertEquals(admin.getAdminName(), ADMIN_NAME);
    }

    @Test(expected = AdminNameAlreadyExistsException.class)
    public void testRegisterExistingAdminWithAdminNameThrowsException() throws EmailAlreadyExistsException, AdminNameAlreadyExistsException {
        Admin admin = service.loadAdminByAdminNameOrEmail(ADMIN_NAME);
        Assert.assertNotNull(admin);
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
        Assert.assertNotNull(registeredAdmin);
        Assert.assertEquals(admin, registeredAdmin);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadAdminThatHasPasswordThrowsException() {
        service.loadAdminThatHasPassword("");
    }

    @Test()
    public void testLoadAdminThatHasPassword() {
        Admin admin = service.loadAdminThatHasPassword(ADMIN_NAME);
        Assert.assertNotNull(admin);
        Assert.assertEquals(admin.getAdminName(), ADMIN_NAME);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadAdminByAdminNameOrEmailThrowsException() {
        service.loadAdminByAdminNameOrEmail("");
    }

    @Test()
    public void testLoadAdminByAdminNameOrEmail() {
        Admin admin = service.loadAdminByAdminNameOrEmail(ADMIN_NAME);
        Assert.assertNotNull(admin);
        Assert.assertEquals(admin.getAdminName(), ADMIN_NAME);
    }

    @Test
    public void testLoadAllAdmins(){
        List<Admin> allAdmins = service.loadAllAdmins();
        Assert.assertNotNull(allAdmins);
        Assert.assertEquals(1,allAdmins.size());
        Assert.assertEquals(ADMIN_NAME,allAdmins.get(0).getAdminName());
    }
}
