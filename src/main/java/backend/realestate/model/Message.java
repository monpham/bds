package backend.realestate.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "from_user_id")
    @JsonIgnoreProperties(value = {"message", "email", "password", "roles"})
    private User fromUser;
    @ManyToOne
    @JoinColumn(name = "to_user_id")
    @JsonIgnoreProperties(value = {"message", "email", "password", "roles"})
    private User toUser;
    private String content;
    private boolean isUserRead;

    public Message() {
    }

    public boolean isUserRead() {
        return isUserRead;
    }

    public void setUserRead(boolean userRead) {
        isUserRead = userRead;
    }

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    public Message(User fromUser, User toUser, String content, boolean isUserRead, Date createdDate) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.content = content;
        this.isUserRead = isUserRead;
        this.createdDate = createdDate;
    }

    public Message(User fromUser, User toUser, String content, boolean isUserRead) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.content = content;
        this.isUserRead = isUserRead;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
