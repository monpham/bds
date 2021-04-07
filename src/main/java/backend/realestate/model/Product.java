package backend.realestate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(min = 3, max = 50, message = "Tên phải lớn hơn 3 và bé hơn 50")
    @NotBlank(message = "Thông tin không được bỏ trống")
    private String tenSanPham;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonIgnoreProperties("product")
    private Project project;
    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonIgnoreProperties("product")
    private Category category;


    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    private Date createdDate;

    @UpdateTimestamp
    @Temporal(TemporalType.DATE)
    private Date updatedDate;

    @Size(max = 200, message = "Địa chỉ phải trong vòng 200 ký tự")
    @NotBlank(message = "Thông tin không được bỏ trống")
    private String diaChi;

    @NotBlank(message = "Thông tin không được bỏ trống")
    private Double dienTich;

    @NotBlank(message = "Thông tin không được bỏ trống")
    private Double giaTien;

    @Lob
    @Column(name = "mo_ta", length = 2000)
    private String moTa;

    @DateTimeFormat(pattern = "MM-dd-yyyy")
    @NotBlank(message = "Thông tin không được bỏ trống")
    private Date ngayTao;

    private String tienDo;

    private String trangThai;

    private String views;

    @OneToMany(mappedBy = "product",
            cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @JsonIgnoreProperties({"product"})
    private List<Image> images;

    @Lob
    private String image;

    private Double latX;

    private Double longY;

    public Product() {
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public Double getLatX() {
        return latX;
    }

    public void setLatX(Double latX) {
        this.latX = latX;
    }

    public Double getLongY() {
        return longY;
    }

    public void setLongY(Double longY) {
        this.longY = longY;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String idAsString() {
        return id != null ? "" + id : null;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public Double getDienTich() {
        return dienTich;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setDienTich(Double dienTich) {
        this.dienTich = dienTich;
    }

    public Double getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(Double giaTien) {
        this.giaTien = giaTien;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public Date getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }

    public String getTienDo() {
        return tienDo;
    }

    public void setTienDo(String tienDo) {
        this.tienDo = tienDo;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    @JsonIgnoreProperties(value = {"image", "product"})
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @JsonIgnoreProperties(value = {"product", "hotPotList"})
    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Product(Long id, @Size(min = 3, max = 50, message = "Tên phải lớn hơn 3 và bé hơn 50") @NotBlank(message = "Thông tin không được bỏ trống") String tenSanPham, @NotBlank(message = "Thông tin không được bỏ trống") Project project, @Size(max = 200, message = "Địa chỉ phải trong vòng 200 ký tự") @NotBlank(message = "Thông tin không được bỏ trống") String diaChi, @NotBlank(message = "Thông tin không được bỏ trống") Double dienTich, @NotBlank(message = "Thông tin không được bỏ trống") Double giaTien, @Size(max = 200, message = "Mô tả phải trong vòng 200 ký tự") @NotBlank(message = "Thông tin không được bỏ trống") String moTa, @NotBlank(message = "Thông tin không được bỏ trống") Date ngayTao, @NotBlank(message = "Thông tin không được bỏ trống") String tienDo, @NotBlank(message = "Thông tin không được bỏ trống") String trangThai, String views, List<Image> images) {
        this.id = id;
        this.tenSanPham = tenSanPham;
        this.project = project;
        this.diaChi = diaChi;
        this.dienTich = dienTich;
        this.giaTien = giaTien;
        this.moTa = moTa;
        this.ngayTao = ngayTao;
        this.tienDo = tienDo;
        this.trangThai = trangThai;
        this.views = views;
        this.images = images;
    }
}
