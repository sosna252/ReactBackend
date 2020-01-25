package pw.react.flatly.flatlybackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.react.flatly.flatlybackend.exception.ItemNotFoundException;
import pw.react.flatly.flatlybackend.exception.UserNotFoundException;
import pw.react.flatly.flatlybackend.model.Item;
import pw.react.flatly.flatlybackend.model.ItemPhoto;
import pw.react.flatly.flatlybackend.model.User;
import pw.react.flatly.flatlybackend.repository.ItemPhotoRepository;
import pw.react.flatly.flatlybackend.repository.ItemRepository;
import pw.react.flatly.flatlybackend.repository.UserRepository;

import java.util.UUID;

@Service
public class ItemPhotoServiceImpl implements ItemPhotoService {

    private ItemPhotoRepository itemPhotoRepository;
    private ItemRepository itemRepository;

    private UserRepository userRepository;

    @Autowired
    ItemPhotoServiceImpl(ItemPhotoRepository itemPhotoRepository, ItemRepository itemRepository, UserRepository userRepository) {
        this.itemPhotoRepository = itemPhotoRepository;

        this.itemRepository = itemRepository;

        this.userRepository = userRepository;
    }

    @Override
    public ItemPhoto findItemPhotoByItemId(Long id) {
        return itemPhotoRepository.findByItemId(id);
    }

    @Override
    public ItemPhoto saveItemPhoto(UUID security_token, Long id, byte[] photo) {
        User user = userRepository.findBySecurityToken(security_token).orElseThrow(() -> new UserNotFoundException("Nie masz uprawnień"));
        Item item = itemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Nie ma takiego mieszkania"));

        ItemPhoto itemPhoto = new ItemPhoto();
        itemPhoto.setItem(item);
        itemPhoto.setPhoto(photo);

        return itemPhotoRepository.save(itemPhoto);
    }
}
