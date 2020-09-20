package ru.jurfed.presentssystem.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.persistence.*;

@Entity
@Table(name = "manufacturing")
public class Manufacturing {

    public Manufacturing() {
    }

    public Manufacturing(String productType, Integer count) {
        this.productType = productType;
        this.count = count;
    }

    public Manufacturing(Integer count) {
        this.count = count;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_number")
    private Integer requestNumber;

    @Column(name = "m_product_type")
    private String productType;

    @Column(name = "count")
    private Integer count;


    public Integer getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber(Integer requestNumber) {
        this.requestNumber = requestNumber;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        return gson.toJson(this);
    }
}
