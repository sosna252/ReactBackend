package pw.react.flatly.flatlybackend.service;

import pw.react.flatly.flatlybackend.model.Item;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ItemService {
    Item save(Item item);

    List<Item> findAll(String dateFrom, String dateTo, String city, Integer people, Long authorId, String sort, String dir);
    List<List<LocalDate>> findVacantById(Long id);

    Item findById(Long id);

    void deleteById(Long id);

    Item updateById(Long id, Item itemDetails);
}
