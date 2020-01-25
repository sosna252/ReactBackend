package pw.react.flatly.flatlybackend.service;

import pw.react.flatly.flatlybackend.model.ItemPhoto;

import java.util.UUID;

public interface ItemPhotoService {
    ItemPhoto findItemPhotoByItemId(Long id);
    ItemPhoto saveItemPhoto(UUID security_token, Long id, byte[] photo);
}
