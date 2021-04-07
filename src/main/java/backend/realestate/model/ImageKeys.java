package backend.realestate.model;


import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@EqualsAndHashCode
@Embeddable
class ImageKeys implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "rootImage_id")
    Long rootImageId;

    @Column(name = "linkedImage_id")
    Long linkedImageId;

    public Long getRootImageId() {
        return rootImageId;
    }

    public void setRootImageId(Long rootImageId) {
        this.rootImageId = rootImageId;
    }

    public Long getLinkedImageId() {
        return linkedImageId;
    }

    public void setLinkedImageId(Long linkedImageId) {
        this.linkedImageId = linkedImageId;
    }

    public ImageKeys() {
    }
}