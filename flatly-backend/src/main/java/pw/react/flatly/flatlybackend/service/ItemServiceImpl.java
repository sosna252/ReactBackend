package pw.react.flatly.flatlybackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.react.flatly.flatlybackend.model.Booking;
import pw.react.flatly.flatlybackend.model.Item;
import pw.react.flatly.flatlybackend.repository.ItemRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    ItemRepository itemRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Item save(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public List<Item> findByParams(LocalDate dateFrom, LocalDate dateTo, String city, Integer people, UUID authorId) {

        List<Item> items = itemRepository.findAll().stream()
                .filter(item -> {

                    if(dateFrom==null && dateTo==null) return true;

                    List<Booking> bookings = item.getBookings();
                    LocalDate from = dateFrom!=null ? dateFrom : item.getStartDateTime();
                    LocalDate to = dateTo!=null ? dateTo : item.getEndDateTime();


                    for (Booking booking: bookings) {
                        if(booking.getStartDate().compareTo(from)>=0 && booking.getStartDate().compareTo(to)<=0) return false;
                    }

                    return true;
                })
                .filter(item -> city == null || city.equals(item.getCity()))
                .filter(item -> people == null || people <= item.getBeds())
                .filter(item -> authorId == null || authorId == item.getUser().getUserId())
                .collect(Collectors.toList());

        return items;
    }

    @Override
    public List<List<LocalDate>> findVacantById(UUID id) {
        Item item = itemRepository.findById(id).orElse(null);
        if(item==null) return null;

        List<Booking> bookings = item.getBookings();

        List<List<LocalDate>> lists = new ArrayList<List<LocalDate>>();

        List<LocalDate> dates = new ArrayList<LocalDate>();
        dates.add(item.getStartDateTime());
        for (Booking booking:bookings) {
            dates.add(booking.getStartDate());
            lists.add(dates);

            dates = new ArrayList<LocalDate>();

            dates.add(booking.getEndDate());
        }
        dates.add(item.getEndDateTime());
        lists.add(dates);

        return lists;
    }

    @Override
    public Item findById(UUID id) {
        return itemRepository.findById(id).orElse(null);
    }

    @Override
    public boolean deleteById(UUID id) {
        Item item = itemRepository.findById(id).orElse(null);
        if(item == null) return false;

        itemRepository.delete(item);
        return true;
    }

}
