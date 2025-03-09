package team_iproject_main.data.mapper;


import org.springframework.jdbc.core.RowMapper;
import team_iproject_main.data.request.RegisterReqeustChannel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class YoutuberRowMapper implements RowMapper<RegisterReqeustChannel> {
    @Override
    public RegisterReqeustChannel mapRow(ResultSet rs, int rowNum) throws SQLException {
        RegisterReqeustChannel uq = new RegisterReqeustChannel();
        uq.setYoutuber_email(rs.getString("youtuber_email"));
        uq.setChannel_id(rs.getString("channel_id"));
        uq.setSubscribe(rs.getLong("subscribe"));
        uq.setVideo_count(rs.getLong("videoCount"));
        uq.setView_count(rs.getLong("viewCount"));
        uq.setChannel_name(rs.getString("channelName"));
        uq.setChannel_photo(rs.getString("channelPhoto"));

        return uq;
    }
}
