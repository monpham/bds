package backend.realestate.repository;

import backend.realestate.model.HotPot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface HotPotRepository extends JpaRepository<HotPot, Long> {
    @Transactional
    @Modifying
    @Query("delete from HotPot h where h.rootImage.id = ?1 and h.id not in ?2")
    void deleteDoesNotExist(Long idImage, List<Long> id);
}
