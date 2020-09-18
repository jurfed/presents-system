package ru.jurfed.presentssystem.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "storage")
public class Storage {

    public Storage() {
    }

    public Storage(String productType) {
        this.productType = productType;
    }

    @Id
    @Column(name = "productType")
    private String productType;

    @Column(name = "availableValue")
    private Integer availableValue;

    @Column(name = "minValue")
    private Integer minValue;

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Integer getAvailableValue() {
        return availableValue;
    }

    public void setAvailableValue(Integer availableValue) {
        this.availableValue = availableValue;
    }

    public Integer getMinValue() {
        return minValue;
    }

    public void setMinValue(Integer minValue) {
        this.minValue = minValue;
    }
}
