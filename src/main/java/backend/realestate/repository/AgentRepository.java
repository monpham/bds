package backend.realestate.repository;

import backend.realestate.model.Agent;
import backend.realestate.model.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {
    @Query("SELECT count(*) FROM Agent ")
    Long getItemCount();
}
