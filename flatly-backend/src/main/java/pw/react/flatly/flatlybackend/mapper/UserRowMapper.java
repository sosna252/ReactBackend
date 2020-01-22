package pw.react.flatly.flatlybackend.mapper;

import org.springframework.jdbc.core.RowMapper;
import pw.react.flatly.flatlybackend.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int arg1) throws SQLException {

        User usr = new User();
        usr.setId(Long.parseLong(rs.getString("id")));
        usr.setLogin(rs.getString("login"));
        usr.setPassword(rs.getString("password"));
        usr.setFirst_name(rs.getString("first_name"));
        usr.setLast_name(rs.getString("last_name"));
        usr.setSecurity_token(UUID.fromString(rs.getString("security_token")));
        return usr;
    }
}