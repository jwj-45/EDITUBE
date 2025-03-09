package team_iproject_main.data.request;

import lombok.Data;


@Data
public class RequestId {
    private String channelId;
    private String youtuberEmail;
    private Long subscribe;
    private Long videoCount;
    private Long viewCount;
    private String channelName;
    private String channelPhoto;
}
