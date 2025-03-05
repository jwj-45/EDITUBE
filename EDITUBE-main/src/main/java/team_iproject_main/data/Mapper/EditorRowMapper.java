package team_iproject_main.data.Mapper;

import org.springframework.jdbc.core.RowMapper;
import team_iproject_main.data.dto.UserEditorDO;

import java.sql.ResultSet;
import java.sql.SQLException;


public class EditorRowMapper implements RowMapper<UserEditorDO> {
    @Override
    public UserEditorDO mapRow(ResultSet rs, int rowNum) throws SQLException{
        UserEditorDO userEditorDO = new UserEditorDO();
        userEditorDO.setEDITOR_EMAIL(rs.getString("EDITOR_EMAIL"));
        userEditorDO.setIS_UPLOADED(rs.getString("IS_UPLOADED"));

        return userEditorDO;
    }
}
