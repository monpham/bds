package backend.realestate.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "tk_doanh_thu")
public class TKDoanhThu {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Thông tin không được bỏ trống")
    @OneToOne
    @JoinColumn(name="idDuAn")
    private Project project;

    @NotBlank(message = "Thông tin không được bỏ trống")
    private Integer soSP;

    @NotBlank(message = "Thông tin không được bỏ trống")
    private Double tongDoanhThu;

    @NotBlank(message = "Thông tin không được bỏ trống")
    private Double doanhThuCaoNhat;

    @NotBlank(message = "Thông tin không được bỏ trống")
    private Double doanhThuThapNhat;

    @NotBlank(message = "Thông tin không được bỏ trống")
    private Double doanhThuTB;

    public TKDoanhThu() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Integer getSoSP() {
        return soSP;
    }

    public void setSoSP(Integer soSP) {
        this.soSP = soSP;
    }

    public Double getTongDoanhThu() {
        return tongDoanhThu;
    }

    public void setTongDoanhThu(Double tongDoanhThu) {
        this.tongDoanhThu = tongDoanhThu;
    }

    public Double getDoanhThuCaoNhat() {
        return doanhThuCaoNhat;
    }

    public void setDoanhThuCaoNhat(Double doanhThuCaoNhat) {
        this.doanhThuCaoNhat = doanhThuCaoNhat;
    }

    public Double getDoanhThuThapNhat() {
        return doanhThuThapNhat;
    }

    public void setDoanhThuThapNhat(Double doanhThuThapNhat) {
        this.doanhThuThapNhat = doanhThuThapNhat;
    }

    public Double getDoanhThuTB() {
        return doanhThuTB;
    }

    public void setDoanhThuTB(Double doanhThuTB) {
        this.doanhThuTB = doanhThuTB;
    }

    public TKDoanhThu(Long id, @NotBlank(message = "Thông tin không được bỏ trống") Project project, @NotBlank(message = "Thông tin không được bỏ trống") Integer soSP, @NotBlank(message = "Thông tin không được bỏ trống") Double tongDoanhThu, @NotBlank(message = "Thông tin không được bỏ trống") Double doanhThuCaoNhat, @NotBlank(message = "Thông tin không được bỏ trống") Double doanhThuThapNhat, @NotBlank(message = "Thông tin không được bỏ trống") Double doanhThuTB) {
        this.id = id;
        this.project = project;
        this.soSP = soSP;
        this.tongDoanhThu = tongDoanhThu;
        this.doanhThuCaoNhat = doanhThuCaoNhat;
        this.doanhThuThapNhat = doanhThuThapNhat;
        this.doanhThuTB = doanhThuTB;
    }
}
