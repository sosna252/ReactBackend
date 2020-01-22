package pw.react.flatly.flatlybackend.dao;

import pw.react.flatly.flatlybackend.model.Booking;

import java.util.List;

public interface BookingDao {
    List<Booking> findAll();
    void insertBooking(Booking book);
    void updateBooking(Booking book);
    void executeUpdateBooking(Booking book);
    void deleteBooking(Booking book);
}
