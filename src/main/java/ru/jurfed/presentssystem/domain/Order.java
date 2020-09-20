package ru.jurfed.presentssystem.domain;

import javax.persistence.*;

@Entity
@Table(name = "ordered")
public class Order {


    public Order() {
    }

    public Order(String productType, String fio, Integer year) {
        this.productType = productType;
        this.fio = fio;
        this.year = year;
    }

    public Order(String productType, String fio, Integer year, boolean released) {
        this.productType = productType;
        this.fio = fio;
        this.year = year;
        this.released = released;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_number")
    private Integer orderNumber;


    @Column(name = "o_product_type")
    private String productType;

    @Column(name = "fio")
    private String fio;

    @Column(name = "year")
    private Integer year;

    @Column(name = "released")
    private boolean released;

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

    public boolean isReleased() {
        return released;
    }

    public void setReleased(boolean released) {
        this.released = released;
    }
}
