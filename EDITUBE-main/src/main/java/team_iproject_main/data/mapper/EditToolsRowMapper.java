package team_iproject_main.data.mapper;

import org.springframework.jdbc.core.RowMapper;
import team_iproject_main.data.dto.PortfolioToolsDO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EditToolsRowMapper implements RowMapper<PortfolioToolsDO> {

    @Override
    public PortfolioToolsDO mapRow(ResultSet rs, int rowNum) throws SQLException {
        PortfolioToolsDO portfolioToolsDO = new PortfolioToolsDO();
        portfolioToolsDO.setEdittools(rs.getString("EDIT_TOOL"));
        return portfolioToolsDO;
    }
}
