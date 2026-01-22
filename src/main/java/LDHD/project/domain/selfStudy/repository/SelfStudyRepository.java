package LDHD.project.domain.selfStudy.repository;

import LDHD.project.domain.selfStudy.SelfStudy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SelfStudyRepository extends JpaRepository<SelfStudy,Long> {
    Optional<SelfStudy>findByIdAndUserId(Long selfStudyId, Long userId);
    boolean existsByUserId(Long userId);
    Page<SelfStudy> findAllByUserId(Long userId, Pageable pageable);
}
