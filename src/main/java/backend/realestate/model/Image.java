package backend.realestate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    @Lob
    @Column(name = "image", length = 1004857)
    private String image;

    @NotBlank(message = "Thông tin không được bỏ trống")
    private String dinhDang;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "rootImage",
            cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.SUBSELECT)
    @JsonIgnoreProperties("hotPotList")
    private Set<HotPot> hotPotList;

    @OneToMany(mappedBy = "linkedImage",
            cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.SUBSELECT)
    @JsonIgnore
    private Set<HotPot> hotPotLinked;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    public Image() {
    }

    public Set<HotPot> getHotPotLinked() {
        return hotPotLinked;
    }

    public void setHotPotLinked(Set<HotPot> hotPotLinked) {
        this.hotPotLinked = hotPotLinked;
    }

    public Set<HotPot> getHotPotList() {
        return hotPotList;
    }

    public void setHotPotList(Set<HotPot> hotPotList) {
        this.hotPotList = hotPotList;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDinhDang() {
        return dinhDang;
    }

    public void setDinhDang(String dinhDang) {
        this.dinhDang = dinhDang;
    }

    @JsonIgnoreProperties(value = {"images", "project", "image",})
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String idAsString() {
        return id != null ? "" + id : null;
    }

    public Image(Long id, @NotBlank(message = "Ảnh không được bỏ trống") String image, @NotBlank(message = "Thông tin không được bỏ trống") Long idSanPham, @NotBlank(message = "Thông tin không được bỏ trống") String dinhDang, Product product) {
        this.id = id;
        this.image = image;
        this.dinhDang = dinhDang;
        this.product = product;
    }
}
