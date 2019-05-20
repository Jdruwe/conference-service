package be.xplore.conference.persistence;

import be.xplore.conference.model.auth.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface AdminRepository extends JpaRepository<Admin, Long> {
    @Query("SELECT a FROM Admin a WHERE a.adminName = ?1 OR a.email = ?1")
    Optional<Admin> findByAdminNameOrEmail(String adminNameOrEmail);

    boolean existsAdminByAdminName(String adminName);

    boolean existsAdminByEmail(String email);

}
