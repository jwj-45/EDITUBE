package team_iproject_main.data.dao;

import team_iproject_main.data.request.RequestKeyword;
import team_iproject_main.data.dto.*;
import java.util.List;

public interface RecruitBoardDao {

    void createRecruitBoard(RecruitBoardDO recruitDO);

    RecruitDO selectRecruitPost(int recruitNo);

    YoutuberDO selectYoutuberInfo(int recruitNo);

    List<RecruitDO> findAllApplyByEmail(String email, int postsPerPage, int offset);

    int getTotalApply(String email) ;

    List<ChannelCategoryDO> getCategories(int recruitNo);

    List<EditToolsRecruitDO> getTools(int recruitNo);

    void modifyRecruitBoard(RecruitBoardDO recruitDO , int recruitNo, String email);

    List<RecruitSearchDO> findRecruit(int postsPerPage, int offset);

    int getTotalPosts();

    void deleteRecruitPost(int recruitNo);

    List<RecruitSearchDO> SearchFinder(RequestKeyword keywordDO, int postsPerPage, int offset);

    int getSearchTotalPosts(RequestKeyword keywordDO);

    List<MyRecruitDO> findMyRecruit(String youtuber_email, int postsPerPage, int offset);

    int getTotalRecruits(String youtuber_email);
}
