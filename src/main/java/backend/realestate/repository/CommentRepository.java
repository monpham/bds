package backend.realestate.repository;

import backend.realestate.model.Category;
import backend.realestate.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT count(*) FROM Product ")
    Long getItemCount();
}
