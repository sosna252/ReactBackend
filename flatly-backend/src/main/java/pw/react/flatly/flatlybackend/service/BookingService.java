package pw.react.flatly.flatlybackend.service;

import pw.react.flatly.flatlybackend.model.Booking;
import pw.react.flatly.flatlybackend.model.BookingDetails;
import pw.react.flatly.flatlybackend.model.BookingList;

import java.util.List;

public interface BookingService {
    Booking getBooking(Long id);
    BookingDetails getBookingDetails(Long id);
    List<BookingList> findAllBookingListByUserId(Long id);

    Booking addBooking(Long item_id, Booking booking);

    Booking deleteBooking(Long id);

    List<Booking> findAllByUserId(Long id);
}
