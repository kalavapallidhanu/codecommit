package com.impactsure.artnook.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import com.impactsure.artnook.dto.MasterProfileDto;

@Repository
public class ProfileRespository {
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	 final String UPDATE_QUERY = "update Master_Contact set PROFILE_IMAGE = :Image_Code,MOBILE_NO = :Mobile_No  where ID = :Contact_ID ";

	public MasterProfileDto findByID(String id) {
		MasterProfileDto report=null;
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		String sql=	"select mc.ID id,mc.FIRST_NAME firstName,mc.MIDDLE_NAME middleName,mc.LAST_NAME lastName,mc.CLASS className,mc.DIVISION divisionName,mc.BRANCH branch,mc.MAIL_ID email,mc.MOBILE_NO mobileNo,mc.PROFILE_IMAGE image,mc.BLOOD_GROUP bloodGroup,date_format(mc.DOB,'%d/%m/%y') dob from Master_Contact mc";
			if(id!=null) {
				sql+=" where mc.ID=:id ";
				paramSource.addValue("id",id);
			}
			try {
				 report=(MasterProfileDto) jdbcTemplate.queryForObject(sql, paramSource, new BeanPropertyRowMapper(MasterProfileDto.class));
				 System.out.println(report.toString());
			}catch (Exception e) {
				e.printStackTrace();
				System.out.println("Unable to get Contact");
			}
			return report;
	}
	
	
	 public Integer update(MasterProfileDto cust) {
	        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("Image_Code", cust.getImage()).addValue("Mobile_No", cust.getMobileNo()).addValue("Contact_ID", cust.getId());
	        return  jdbcTemplate.update(UPDATE_QUERY, namedParameters); 
	    }
}
