package backend.realestate.repository;

import backend.realestate.model.News;
import backend.realestate.model.Role;
import backend.realestate.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    List<User> getAllByRoles(Role role);

    @Query("SELECT c FROM User c WHERE c.email = ?1")
    public User findByEmail(String email);
     @Query("SELECT c FROM User c WHERE c.resetPasswordToken= ?1")
    public User findByResetPasswordToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE User c SET c.resetPasswordToken = ?2 WHERE c.id = ?1")
    public void saveToken(Long id, String token);
}
