package team_iproject_main.data.request;

import lombok.Data;

@Data
public class FindPasswordRequest {
    String email;
    String name;
    String phoneNumber;
}
