package LDHD.project.domain.user.repository;

import LDHD.project.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    Boolean existsByEmail(String email);

    boolean existsByLoginId(String loginId);
}
