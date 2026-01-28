package LDHD.project.domain.user.repository;

import LDHD.project.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String username);
    Boolean existsByEmail(String email);
}
