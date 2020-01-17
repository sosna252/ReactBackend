package pw.react.flatly.flatlybackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pw.react.flatly.flatlybackend.model.Booking;

import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
