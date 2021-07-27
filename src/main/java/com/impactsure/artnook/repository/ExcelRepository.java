package com.impactsure.artnook.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.impactsure.artnook.dto.CustomerData;

@Repository
public class ExcelRepository {
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	public List<CustomerData> getEmployeeAllDetails() {
		List<CustomerData> report=null;
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		String sql=	"select cont.ID as id, cont.FIRST_NAME as firstName, cont.LAST_NAME as lastName, cont.MIDDLE_NAME as middleName,CASE WHEN ISNULL(date_format(convert_tz(TIMESTAMP(LOGIN_TIME,'%d-%m-%Y'),'+00:00','+05:30'),'%d-%m-%Y %H:%i'))THEN '-' else date_format(convert_tz(TIMESTAMP(LOGIN_TIME,'%d-%m-%Y'),'+00:00','+05:30'),'%d-%m-%Y %H:%i') end as loginTime,cont.GENDER as gender, date_format(cont.DOB, '%d-%m-%Y') as dob, cont.BLOOD_GROUP as bloodGroup, cont.MOBILE_NO as mobileNumber,cont.MAIL_ID mailId,cont.CLASS as className, cont.DIVISION as division,cont.BRANCH as branch, userGroup.NAME userGroup, CASE WHEN cont.ISACTIVE = 1 THEN'Active' else 'In Active' end as status from Master_User_Type userGroup,Master_Contact cont left join User_Login login on cont.MAIL_ID = login.USER_NAME and login.LOGIN_TIME IN( SELECT MAX(login1.LOGIN_TIME) FROM User_Login login1 GROUP BY login1.USER_NAME ) where cont.USER_GROUP = userGroup.ID";
		try {
			 report=(List<CustomerData>) jdbcTemplate.query(sql, paramSource, new BeanPropertyRowMapper(CustomerData.class));
		}catch (Exception e) {
			
				e.printStackTrace();
				System.out.println("Unable to get Customer Details");
			}
			return report;
	}
	
	
	 
}
