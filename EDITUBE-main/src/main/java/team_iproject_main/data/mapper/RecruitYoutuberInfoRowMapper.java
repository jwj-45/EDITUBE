package team_iproject_main.data.mapper;

import org.springframework.jdbc.core.RowMapper;
import team_iproject_main.data.dto.YoutuberDO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RecruitYoutuberInfoRowMapper implements RowMapper<YoutuberDO> {

    @Override
    public YoutuberDO mapRow(ResultSet rs, int rowNum) throws SQLException {

        YoutuberDO youtuberDO = new YoutuberDO();
        youtuberDO.setYoutuber_email(rs.getString("youtuber_email"));
        youtuberDO.setChannel_id(rs.getString("channel_id"));
        youtuberDO.setSubscribe(rs.getLong("subscribe"));
        youtuberDO.setVideo_count(rs.getLong("videoCount"));
        youtuberDO.setView_count(rs.getLong("viewCount"));
        youtuberDO.setChannel_name(rs.getString("channelName"));
        youtuberDO.setChannel_photo(rs.getString("channelPhoto"));


        return youtuberDO;
    }
}
