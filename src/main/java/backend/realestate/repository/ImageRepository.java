package backend.realestate.repository;

import backend.realestate.model.Category;
import backend.realestate.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> getAllByProduct_IdAndAndDinhDang(Long id, String DinhDang);
    @Query("SELECT count(*) FROM Product ")
    Long getItemCount();
}
