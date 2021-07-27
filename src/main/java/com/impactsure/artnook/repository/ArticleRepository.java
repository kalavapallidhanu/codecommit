package com.impactsure.artnook.repository;

/*
 * Created By Gufran
 */
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import com.impactsure.artnook.dto.ArticleDto;

@Repository
public class ArticleRepository {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	final String Update_QUERY ="update Announcements set DisplayOrder = DisplayOrder+1  where category =:category and DisplayOrder  >=:DisplayOrder";
	final String UPDATE_INCREMENT = "update Announcements set DisplayOrder = DisplayOrder+1  where category =:category and DisplayOrder  BETWEEN :DisplayOrder AND :oldDispaly ";
	final String UPDATE_DECREMENT = "update Announcements set DisplayOrder = DisplayOrder-1  where category =:category and DisplayOrder  BETWEEN :oldDispaly AND :DisplayOrder";
	final String DELETE_QUERY = "update Announcements set ISACTIVE = 0  where ID =:ID" ;
	/*
	 * Added By Gufran
	 * Fetch Category Name for Dropdown in NewArticle page
	 */
	public List<ArticleDto> getArticleId() {
		List<ArticleDto> report = null;
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		String sql = "SELECT Distinct id,name title  FROM Master_Announcements where ISActive=1 order by id";
		try {
			report = jdbcTemplate.query(sql, paramSource, new BeanPropertyRowMapper(ArticleDto.class));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unable to get Announcement Category Id");
		}
		return report;

	}

	/*
	 * Added By Gufran 
	 * Update orderSequence by 1 Based on Entered OrderSequence
	 */
	public Integer updateIncrement(ArticleDto article,Integer oldDispaly) {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("category", article.getCategory())
				.addValue("DisplayOrder", article.getDisplayOrder()).addValue("oldDispaly", oldDispaly);
		return jdbcTemplate.update(UPDATE_INCREMENT, namedParameters);
	}
	public Integer updateDecrement(ArticleDto article, Integer oldDispaly) {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("category", article.getCategory())
				.addValue("DisplayOrder", article.getDisplayOrder()).addValue("oldDispaly", oldDispaly);
		return jdbcTemplate.update(UPDATE_DECREMENT, namedParameters);
	}
	public Integer update(ArticleDto article) {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("category", article.getCategory())
				.addValue("DisplayOrder", article.getDisplayOrder());
		return jdbcTemplate.update(Update_QUERY, namedParameters);
	}
	public Integer delete(Long id) {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("ID",id);
		return jdbcTemplate.update(DELETE_QUERY, namedParameters);
	}
	
	
	public List<ArticleDto> getArticleByCategoryId(Integer category) {
		List<ArticleDto> report = null;
		MapSqlParameterSource paramSource = new MapSqlParameterSource().addValue("category", category);
		String sql = "SELECT  ID id,TITLE title,SUBTITLE subTitle,CASE WHEN ISACTIVE= 1 THEN 'Active' else 'InActive' end activeDetails,DISPLAYORDER displayOrder  FROM Announcements where CATEGORY =:category   order by activeDetails,displayOrder";
		try {
			report = jdbcTemplate.query(sql, paramSource, new BeanPropertyRowMapper(ArticleDto.class));
			System.out.println(report.toArray());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unable to get Announcement By Category Id");
		}
		return report;

	}

	
	
	

}

