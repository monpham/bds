package backend.realestate.repository;

import backend.realestate.model.Category;
import backend.realestate.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    @Query("SELECT COUNT(*) FROM News")
    Long getItemCount();


    @Query(value = "SELECT COUNT(n.id) AS count, n.createdDate AS date FROM News n GROUP BY n.createdDate")
    List<Map<String, Object>> countAllByCreatedDate();
}
