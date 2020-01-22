package pw.react.flatly.flatlybackend.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import pw.react.flatly.flatlybackend.mapper.BookingRowMapper;
import pw.react.flatly.flatlybackend.model.Booking;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BookingDaoImpl implements BookingDao {

    public BookingDaoImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    private NamedParameterJdbcTemplate template;

    @Override
    public List<Booking> findAll() {
        return template.query("select * from public.bookings", new BookingRowMapper());
    }

    @Override
    public void insertBooking(Booking book) {
        final String sql = "insert into public.bookings(id, item_id, start_date, end_date, name, last_name, email, people) values(:id, :item_id, :start_date, :end_date, :name, :last_name, :email, :people);";
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", book.getId())
                .addValue("item_id", book.getItem().getId())
                .addValue("start_date", book.getStart_date())
                .addValue("end_date", book.getEnd_date())
                .addValue("name", book.getName())
                .addValue("last_name", book.getLast_name())
                .addValue("email", book.getEmail())
                .addValue("people", book.getPeople());
        template.update(sql,param, holder);
    }

    @Override
    public void updateBooking(Booking book) {
        final String sql = "update public.bookings set item_id=:item_id, start_date=:start_date, end_date=:end_date, name=:name, last_name=:last_name, email=:email, people=:people where id=:id";
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", book.getId())
                .addValue("item_id", book.getItem().getId())
                .addValue("start_date", book.getStart_date())
                .addValue("end_date", book.getEnd_date())
                .addValue("name", book.getName())
                .addValue("last_name", book.getLast_name())
                .addValue("email", book.getEmail())
                .addValue("people", book.getPeople());
        template.update(sql,param, holder);
    }

    @Override
    public void executeUpdateBooking(Booking book) {
        final String sql = "update public.bookings set item_id=:item_id, start_date=:start_date, end_date=:end_date, name=:name, last_name=:last_name, email=:email, people=:people where id=:id";
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("id", book.getId());
        map.put("item_id", book.getItem().getId());
        map.put("start_date", book.getStart_date());
        map.put("end_date", book.getEnd_date());
        map.put("name", book.getName());
        map.put("last_name", book.getLast_name());
        map.put("email", book.getEmail());
        map.put("people", book.getPeople());

        template.execute(sql,map,new PreparedStatementCallback<Object>() {
            @Override
            public Object doInPreparedStatement(PreparedStatement ps)
                    throws SQLException, DataAccessException {
                return ps.executeUpdate();
            }
        });
    }

    @Override
    public void deleteBooking(Booking book) {
        final String sql = "delete from public.bookings where id=:id";
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("id", book.getId());

        template.execute(sql,map,new PreparedStatementCallback<Object>() {
            @Override
            public Object doInPreparedStatement(PreparedStatement ps)
                    throws SQLException, DataAccessException {
                return ps.executeUpdate();
            }
        });
    }
}
