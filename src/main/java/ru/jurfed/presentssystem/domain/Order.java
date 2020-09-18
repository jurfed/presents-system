package ru.jurfed.presentssystem.domain;

import javax.persistence.*;

@Entity
@Table(name = "Ordered")
public class Order {


    public Order() {
    }

    public Order(String productType, String fio, Integer year) {
        this.productType = productType;
        this.fio = fio;
        this.year = year;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderNumber")
    private Integer orderNumber;


    @Column(name = "productType")
    private String productType;

    @Column(name = "fio")
    private String fio;

    @Column(name = "year")
    private Integer year;


    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
