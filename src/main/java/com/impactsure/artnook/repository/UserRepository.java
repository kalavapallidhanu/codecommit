package com.impactsure.artnook.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.impactsure.artnook.dto.CognitoUser;
import com.impactsure.artnook.dto.MasterContactDto;
import com.impactsure.artnook.dto.MasterDataDto;

@Repository
public class UserRepository {
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	final String UPDATE_QUERY = "update User_Login set logout_time = :logoutTime where session_id = :sessionId";

	public MasterContactDto findByEmail(String email) {
		MasterContactDto report = null;
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		String sql = " select mc.ID  contactId,MAIL_ID email,ifnull(mc.FIRST_NAME,'') contactFirstName,ifnull(mc.LAST_NAME,'') contactLastName,ifnull(mc.MIDDLE_NAME,'') contactMiddleName,mc.PROFILE_IMAGE imageCode from Master_Contact mc where mc.ISACTIVE=1";
		if (email != null && email.trim().length() > 0) {
			sql += " and MAIL_ID=:email ";
			paramSource.addValue("email", email);
		}
		try {
			report = (MasterContactDto) jdbcTemplate.queryForObject(sql, paramSource,
					new BeanPropertyRowMapper(MasterContactDto.class));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unable to get Contact");
		}
		return report;
	}

	public Integer update(CognitoUser cognitoUser) {
		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("logoutTime", cognitoUser.getLogoutTime()).addValue("sessionId", cognitoUser.getSessionId());
		return jdbcTemplate.update(UPDATE_QUERY, namedParameters);
	}

	public MasterDataDto fetchStaus(String mail) {
		MasterDataDto report = null;
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		String sql = "select ISACTIVE value FROM Master_Contact where MAIL_ID=:mail";
		paramSource.addValue("mail", mail);
		try {
			report = (MasterDataDto) jdbcTemplate.queryForObject(sql, paramSource,
					new BeanPropertyRowMapper(MasterDataDto.class));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unable to fetch Account Status");
		}
		return report;

	}

	public String findLastLogin(String email) {
		String report = "";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		String sql = " select convert_tz(TIMESTAMP(LOGIN_TIME,'%d/%m/%y'),'+00:00','-05:30') lastlogin FROM User_Login where USER_NAME=:userName ORDER BY LOGIN_TIME DESC LIMIT 1";
		if (email != null && email.trim().length() > 0) {

			paramSource.addValue("userName", email);
		}
		try {
			report = jdbcTemplate.queryForObject(sql, paramSource, String.class);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unable to get LastLogin");
		}
		return report;
	}

}
