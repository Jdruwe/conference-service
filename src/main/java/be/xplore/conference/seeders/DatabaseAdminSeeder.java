package be.xplore.conference.seeders;

import be.xplore.conference.exception.AdminNameAlreadyExistsException;
import be.xplore.conference.exception.EmailAlreadyExistsException;
import be.xplore.conference.model.Admin;
import be.xplore.conference.service.AdminService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DatabaseAdminSeeder {
    private AdminService adminService;

    public DatabaseAdminSeeder(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostConstruct
    public void seed() throws EmailAlreadyExistsException, AdminNameAlreadyExistsException {
        seedAdminTable();
    }

    private void seedAdminTable() throws EmailAlreadyExistsException, AdminNameAlreadyExistsException {
        Admin admin = new Admin("xploreAdmin", "admin@xplore.com", "admin123!");
        adminService.register(admin);
    }
}
