package com.ks.tests.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;


import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ks.tests.entries.DeviceInfo;
import com.ks.tests.entries.TestCases;



@Service("config")
@Transactional
public class ConfigDao {
	private JdbcTemplate jdbcTemplate;
	
	@Resource(name="dataSource")
	public void setDataSource(DataSource dataSource) {
	    this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String,String> getConfig(String sn){
		String sql="select * from deviceconfigs where deviceSn= "+sn;
		
	return	jdbcTemplate.execute(sql, new PreparedStatementCallback() {

			@Override
			public Object doInPreparedStatement(PreparedStatement p) throws SQLException, DataAccessException {
				ResultSet rs = p.executeQuery();
				Map<String, String> configMap = new HashMap<>();
				while (rs.next()) {
					configMap.put("deviceName", rs.getString("deviceName"));
					configMap.put("platformName", rs.getString("platformName"));
					configMap.put("platformVersion", rs.getString("platformVersion"));
					configMap.put("appPackage", rs.getString("appPackage"));
					configMap.put("appActivity", rs.getString("appActivity"));
					configMap.put("deviceUrl", rs.getString("deviceUrl"));
					configMap.put("deviceSn", rs.getString("deviceSn"));
				}
				return configMap;
			}
	
		});
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map<String,String>> getAllConfigs(){
		String sql="select * from deviceconfigs ";
		
		return	jdbcTemplate.execute(sql, new PreparedStatementCallback() {

				@Override
				public Object doInPreparedStatement(PreparedStatement p) throws SQLException, DataAccessException {
					ResultSet rs = p.executeQuery();
					List<Map<String,String>> list=new ArrayList();
					while (rs.next()) {
						Map<String, String> configMap = new HashMap<>();
						configMap.put("deviceName", rs.getString("deviceName"));
						configMap.put("platformName", rs.getString("platformName"));
						configMap.put("platformVersion", rs.getString("platformVersion"));
						configMap.put("appPackage", rs.getString("appPackage"));
						configMap.put("appActivity", rs.getString("appActivity"));
						configMap.put("deviceUrl", rs.getString("deviceUrl"));
						configMap.put("deviceSn", rs.getString("deviceSn"));
						list.add(configMap);
					}
					return list;
				}
		
			});
	}
	
	public List<DeviceInfo> getAllDeviceInfos(){
		String sql="select * from deviceconfigs ";
		List<DeviceInfo> list=jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(DeviceInfo.class));
		return list;
	}
	
}
