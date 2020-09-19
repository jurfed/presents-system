package ru.jurfed.presentssystem.domain;

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
}
