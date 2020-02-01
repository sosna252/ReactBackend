package pw.react.flatly.flatlybackend.service;

import pw.react.flatly.flatlybackend.model.Booking;
import pw.react.flatly.flatlybackend.model.BookingDetails;
import pw.react.flatly.flatlybackend.model.BookingList;

import java.util.List;
import java.util.UUID;

public interface BookingService {
    Booking getBooking(UUID security_token, Long book_id);
    BookingDetails getBookingDetails(UUID security_token, Long id);

    Booking addBooking(UUID security_token, Long item_id, Booking booking);

    Booking deleteBooking(UUID security_token, Long id);

    List<Booking> findAllByToken(UUID securityToken);

    List<BookingList> findAllBookingListByToken(UUID securityToken);
}
