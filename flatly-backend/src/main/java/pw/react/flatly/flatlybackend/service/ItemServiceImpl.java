package pw.react.flatly.flatlybackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.react.flatly.flatlybackend.exception.ItemNotFoundException;
import pw.react.flatly.flatlybackend.exception.ParamsMismatchException;
import pw.react.flatly.flatlybackend.model.Booking;
import pw.react.flatly.flatlybackend.model.Item;
import pw.react.flatly.flatlybackend.repository.ItemRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
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
    public List<Item> findAll(String dateFrom, String dateTo, String city, Integer people, Long authorId, String sort, String dir) {

        LocalDate from = dateFrom!=null ? LocalDate.parse(dateFrom) : null;
        LocalDate to = dateFrom!=null ? LocalDate.parse(dateTo) : null;

        if(from!=null && to!=null) {
            if(from.compareTo(to)>0) {
                throw new ParamsMismatchException("Data rozpoczęcia nie może być późniejsza niż zakończenia");
            }
        }

        return itemRepository.findAll().stream()
                .filter(item -> {

                    if(from==null && to==null) return true;

                    List<Booking> bookings = item.getBookings();
                    LocalDate itemFrom = from!=null ? from : item.getStart_date_time();
                    LocalDate itemTo = to!=null ? to : item.getEnd_date_time();


                    for (Booking booking: bookings) {
                        if(booking.getStart_date().compareTo(itemFrom)>=0 && booking.getStart_date().compareTo(itemTo)<=0) return false;
                    }

                    return true;
                })
                .filter(item -> city == null || city.equals(item.getCity()))
                .filter(item -> people == null || people <= item.getBeds())
                .filter(item -> authorId == null || authorId.equals(item.getUser().getId()))
                .sorted((i1, i2) -> {
                    if(dir.equals("desc")) {
                        switch (sort) {
                            case "end-time":
                                return i2.getEnd_date_time().compareTo((i1.getEnd_date_time()));
                            case "price":
                                return i2.getPrice().compareTo(i1.getPrice());
                            case "rating":
                                return i2.getRating().compareTo(i1.getRating());
                            default:
                                return i2.getStart_date_time().compareTo(i1.getStart_date_time());
                        }
                    } else {
                        switch (sort) {
                            case "end-time":
                                return i1.getEnd_date_time().compareTo((i2.getEnd_date_time()));
                            case "price":
                                return i1.getPrice().compareTo(i2.getPrice());
                            case "rating":
                                return i1.getRating().compareTo(i2.getRating());
                            default:
                                return i1.getStart_date_time().compareTo(i2.getStart_date_time());
                        }
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<List<LocalDate>> findVacantById(Long id) {
        Item item = itemRepository.findById(id).orElse(null);
        if(item==null) return null;

        List<Booking> bookings = item.getBookings();

        List<List<LocalDate>> lists = new ArrayList<List<LocalDate>>();

        List<LocalDate> dates = new ArrayList<LocalDate>();
        dates.add(item.getStart_date_time());
        for (Booking booking:bookings) {
            dates.add(booking.getStart_date());
            lists.add(dates);

            dates = new ArrayList<LocalDate>();

            dates.add(booking.getEnd_date());
        }
        dates.add(item.getEnd_date_time());
        lists.add(dates);

        return lists;
    }

    @Override
    public Item findById(Long id) {
        return itemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Nie ma takiego mieszkania"));
    }

    @Override
    public void deleteById(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Nie ma takiego mieszkania"));

        itemRepository.delete(item);
    }

    @Override
    public Item updateById(Long id, Item itemDetails) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Nie ma takiego mieszkania"));

        itemDetails.setId(item.getId());
        itemRepository.save(itemDetails);

        return itemDetails;
    }

}
