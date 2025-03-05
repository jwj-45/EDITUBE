package team_iproject_main.data.dao;

import team_iproject_main.data.dto.ApplierListDO;
import team_iproject_main.data.dto.ApplyEditorDO;
import team_iproject_main.data.dto.UserEditorDO;

import java.util.List;

public interface ApplyEditorDao {

    void addApplyEditor(int recruitNO, String email);

    ApplyEditorDO findApplyEditorByNumAndEmail(int recruitNO, String email);

    void updateApplyEditor(int recruitNo, String editor_email, String edited_link, String editor_memo);

    void deleteApplyEditor(int recruitNo, String email);

    List<ApplierListDO> myRecruitApplierList(int recruitNo, int postsPerPage, int offset) ;

    int getTotalApplier(int recruitNo) ;

    UserEditorDO findEditor(String email);

    ApplyEditorDO checkApplierVideo(String editor_email, int recruit_no);

    void updateYoutuberMemo(String youtuber_memo, int apply_no);
}
