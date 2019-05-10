package be.xplore.conference.service;

import be.xplore.conference.exception.AdminNameAlreadyExistsException;
import be.xplore.conference.exception.EmailAlreadyExistsException;
import be.xplore.conference.model.Admin;
import be.xplore.conference.persistence.AdminRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AdminService implements UserDetailsService {

    private final AdminRepository repo;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public Admin save(Admin admin) {
        return repo.save(admin);
    }

    @Override
    public UserDetails loadUserByUsername(String adminNameOrEmail) throws UsernameNotFoundException {
        Admin admin = repo.findByAdminNameOrEmail(adminNameOrEmail).orElseThrow(() -> new UsernameNotFoundException("No admins with that name or email were found."));
        return new User(admin.getAdminName(), admin.getPassword(), new ArrayList<>());
    }

    public Admin register(Admin admin) throws AdminNameAlreadyExistsException, EmailAlreadyExistsException {
        if (repo.existsAdminByAdminName(admin.getAdminName())) {
            throw new AdminNameAlreadyExistsException("A admin with this adminName already exists.");
        }
        if (repo.existsAdminByEmail(admin.getEmail())) {
            throw new EmailAlreadyExistsException("A admin with this email already exists.");
        }
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return repo.save(admin);
    }

    public Admin loadAdminThatHasPassword(String adminNameOrEmail) {
        Admin admin = loadAdminByAdminNameOrEmail(adminNameOrEmail);
        if (admin != null) {
            return admin;
        }
        throw new UsernameNotFoundException("No admin with that name or email were found.");
    }

    public Admin loadAdminByAdminNameOrEmail(String adminNameOrEmail) throws UsernameNotFoundException {
        return repo.findByAdminNameOrEmail(adminNameOrEmail).orElseThrow(() -> new UsernameNotFoundException("No admin with that name or email were found."));
    }

    public List<Admin> loadAllAdmins() {
        return repo.findAll();
    }
}
