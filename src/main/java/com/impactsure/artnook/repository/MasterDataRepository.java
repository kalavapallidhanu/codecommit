package com.impactsure.artnook.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.impactsure.artnook.dto.MasterDataDto;
import com.impactsure.artnook.dto.ResetPasswordDto;
import com.impactsure.artnook.entity.Customer;

@Repository
public class MasterDataRepository {
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	
	
	public List<MasterDataDto> getUserType() {
		List<MasterDataDto> report = null;
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		String sql = "SELECT Distinct ID value,NAME label  FROM Master_User_Type where ACTIVE=1";
		try {
			report = jdbcTemplate.query(sql, paramSource, new BeanPropertyRowMapper(MasterDataDto.class));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unable to get getUserType");
		}
		return report;

	}
	
	
	public Customer fetchContactDetails(String id) {
		Customer report = null;
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		String sql = "select ID id,USER_GROUP userGroup,PROFILE_IMAGE imageCode FROM Master_Contact where ID=:id";
		paramSource.addValue("id",id);
		try {
			report = (Customer) jdbcTemplate.queryForObject(sql, paramSource, new BeanPropertyRowMapper(Customer.class));
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("Unable to fetchContactDetails");
		}
		return report;
		
	}
	
	public Customer findByMailId(String mail) {
		Customer report = null;
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		String sql = "select * FROM Master_Contact where MAIL_ID=:mail";
		paramSource.addValue("mail",mail);
		try {
			report = (Customer) jdbcTemplate.queryForObject(sql, paramSource, new BeanPropertyRowMapper(Customer.class));
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("Unable to fetchContactDetails");
		}
		return report;
		
	}
	
	
	public Customer fetchContactDetailsByID(String id) {
		Customer report = null;
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		String sql = "select ID id,FIRST_NAME firstName,MIDDLE_NAME middleName,LAST_NAME lastName,GENDER gender,BLOOD_GROUP bloodGroup,DOB dob,PROFILE_IMAGE imageCode,CLASS className,DIVISION divisionName,BRANCH branch,MOBILE_NO mobileNo,MAIL_ID mailId,COUNTRY_CODE countryCode,STATUS status,USER_GROUP userGroup,ISACTIVE isActive FROM Master_Contact where ID=:id";
		paramSource.addValue("id",id);
		try {
			report = (Customer) jdbcTemplate.queryForObject(sql, paramSource, new BeanPropertyRowMapper(Customer.class));
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("Unable to fetchContactDetails");
		}
		return report;
		
	}
	
	public MasterDataDto fetchGroupName(Long id) {
		MasterDataDto report = null;
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		String sql = "select ID value,VALUE label FROM Master_User_Type where ACTIVE=1 AND ID=:id";
		paramSource.addValue("id",id);
		try {
			report = (MasterDataDto) jdbcTemplate.queryForObject(sql, paramSource, new BeanPropertyRowMapper(MasterDataDto.class));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unable to fetchGroupName");
		}
		return report;
		
	}
	
	public MasterDataDto fetchUserGrpValue(Long id) {
		MasterDataDto report = null;
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		String sql = "select NAME label,VALUE name FROM Master_User_Type where ACTIVE=1 AND ID=:id";
		paramSource.addValue("id",id);
		try {
			report = (MasterDataDto) jdbcTemplate.queryForObject(sql, paramSource, new BeanPropertyRowMapper(MasterDataDto.class));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unable to fetchUserGrpValue");
		}
		return report;
		
	}
	
	
	public ResetPasswordDto fetchMailDetails(String mail) {
		ResetPasswordDto report = null;
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		String sql = "select contact.FIRST_NAME firstName,contact.MAIL_ID contactMailId,user.VALUE groupName FROM Master_Contact contact,Master_User_Type user where contact.MAIL_ID=:mail and contact.USER_GROUP=user.ID";
		paramSource.addValue("mail",mail);
		try {
			report = (ResetPasswordDto) jdbcTemplate.queryForObject(sql, paramSource, new BeanPropertyRowMapper(ResetPasswordDto.class));
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("Unable to fetchContacDetails");
		}
		return report;
		
	}
	
	
	public ResetPasswordDto fetchResetDetails(String id) {
		ResetPasswordDto report = null;
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		String sql = "select contact.FIRST_NAME firstName,contact.MAIL_ID contactMailId,user.VALUE groupName FROM Master_Contact contact,Master_User_Type user where contact.ID=:id and contact.USER_GROUP=user.ID";
		paramSource.addValue("id",id);
		try {
			report = (ResetPasswordDto) jdbcTemplate.queryForObject(sql, paramSource, new BeanPropertyRowMapper(ResetPasswordDto.class));
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("Unable to fetchContacDetails");
		}
		return report;
		
	}
	
}
