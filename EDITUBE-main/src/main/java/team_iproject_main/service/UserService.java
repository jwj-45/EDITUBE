package team_iproject_main.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team_iproject_main.exception.*;
import team_iproject_main.data.dao.impl.UserDaoImpl;
import team_iproject_main.data.dto.UserDO;
import team_iproject_main.data.request.*;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.List;

@Service
@Log4j2
public class UserService {

    @Autowired
    private UserDaoImpl userDao;

    public void editorSignUp(RegisterRequest req) throws UnsupportedEncodingException {
        UserDO user = userDao.findByEmail(req.getEmail());
        UserDO userSelectByNickName = userDao.findByNickname(req.getNickName());
        UserDO userSelectByPhoneNumber = userDao.findByPhoneNumber(req.getPhoneNumber());
        if (user != null) {
            throw new DuplicateEmailException();
        }
        if (userSelectByNickName != null){
            throw new DuplicateNickNameException();

        }
        if (userSelectByPhoneNumber != null) {
            throw new DuplicatePhoneNumberException();
        }

        user = new UserDO(req.getEmail(), req.getPassword(), req.getName(), req.getNickName(), req.getPhoneNumber(), req.getAddress(),
                req.getDetailAddr(), "편집자", req.getGender(), LocalDate.parse(req.getBirthDate()));
        userDao.createEditor(user);
    }

    public void youtuberSignUp(RegisterRequest req) {
        UserDO user = userDao.findByEmail(req.getEmail());
        UserDO userSelectByNickName = userDao.findByNickname(req.getNickName());
        UserDO userSelectByPhoneNumber = userDao.findByPhoneNumber(req.getPhoneNumber());
        RegisterReqeustChannel uq = userDao.findByChannel(req.getChannelId());
        if (user != null) {
            throw new DuplicateEmailException();
        }
        if (userSelectByNickName != null){
            throw new DuplicateNickNameException();
        }
        if (uq != null) {
            throw new DuplicateChannelException("channel_id address is already registered.");
        }
        if (userSelectByPhoneNumber != null) {
            throw new DuplicatePhoneNumberException();
        }

        user = new UserDO(req.getEmail(), req.getPassword(), req.getName(), req.getNickName(), req.getPhoneNumber(), req.getAddress(),
                req.getDetailAddr(), "유튜버", req.getGender(), LocalDate.parse(req.getBirthDate()), req.getChannelId(),
                req.getSubscribe(), req.getVideoCount(), req.getViewCount(), req.getChannelName(), req.getChannelPhoto());
        userDao.createYoutuber(user);
    }

    public UserDO findUser(String email){
        return userDao.findByEmail(email);
    }


    //희수
    //전체 회원 조회
    //준영 페이징 추가
    public List<UserDO> findMembers(int page, int postsPerPage) {
        int offset = (page - 1) * postsPerPage;
        return userDao.findAll(postsPerPage, offset);
    }

    public int getTotalResults() {
        return userDao.getTotalResults();
    }

    //희수
    //회원 삭제
    public void deleteMember(String email){
        userDao.deleteUserInfo(email);
    }

    //희수
    //회원 id,닉네임 검색
    public List<UserDO> findMember(UserSearchRequest userSearchRequest, int page, int postsPerPage) {
        int offset = (page - 1) * postsPerPage;
        return userDao.userFindById(userSearchRequest, postsPerPage, offset);
    }

    public int getTotalSearch(UserSearchRequest userSearchRequest) {
        return userDao.getTotalSearch(userSearchRequest);
    }

    //0506-손주현 findUser 메소드 오버로딩
    public UserDO findUser(String name, String phoneNumber){
        return userDao.findByNameAndPhone(name, phoneNumber);
    }

    //0508손주현 - checkLoginAuth 수정
    public boolean checkLoginAuth(LoginCommand login) {
        boolean result = false;
        UserDO users = userDao.findByEmail(login.getEmail());
        if(users == null){
            throw new UserNotFoundException();
        }
        else if (!users.checkPassword(login.getPassword())) {
            throw new WrongPasswordException();
        }
        if(users != null && users.checkPassword(login.getPassword())){
            result = true;
        }
        return result;
    }

    //0506-손주현
    //0508손주현 - checkFindId 수정
    public boolean checkFindId(FindIdRequest req) throws UserNotFoundException {
        boolean result = false;
        UserDO users = userDao.findByNameAndPhone(req.getName(), req.getPhoneNumber());
        if(users == null){
            throw new UserNotFoundException();
        }
        if(users != null && users.checkNameAndPhonenum(req.getName(), req.getPhoneNumber())){
            result = true;
        }
        return result;
    }

    // 비밀번호 찾기 후 비밀번호 변경
    public void changePwd(String email, String newpwd) {
        userDao.updatePassword(email, newpwd);
    }

    public void myPageUpdate(UserUpdateRequest userUpdateRequest, String id) {
        userDao.updateUserInfo(userUpdateRequest,id);
    }

    public UserDO findNickname(String nickname){return userDao.findByNickname(nickname);}

    public UserDO findById(String email) {
        return userDao.findByEmail(email);
    }

    public boolean confirmEmail(String email) {
        boolean result = false;
        UserDO users = userDao.findByEmail(email);
        log.info("service"+users);
        if(users == null) {
            return result;
        }
        else if(!users.ConfirmEmail(email)) {
            return result;
        }
                /*if(users != null && users.ConfirmEmail(email)) {
                    result = true;
                }*/
        result = true;
        return result;
    }

    public boolean confirmNickname(String nickname) {
        boolean result = false;
        UserDO users = userDao.findByNickname1(nickname);
        log.info("service"+users);
        if(users == null) {
            return result;
        }
        else if(!users.ConFirmNickname(nickname)) {
            return result;
        }
                /*if(users != null && users.ConfirmEmail(email)) {
                    result = true;
                }*/
        result = true;
        return result;
    }

    public boolean confirmPhoneNumber(String phoneNumber) {
        boolean result = false;
        UserDO users = userDao.findByPhoneNumber(phoneNumber);

        if(users == null) {
            return result;
        }
        else if(!users.ConFirmPhoneNumber(phoneNumber)) {
            return result;
        }
                /*if(users != null && users.ConfirmEmail(email)) {
                    result = true;
                }*/
        result = true;
        return result;
    }

    public UserDO findByPhoneNumber(String phoneNumber) {
        return userDao.findByPhoneNumber(phoneNumber);
    }

    //0511- 손주현
    public void deleteUser(String email){
        userDao.deleteUserInfo(email);
    }
}
