package team_iproject_main.data.dao;

import team_iproject_main.data.dto.PortfolioDO;
import team_iproject_main.data.dto.PortfolioEditDO;
import team_iproject_main.data.dto.PortfolioToolsDO;

import java.util.List;

public interface PortfolioDao {

    List<PortfolioDO> PortfolioAll(int postsPerPage, int  offset);

    int getTotalPosts();

    PortfolioDO selectPortfolioPost(String email);

    void portfolioupdate(PortfolioEditDO portfolioEditDO);

    void portfolioInsert(PortfolioEditDO portfolioEditDO);

    PortfolioEditDO selectPortfolioEdit(String email);

    void editlinkUpdate(String link,String email,int count);

    void deletevideo(String email);

    void deletetools(String email);

    void deletePortfolioPost(String email1);

    List<PortfolioDO> FolioFinder(String folio_search_text, String location, String[] edit_tools_folio, int postsPerPage, int offset);

    int getTotalSearchPosts(String folio_search_text, String location, String[] edit_tools_folio);

    List<PortfolioToolsDO> getTools(String email);

    List<String> getVideoLinks(String email);
}
