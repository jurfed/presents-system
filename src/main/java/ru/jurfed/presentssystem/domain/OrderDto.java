package ru.jurfed.presentssystem.domain;

import java.util.List;

public class OrderDto {

    public OrderDto() {
    }

    private List<Order> orders;

    public OrderDto(List<Order> orders) {
        this.orders = orders;
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
