package pw.react.flatly.flatlybackend.service;

import pw.react.flatly.flatlybackend.model.Booking;

import java.util.List;

public interface BookingService {
    Booking addBooking(Long item_id, Booking booking);

    Booking deleteBooking(Long id);

    List<Booking> findAllByUserId(Long id);
}
