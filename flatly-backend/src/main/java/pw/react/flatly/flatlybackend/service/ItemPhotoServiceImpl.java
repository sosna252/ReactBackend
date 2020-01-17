package pw.react.flatly.flatlybackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.react.flatly.flatlybackend.model.Item;
import pw.react.flatly.flatlybackend.model.ItemPhoto;
import pw.react.flatly.flatlybackend.repository.ItemPhotoRepository;
import pw.react.flatly.flatlybackend.repository.ItemRepository;

@Service
public class ItemPhotoServiceImpl implements ItemPhotoService {

    ItemPhotoRepository itemPhotoRepository;
    ItemRepository itemRepository;

    @Autowired
    ItemPhotoServiceImpl(ItemPhotoRepository itemPhotoRepository, ItemRepository itemRepository) {
        this.itemPhotoRepository = itemPhotoRepository;

        this.itemRepository = itemRepository;
    }

    @Override
    public ItemPhoto findItemPhotoByItemId(Long id) {
        return itemPhotoRepository.findByItemId(id);
    }

    @Override
    public ItemPhoto saveItemPhoto(Long id, byte[] photo) {
        Item item = itemRepository.findById(id).orElse(null);

        ItemPhoto itemPhoto = new ItemPhoto();
        itemPhoto.setItem(item);
        itemPhoto.setPhoto(photo);

        return itemPhotoRepository.save(itemPhoto);
    }
}
