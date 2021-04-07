package backend.realestate.repository;

import backend.realestate.model.Category;
import backend.realestate.model.Product;
import backend.realestate.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT count(*) FROM Product")
    Long getItemCount();
}
