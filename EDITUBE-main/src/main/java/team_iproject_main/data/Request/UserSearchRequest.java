package team_iproject_main.data.Request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserSearchRequest {
    private String job;
    private String searchtext;
}
