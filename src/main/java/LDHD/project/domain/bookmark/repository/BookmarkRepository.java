package LDHD.project.domain.bookmark.repository;

import LDHD.project.domain.bookmark.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    // 즐겨찾기 중복 검사(Service계층)에서 사용
    Optional<Bookmark> findByUser_IdAndSelfStudy_Id(Long user_id, Long selfStudy_id);
}
