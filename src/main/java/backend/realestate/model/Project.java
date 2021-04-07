package backend.realestate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(min = 3, max = 100, message = "Tên dự án phải lớn hơn 3 và ít hơn 100")
    @NotBlank(message = "Thông tin không được bỏ trống")
    private String tenDuAn;

    @Size(max = 100, message = "Tên loại hình phải trong vòng 100 ký tự")
    @NotBlank(message = "Thông tin không được bỏ trống")
    private String loaiHinh;

    @Size(max = 200, message = "Địa chỉ phải trong vòng 200 ký tự")
    @NotBlank(message = "Thông tin không được bỏ trống")
    private String diaChi;

    @NotBlank(message = "Thông tin không được bỏ trống")
    private Double dienTich;

    @NotBlank(message = "Thông tin không được bỏ trống")
    private Double chiPhiDuAn;

    @DateTimeFormat(pattern = "MM-dd-yyyy")
    private Date ngayBatDau;

    @Lob
    private String image;

    @ManyToMany(mappedBy = "projects")
    @JsonIgnoreProperties("partners")
    private List<Partner> partners;

    @Size(max = 100)
    private String trangThai;

    @NotBlank
    private Double mapX;

    @NotBlank
    private Double mapY;

    @NotBlank
    private Double banKinh;

    @OneToMany(mappedBy = "project",
            cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @JsonIgnoreProperties(value = {"project", "images"})
    private List<Product> product;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    public Project() {
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

//    @JsonIgnoreProperties(value = {"project", "images"})
    @JsonIgnore
    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenDuAn() {
        return tenDuAn;
    }

    public void setTenDuAn(String tenDuAn) {
        this.tenDuAn = tenDuAn;
    }

    public String getLoaiHinh() {
        return loaiHinh;
    }

    public void setLoaiHinh(String loaiHinh) {
        this.loaiHinh = loaiHinh;
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

    public void setDienTich(Double dienTich) {
        this.dienTich = dienTich;
    }

    public Double getChiPhiDuAn() {
        return chiPhiDuAn;
    }

    public void setChiPhiDuAn(Double chiPhiDuAn) {
        this.chiPhiDuAn = chiPhiDuAn;
    }

    public Date getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(Date ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public List<Partner> getPartners() {
        return partners;
    }

    public void setPartners(List<Partner> partners) {
        this.partners = partners;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public Double getMapX() {
        return mapX;
    }

    public void setMapX(Double mapX) {
        this.mapX = mapX;
    }

    public Double getMapY() {
        return mapY;
    }

    public void setMapY(Double mapY) {
        this.mapY = mapY;
    }

    public Double getBanKinh() {
        return banKinh;
    }

    public void setBanKinh(Double banKinh) {
        this.banKinh = banKinh;
    }

    public String idAsString() {
        return id != null ? "" + id : null;
    }
}
