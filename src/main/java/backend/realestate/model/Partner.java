package backend.realestate.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "partner", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"})
})
public class Partner {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(min = 3, max = 100, message = "Tên phải lớn hơn 3 và bé hơn 100")
    @NotBlank(message = "Thông tin không được bỏ trống")
    private String tenDoiTac;

    @NotBlank
    private String linhVuc;

    @Size(max = 100, message = "Địa chỉ phải ít hơn 100 ký tự")
    @NotBlank(message = "Thông tin không được bỏ trống")
    private String diaChi;

    @Size(max = 12, message = "Số điện thoại phải ít hơn 12 ký tự")
    @NotBlank(message = "Thông tin không được bỏ trống")
    private String sdt;

    @NotBlank
    @Size(max = 50)
    @Email(message = "Sai định dạng email")
    @Column(name = "email", length = 30)
    private String email;

    @Lob
    private String logo;

    @NotBlank(message = "Số vốn không được bỏ trống")
    private Double soVonDauTu;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "project_doitac",
        joinColumns = @JoinColumn(name = "idDoiTac"),
        inverseJoinColumns = @JoinColumn(name = "idProject"))
    @JsonIgnoreProperties("projects")
    private List<Project> projects;

    public Partner() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenDoiTac() {
        return tenDoiTac;
    }

    public void setTenDoiTac(String tenDoiTac) {
        this.tenDoiTac = tenDoiTac;
    }

    public String getLinhVuc() {
        return linhVuc;
    }

    public void setLinhVuc(String linhVuc) {
        this.linhVuc = linhVuc;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public Double getSoVonDauTu() {
        return soVonDauTu;
    }

    public void setSoVonDauTu(Double soVonDauTu) {
        this.soVonDauTu = soVonDauTu;
    }

    public String idAsString() {
        return id != null ? "" + id : null;
    }

    public Partner(Long id, @Size(min = 3, max = 100, message = "Tên phải lớn hơn 3 và bé hơn 100") @NotBlank(message = "Thông tin không được bỏ trống") String tenDoiTac, @NotBlank String linhVuc, @Size(max = 100, message = "Địa chỉ phải ít hơn 100 ký tự") @NotBlank(message = "Thông tin không được bỏ trống") String diaChi, @Size(max = 12, message = "Số điện thoại phải ít hơn 12 ký tự") @NotBlank(message = "Thông tin không được bỏ trống") String sdt, @NotBlank @Size(max = 50) @Email(message = "Sai định dạng email") String email, String logo, @NotBlank(message = "Số vốn không được bỏ trống") Double soVonDauTu, List<Project> projects) {
        this.id = id;
        this.tenDoiTac = tenDoiTac;
        this.linhVuc = linhVuc;
        this.diaChi = diaChi;
        this.sdt = sdt;
        this.email = email;
        this.logo = logo;
        this.soVonDauTu = soVonDauTu;
        this.projects = projects;
    }
}
