package team_iproject_main.data.dto;

import lombok.Data;

@Data
public class YoutuberDO {
    String channel_id;
    String channel_photo;
    String channel_name;
    String youtuber_email;
    Long subscribe;
    Long video_count;
    Long view_count;
}
