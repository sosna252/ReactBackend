package pw.react.flatly.flatlybackend.service;

import pw.react.flatly.flatlybackend.model.Item;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ItemService {
    Item save(Item item);

    List<Item> findByParams(LocalDate dateFrom, LocalDate dateTo, String city, Integer people, UUID authorId);
    List<List<LocalDate>> findVacantById(UUID id);

    Item findById(UUID id);

    boolean deleteById(UUID id);
}
