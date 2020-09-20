package ru.jurfed.presentssystem.Dto;

import ru.jurfed.presentssystem.domain.Storage;

import java.util.ArrayList;
import java.util.List;

public class ProductDto {

    private List<Storage> storages;

    public ProductDto() {
        storages = new ArrayList<>();
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
