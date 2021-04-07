package backend.realestate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "hotpot")
public class HotPot {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @ManyToOne
    @JoinColumn(name = "rootImage_id")
    @JsonIgnoreProperties("product")
    Image rootImage;

    @ManyToOne
    @JoinColumn(name = "linkedImage_id")
    @JsonIgnoreProperties("product")
    Image linkedImage;

    private Double xIndex;
    private Double yIndex;
    private Double zIndex;
    private Double imgScale;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnoreProperties("hotPotList")
    public Image getRootImage() {
        return rootImage;
    }

    public void setRootImage(Image rootImage) {
        this.rootImage = rootImage;
    }

    @JsonIgnoreProperties(value = {"hotPotList","product","image"})
    public Image getLinkedImage() {
        return linkedImage;
    }

    public void setLinkedImage(Image linkedImage) {
        this.linkedImage = linkedImage;
    }

    public Double getxIndex() {
        return xIndex;
    }

    public void setxIndex(Double xIndex) {
        this.xIndex = xIndex;
    }

    public Double getyIndex() {
        return yIndex;
    }

    public void setyIndex(Double yIndex) {
        this.yIndex = yIndex;
    }

    public Double getzIndex() {
        return zIndex;
    }

    public void setzIndex(Double zIndex) {
        this.zIndex = zIndex;
    }

    public Double getImgScale() {
        return imgScale;
    }

    public void setImgScale(Double imgScale) {
        this.imgScale = imgScale;
    }

    public HotPot() {
    }
}
