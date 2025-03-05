package team_iproject_main.data.mapper;

import org.springframework.jdbc.core.RowMapper;
import team_iproject_main.data.dto.UserDO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserEmailRowMapper implements RowMapper<UserDO> {
    @Override
    public UserDO mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserDO user = new UserDO();
        user.setEmail(rs.getString("EMAIL"));

        return user;
    }
}
