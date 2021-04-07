package backend.realestate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(min = 3, max = 100, message = "Title phải lớn hơn 3 và bé hơn 100")
    @NotBlank(message = "Thông tin không được để trống")
    private String title;

    private Integer views;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnoreProperties("comments")
    private List<Comment> comments;
    @Lob
    private String content;
    @Lob
    private String description;
    @Lob
    @Column(name = "image", columnDefinition="LONGBLOB")
    private String image;

    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    private Date createdDate;

    @UpdateTimestamp
    @Temporal(TemporalType.DATE)
    private Date updatedDate;
    @Column
    private boolean active;
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public News() {
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String idAsString() {
        return id != null ? "" + id : null;
    }
}
