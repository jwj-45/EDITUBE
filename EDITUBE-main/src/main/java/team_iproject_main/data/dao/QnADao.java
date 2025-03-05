package team_iproject_main.data.dao;

import team_iproject_main.data.dto.QnADO;
import java.util.List;

public interface QnADao {

    List<QnADO> findAll(int postsPerPage, int offset);

    int getTotalQna();

    void qnaUpdate(String email, String question, String answer);

    void qnaAnswer(int qnaNo, String answer);
}
