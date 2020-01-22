package pw.react.flatly.flatlybackend.dao;

import pw.react.flatly.flatlybackend.model.Item;

import java.util.List;

public interface ItemDao {
    List<Item> findAll();
    void insertItem(Item it);
    void updateItem(Item it);
    void executeUpdateItem(Item it);
    void deleteItem(Item it);
}
