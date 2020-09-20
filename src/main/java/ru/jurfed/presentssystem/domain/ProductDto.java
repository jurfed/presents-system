package ru.jurfed.presentssystem.domain;

import java.util.List;

public class ProductDto {

    private List<Storage> storages;

    public ProductDto() {
    }

    public ProductDto(List<Storage> storages) {
        this.storages = storages;
    }

    public void addProduct(Storage storage) {
        this.storages.add(storage);
    }

    public List<Storage> getStorages() {
        return storages;
    }

    public void setStorages(List<Storage> storages) {
        this.storages = storages;
    }
}
