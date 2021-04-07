package backend.realestate.repository;

import backend.realestate.model.Message;
import backend.realestate.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT count(*) FROM Message")
    Long getItemCount();

    List<Message> getAllByToUserAndFromUserOrderByCreatedDate(User toUser, User fromUser);
}
