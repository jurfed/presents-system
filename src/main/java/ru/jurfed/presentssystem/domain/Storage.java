package ru.jurfed.presentssystem.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * storage for presents
 */
@Entity
@Table(name = "storage")
public class Storage {

    public Storage() {
    }

    public Storage(String productType, Integer availableValue, Integer minValue) {
        this.productType = productType;
        this.availableValue = availableValue;
        this.minValue = minValue;
    }

    public Storage(String productType, Integer minValue) {
        this.productType = productType;
        this.minValue = minValue;
    }

    public Storage(String productType) {
        this.productType = productType;
        this.availableValue = 0;
        this.minValue = 1;
    }

    @Id
    @Column(name = "product_type")
    private String productType;

    @Column(name = "available_value")
    private Integer availableValue;

    @Column(name = "min_value")
    private Integer minValue;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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

    @Override
    public String toString() {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        return gson.toJson(this);
    }

}
