package be.xplore.conference.service;

import be.xplore.conference.excpetion.AdminNameAlreadyExistsException;
import be.xplore.conference.excpetion.EmailAlreadyExistsException;
import be.xplore.conference.model.Admin;
import be.xplore.conference.persistence.AdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional
public class AdminService implements UserDetailsService {

    private AdminRepository repo;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public Admin save(Admin admin) {
        return repo.save(admin);
    }


    @Override
    public UserDetails loadUserByUsername(String AdminNameOrEmail) throws UsernameNotFoundException {
        Admin admin = repo.findByAdminNameOrEmail(AdminNameOrEmail).orElseThrow(() -> new UsernameNotFoundException("No Admins with that name or email were found."));
        return new User(admin.getAdminName(), admin.getPassword(), new ArrayList<>());
    }

    public Admin register(Admin admin) throws AdminNameAlreadyExistsException, EmailAlreadyExistsException {
        if (repo.existsAdminByAdminName(admin.getAdminName()))
            throw new AdminNameAlreadyExistsException("A player with this playerName already exists.");
        if (repo.existsAdminByEmail(admin.getEmail()))
            throw new EmailAlreadyExistsException("A player with this email already exists.");
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return repo.save(admin);
    }
}
