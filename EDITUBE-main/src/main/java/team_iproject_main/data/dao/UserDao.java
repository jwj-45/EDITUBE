package team_iproject_main.data.dao;

import team_iproject_main.data.request.RegisterReqeustChannel;
import team_iproject_main.data.request.UserSearchRequest;
import team_iproject_main.data.request.UserUpdateRequest;
import team_iproject_main.data.dto.UserDO;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface UserDao {

    void createEditor(UserDO user) throws UnsupportedEncodingException;

    void createYoutuber(UserDO user);

    UserDO findByEmail(String email);

    UserDO findByNickname1(String nickname);

    UserDO findByPhoneNumber(String phoneNumber);

    List<UserDO> userFindById(UserSearchRequest userSearchRequest, int postsPerPage, int offset);

    int getTotalSearch(UserSearchRequest userSearchRequest);

    List<UserDO> findAll(int postsPerPage, int offset);

    int getTotalResults();

    UserDO findByNameAndPhone(String name, String phoneNumber);

    UserDO findByNickname(String nickname);

    RegisterReqeustChannel findByChannel(String channel_id);

    void updatePassword(String email, String newpwd);

    void updateUserInfo(UserUpdateRequest userUpdateRequest, String email);

    void deleteUserInfo(String email);
}
