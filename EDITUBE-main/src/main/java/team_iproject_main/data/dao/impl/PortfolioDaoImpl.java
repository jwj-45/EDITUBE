package team_iproject_main.data.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import team_iproject_main.data.dao.PortfolioDao;
import team_iproject_main.data.dto.PortfolioDO;
import team_iproject_main.data.dto.PortfolioEditDO;
import team_iproject_main.data.dto.PortfolioToolsDO;
import team_iproject_main.data.Mapper.*;

import java.util.List;

@Repository
public class PortfolioDaoImpl implements PortfolioDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PortfolioDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<PortfolioDO> PortfolioAll(int postsPerPage, int  offset) {
        return jdbcTemplate.query("SELECT * FROM (SELECT ROWNUM As rn, rb.* FROM" +
                "(SELECT p.*, u.nickname FROM PORTFOLIO p join USER_INFO u ON p.EDITOR_EMAIL = u.EMAIL WHERE p.IS_PUBLIC = 'TRUE' ORDER BY p.POST_DATE DESC) rb) WHERE rn BETWEEN ? AND ?"
                , new Object[]{offset + 1, offset + postsPerPage}, new PortfolioRowMapper());
    }

    @Override
    public int getTotalPosts() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM PORTFOLIO WHERE IS_PUBLIC = 'TRUE'", Integer.class);
    }

    @Override
    public PortfolioDO selectPortfolioPost(String email) {
        return jdbcTemplate.queryForObject("select p.*, u.nickname from portfolio p join user_info u on p.editor_email = u.email where p.EDITOR_EMAIL = ?", new PortfolioRowMapper(), email);
    }

    @Override
    public void portfolioupdate(PortfolioEditDO portfolioEditDO) {
        String sqlportfolio = "update PORTFOLIO set WORKABLE_LOCATION=?,MESSAGE=?,YOUTUBE_CAREER=?,OTHER_CAREER=?,PORTFOLIO_TITLE=?,DESIRED_SALARY=?," +
                "DESIRED_WORK_TYPE=?,MESSAGE_TO_YOUTUBER=?, IS_PUBLIC=? where EDITOR_EMAIL=?";
        jdbcTemplate.update(sqlportfolio, portfolioEditDO.getLocation(), portfolioEditDO.getMessage(),portfolioEditDO.getYOUTUBE_CAREER(), portfolioEditDO.getCareer(),
                portfolioEditDO.getTitle(), portfolioEditDO.getSalary()
                , portfolioEditDO.getWorktype(), portfolioEditDO.getToyoutuber(), portfolioEditDO.getIs_public() ,portfolioEditDO.getEdit_email());

        String sqltools = "INSERT INTO EDIT_TOOLS_LIST VALUES(EDIT_TOOLS_NO_SEQ.NEXTVAL, ?,?)";
        for (String tool : portfolioEditDO.getEdit_tools()) {
            jdbcTemplate.update(sqltools, portfolioEditDO.getEdit_email(), tool);
            //조회할때 가장 최근의 번호 가져오면됨
        }

        String sqlmainvideo = "INSERT INTO VIDEO_LINKS VALUES(VIDEO_LINKS_SEQ.NEXTVAL, ?, ?, 1)"; //대표영상저장
        jdbcTemplate.update(sqlmainvideo,portfolioEditDO.getEdit_email(),portfolioEditDO.getMain_link());
    }

    @Override
    public void portfolioInsert(PortfolioEditDO portfolioEditDO) {
        String sqlportfolio = "insert into PORTFOLIO (EDITOR_EMAIL, WORKABLE_LOCATION, MESSAGE, YOUTUBE_CAREER, OTHER_CAREER, PORTFOLIO_TITLE, DESIRED_SALARY, " +
                "DESIRED_WORK_TYPE, MESSAGE_TO_YOUTUBER, IS_PUBLIC) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlportfolio, portfolioEditDO.getEdit_email(), portfolioEditDO.getLocation(), portfolioEditDO.getMessage(),portfolioEditDO.getYOUTUBE_CAREER(), portfolioEditDO.getCareer(),
                portfolioEditDO.getTitle(), portfolioEditDO.getSalary()
                , portfolioEditDO.getWorktype(), portfolioEditDO.getToyoutuber(), portfolioEditDO.getIs_public());

        String sqltools = "INSERT INTO EDIT_TOOLS_LIST VALUES(EDIT_TOOLS_NO_SEQ.NEXTVAL, ?,?)";
        for (String tool : portfolioEditDO.getEdit_tools()) {
            jdbcTemplate.update(sqltools, portfolioEditDO.getEdit_email(), tool);
            //조회할때 가장 최근의 번호 가져오면됨
        }

        String sqlmainvideo = "INSERT INTO VIDEO_LINKS VALUES(VIDEO_LINKS_SEQ.NEXTVAL, ?, ?, 1)"; //대표영상저장
        jdbcTemplate.update(sqlmainvideo,portfolioEditDO.getEdit_email(),portfolioEditDO.getMain_link());

        String sqlisuploaded = "UPDATE USER_EDITOR SET IS_UPLOADED = 'TRUE' WHERE EDITOR_EMAIL = ?";
        jdbcTemplate.update(sqlisuploaded, portfolioEditDO.getEdit_email());

    }

    @Override
    public PortfolioEditDO selectPortfolioEdit(String email) throws EmptyResultDataAccessException {

        String sql = "SELECT ui.gender, ui.name,p.*\n" +
                "FROM user_info ui " +
                "JOIN portfolio p ON ui.email = p.editor_email " +
                "WHERE ui.email = ?";

        return jdbcTemplate.queryForObject(sql, new PortfolioEditRowMapper(), email);
    }

    @Override
    public void editlinkUpdate(String link,String email,int count){ //편집영상저장
        String sqleditlink="INSERT INTO VIDEO_LINKS " +
                "VALUES(VIDEO_LINKS_SEQ.NEXTVAL,? , ?, ?)";
        jdbcTemplate.update(sqleditlink,email,link,count);


    }

    @Override
    public void deletevideo(String email){
        String sqldelvideo = "Delete from video_links where EDITOR_EMAIL=?";
        jdbcTemplate.update(sqldelvideo,email);
    }

    @Override
    public void deletetools(String email){
        String sqldedeltool = "Delete from EDIT_TOOLS_LIST where EDITOR_EMAIL=?";
        jdbcTemplate.update(sqldedeltool,email);
    }

    @Override
    public void deletePortfolioPost(String email1) {
        jdbcTemplate.update("DELETE FROM portfolio WHERE EDITOR_EMAIL = ?", email1);
        jdbcTemplate.update("UPDATE USER_EDITOR SET IS_UPLOADED = 'FALSE' WHERE EDITOR_EMAIL = ?", email1);
    }

    @Override
    public List<PortfolioDO> FolioFinder(String folio_search_text, String location, String[] edit_tools_folio, int postsPerPage, int offset) {
        String sql = "select * from ( select ROWNUM rn, rb.* from (select distinct p.is_public, u.email, p.portfolio_title, u.nickname, p.post_date " +
                "from portfolio p " +
                "join user_info u on p.editor_email = u.email " +
                "join edit_tools_list t on p.editor_email = t.editor_email " +
                "where p.portfolio_title LIKE '%" + folio_search_text + "%' " +
                "and p.workable_location LIKE '%" + location + "%' ";
        if(edit_tools_folio.length !=0) {
            sql += "and t.edit_tool IN (";
            for(String tool : edit_tools_folio) {
                sql += "'" + tool + "',";
            }
            sql = sql.substring(0, sql.length() -1);
            sql += ")";
        }
        sql += ") rb ) where rn between ? and ?";

        return jdbcTemplate.query(sql, new Object[]{offset + 1, offset + postsPerPage}, new PortfolioFinderRowMapper());
    }

    @Override
    public int getTotalSearchPosts(String folio_search_text, String location, String[] edit_tools_folio) {
        String sql = "select COUNT(*) from (select distinct p.is_public, u.email, p.portfolio_title, u.nickname, p.post_date " +
                "from portfolio p " +
                "join user_info u on p.editor_email = u.email " +
                "join edit_tools_list t on p.editor_email = t.editor_email " +
                "where p.portfolio_title LIKE '%" + folio_search_text + "%' " +
                "and p.workable_location LIKE '%" + location + "%' ";
        if(edit_tools_folio.length !=0) {
            sql += "and t.edit_tool IN (";
            for(String tool : edit_tools_folio) {
                sql += "'" + tool + "',";
            }
            sql = sql.substring(0, sql.length() -1);
            sql += ")";
        }
        sql += ")";

        return jdbcTemplate.queryForObject(sql, Integer.class);
    }


    @Override
    public List<PortfolioToolsDO> getTools(String email) {
        String sql = "SELECT * FROM EDIT_TOOLS_LIST WHERE EDITOR_EMAIL = ?";
        return jdbcTemplate.query(sql, new EditToolsRowMapper(), email);
    }

    @Override
    public List<String> getVideoLinks(String email) {
        String sql = "SELECT video_link FROM VIDEO_LINKS WHERE EDITOR_EMAIL = ? order by PRIORITY";
        return jdbcTemplate.query(sql, new VideoLinksRowMapper(), email);

    }
}
