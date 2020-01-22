package pw.react.flatly.flatlybackend.mapper;

import org.springframework.jdbc.core.RowMapper;
import pw.react.flatly.flatlybackend.model.Booking;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class BookingRowMapper implements RowMapper<Booking> {
    @Override
    public Booking mapRow(ResultSet rs, int arg1) throws SQLException {

        Booking book = new Booking();
        book.setId(Long.parseLong(rs.getString("id")));
        //TODO: setItem
        book.setStart_date(LocalDate.parse(rs.getString("start_date")));
        book.setEnd_date(LocalDate.parse(rs.getString("end_date")));
        book.setName(rs.getString("name"));
        book.setLast_name(rs.getString("last_name"));
        book.setEmail(rs.getString("email"));
        book.setPeople(Integer.parseInt(rs.getString("people")));
        return book;
    }
}