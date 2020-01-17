package pw.react.flatly.flatlybackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pw.react.flatly.flatlybackend.model.ItemPhoto;

public interface ItemPhotoRepository extends JpaRepository<ItemPhoto, Long> {
    ItemPhoto findByItemId(Long id);
}
