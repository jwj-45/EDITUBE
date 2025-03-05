package team_iproject_main.data.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import team_iproject_main.data.dao.QnADao;
import team_iproject_main.data.dto.QnADO;
import team_iproject_main.data.mapper.QnARowMapper;

import java.util.List;


@Repository
public class QnADaoImpl implements QnADao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<QnADO> findAll(int postsPerPage, int offset) {
        return jdbcTemplate.query("SELECT * FROM (SELECT ROWNUM AS rn, a.* FROM (SELECT q.*, u.nickname FROM qna q join user_info u on q.email = u.email ORDER BY qna_no DESC) a ) WHERE rn BETWEEN ? AND ?", new Object[]{offset + 1, offset + postsPerPage} , new QnARowMapper());
    }

    @Override
    public int getTotalQna() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM qna", Integer.class);
    }

    @Override
    public void qnaUpdate(String email, String question, String answer) {
        jdbcTemplate.update("INSERT INTO QNA (QNA_NO, EMAIL, QUESTION, ANSWER) VALUES (QNA_NO_SEQ.NEXTVAL,?, ? ,?)", email, question, answer);
    }

    @Override
    public void qnaAnswer(int qnaNo, String answer) {
        jdbcTemplate.update("UPDATE QNA SET ANSWER = ? WHERE QNA_NO = ?", answer, qnaNo);
    }
}
