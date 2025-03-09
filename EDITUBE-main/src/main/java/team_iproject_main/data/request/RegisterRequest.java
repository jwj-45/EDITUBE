package team_iproject_main.data.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private String confirmPassword;
    private String name;
    private String nickName;
    private String phoneNumber;
    private String address;
    private String detailAddr;
    private String gender;
    private String birthDate;
    private String channelId;
    private Long subscribe;
    private Long videoCount;
    private Long viewCount;
    private String channelName;
    private String channelPhoto;
}
