package ru.jurfed.presentssystem.domain;

import java.util.List;

public class OrderDto {

    private List<Order> orderList;

    public OrderDto(List<Order> orderList) {
        this.orderList = orderList;
    }

    public void addOrder(Order order) {
        this.orderList.add(order);
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }
}
