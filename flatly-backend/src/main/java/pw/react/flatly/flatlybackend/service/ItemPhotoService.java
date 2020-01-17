package pw.react.flatly.flatlybackend.service;

import pw.react.flatly.flatlybackend.model.ItemPhoto;

public interface ItemPhotoService {
    ItemPhoto findItemPhotoByItemId(Long id);
    ItemPhoto saveItemPhoto(Long id, byte[] photo);
}
