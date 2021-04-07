package backend.realestate.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer soSao;

    @NotBlank
    private String linhVuc;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @ManyToOne
    private News news;

    public Comment() {
    }

    public Long getId() {
        return id;
    }

    public Date getCreatedDate() {
        return createdDate;
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

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSoSao() {
        return soSao;
    }

    public void setSoSao(Integer soSao) {
        this.soSao = soSao;
    }

    public String getLinhVuc() {
        return linhVuc;
    }

    public void setLinhVuc(String linhVuc) {
        this.linhVuc = linhVuc;
    }

    public Comment(Long id, Integer soSao, @NotBlank String linhVuc, News news) {
        this.id = id;
        this.soSao = soSao;
        this.linhVuc = linhVuc;
        this.news = news;
    }
}
