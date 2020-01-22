package pw.react.flatly.flatlybackend.dao;

import pw.react.flatly.flatlybackend.model.ItemPhoto;

import java.util.List;

public interface ItemPhotoDao {
    List<ItemPhoto> findAll();
    void insertItemPhoto(ItemPhoto photo);
    void updateItemPhoto(ItemPhoto photo);
    void executeUpdateItemPhoto(ItemPhoto photo);
    void deleteItemPhoto(ItemPhoto photo);
}
