package pw.react.flatly.flatlybackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.react.flatly.flatlybackend.model.Booking;
import pw.react.flatly.flatlybackend.model.Item;
import pw.react.flatly.flatlybackend.repository.BookingRepository;
import pw.react.flatly.flatlybackend.repository.ItemRepository;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    BookingRepository bookingRepository;
    ItemRepository itemRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public Booking addBooking(Long item_id, Booking booking) {
        Item item = itemRepository.findById(item_id).orElse(null);
        if(item==null) return null;

        if(item.getStart_date_time().compareTo(booking.getStart_date())>0 || item.getEnd_date_time().compareTo(booking.getEnd_date())<0) return null;

        List<Booking> itemBookings = item.getBookings();
        for(Booking itemBooking : itemBookings) {
            if(!(itemBooking.getStart_date().compareTo(booking.getEnd_date())<0 && itemBooking.getEnd_date().compareTo(booking.getStart_date())<0)) return null;
        }

        bookingRepository.save(booking);
        return booking;
    }

    @Override
    public Booking deleteBooking(Long id) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        if(booking==null) return null;

        bookingRepository.delete(booking);
        return booking;
    }

}
