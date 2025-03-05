package team_iproject_main.data.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import team_iproject_main.data.dao.ApplyEditorDao;
import team_iproject_main.data.dto.ApplierListDO;
import team_iproject_main.data.dto.ApplyEditorDO;
import team_iproject_main.data.dto.UserEditorDO;
import team_iproject_main.data.Mapper.ApplierListRowMapper;
import team_iproject_main.data.Mapper.ApplyEditorRowMapper;
import team_iproject_main.data.Mapper.EditorRowMapper;

import java.util.List;

@Repository
public class ApplyEditorDaoImpl implements ApplyEditorDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void addApplyEditor(int recruitNO, String email){
        String sql = "INSERT INTO APPLY_EDITOR(APPLY_NO, RECRUIT_NO, EDITOR_EMAIL) VALUES(APPLY_EDITOR_SEQ.NEXTVAL,?,?)";
        jdbcTemplate.update(sql,recruitNO,email);
    }

    @Override
    public ApplyEditorDO findApplyEditorByNumAndEmail(int recruitNO, String email){
        String sql = "SELECT * FROM APPLY_EDITOR WHERE RECRUIT_NO = ? AND EDITOR_EMAIL = ?";
        ApplyEditorDO applyEditorDO = null;
        try{
            applyEditorDO = jdbcTemplate.queryForObject(sql, new ApplyEditorRowMapper(), recruitNO, email);
            return  applyEditorDO;
        }
        catch(EmptyResultDataAccessException e){
            return null;
        }

    }

    @Override
    public void updateApplyEditor(int recruitNo, String editor_email, String edited_link, String editor_memo){
        String sql = "UPDATE APPLY_EDITOR SET EDITED_LINK = ?, EDITOR_MEMO = ? WHERE RECRUIT_NO = ? AND EDITOR_EMAIL = ?";
        jdbcTemplate.update(sql, edited_link, editor_memo, recruitNo, editor_email);
    }

    @Override
    public void deleteApplyEditor(int recruitNo, String email){
        String sql = "DELETE FROM APPLY_EDITOR WHERE RECRUIT_NO = ? AND EDITOR_EMAIL = ?";
        jdbcTemplate.update(sql, recruitNo, email);
    }

    @Override
    public List<ApplierListDO> myRecruitApplierList(int recruitNo, int postsPerPage, int offset) {

        String sql = "SELECT * FROM (SELECT ROWNUM AS rn, a.* FROM (SELECT E.RECRUIT_NO, P.PORTFOLIO_TITLE, U.NICKNAME, U.EMAIL " +
                "FROM APPLY_EDITOR E JOIN USER_INFO U ON E.EDITOR_EMAIL = U.EMAIL JOIN PORTFOLIO P ON U.EMAIL = P.EDITOR_EMAIL " +
                "WHERE E.RECRUIT_NO = ? ORDER BY E.RECRUIT_NO DESC) a) WHERE rn BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, new Object[]{recruitNo, offset + 1, offset + postsPerPage}, new ApplierListRowMapper());
    }

    @Override
    public int getTotalApplier(int recruitNo) {
        String sql = "SELECT COUNT(*) FROM (SELECT E.RECRUIT_NO, P.PORTFOLIO_TITLE, U.NICKNAME, U.EMAIL " +
                "FROM (APPLY_EDITOR E) JOIN (USER_INFO U) ON (E.EDITOR_EMAIL LIKE U.EMAIL) JOIN PORTFOLIO P ON (U.EMAIL LIKE P.EDITOR_EMAIL) " +
                "WHERE E.RECRUIT_NO = ? )";
        return jdbcTemplate.queryForObject(sql, Integer.class, recruitNo);
    }

    @Override
    public UserEditorDO findEditor(String email){
        String sql = "SELECT * FROM USER_EDITOR WHERE EDITOR_EMAIL = ?";
        return jdbcTemplate.queryForObject(sql, new EditorRowMapper(), email);
    }

    @Override
    public ApplyEditorDO checkApplierVideo(String editor_email, int recruit_no) {
        String sql = "SELECT * FROM APPLY_EDITOR WHERE EDITOR_EMAIL = ? AND RECRUIT_NO = ?";
        return jdbcTemplate.queryForObject(sql, new ApplyEditorRowMapper(), editor_email, recruit_no);
    }

    @Override
    public void updateYoutuberMemo(String youtuber_memo, int apply_no){
        String sql = "UPDATE APPLY_EDITOR SET YOUTUBER_MEMO = ? WHERE APPLY_NO = ?";
        jdbcTemplate.update(sql, youtuber_memo, apply_no);
    }
}
