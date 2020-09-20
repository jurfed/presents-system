package ru.jurfed.presentssystem.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "storage")
public class Storage {

    public Storage() {
    }


    public Storage(String productType) {
        this.productType = productType;
    }

    @Id
    @Column(name = "product_type")
    private String productType;

    @Column(name = "available_value")
    private Integer availableValue;

    @Column(name = "min_value")
    private Integer minValue;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "o_product_type", referencedColumnName = "product_type")
    private List<Order> orders = new ArrayList<>();


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

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

/*    public List<Manufacturing> getManufacturings() {
        return manufacturings;
    }

    public void setManufacturings(List<Manufacturing> manufacturings) {
        this.manufacturings = manufacturings;
    }*/
}
