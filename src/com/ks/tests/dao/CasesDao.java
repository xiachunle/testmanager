package com.ks.tests.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ks.tests.entries.TestCases;


@Service("testcase")
@Transactional
public class CasesDao {
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

	// 增
	public void addTest(TestCases caser) {
		String sql = "insert into testcases(caseId,caseName,caseAction,caseExpect,caseActual,caseTester,testResult)"
				+ " values(?,?,?,?,?,?,?)";
		Object[] params = new Object[] { caser.getCaseId(),caser.getCaseName(),caser.getCaseAction(),
				caser.getCaseExpect(),caser.getCaseActual(),caser.getCaseTester(),caser.getTestResult() };
		jdbcTemplate.update(sql, params);
	}

	// 删
	public void deleterTest(int caseId) {
		String sql = "delete from testcases where caseId=?";
		Object [] parameters=new Object []{caseId};
		jdbcTemplate.update(sql, parameters);
	}

	// 改
	public void updateTest(TestCases casers) {
		
		String sql = "update  testcases set caseName=?,caseAction=?,caseExpect=?,caseTester=? where caseId=?";
		Object[] params = new Object[] {casers.getCaseName(),casers.getCaseAction(),casers.getCaseExpect(),casers.getCaseTester(),casers.getCaseId()};
		jdbcTemplate.update(sql, params);
	}
	//查
	public TestCases getTest(int id){
		String sql="select * from testcases where caseId= "+id; 
//		System.out.println("查找sql语句:"+sql);
		TestCases casers=jdbcTemplate.queryForObject(sql,BeanPropertyRowMapper.newInstance(TestCases.class));
//		System.out.println("*****"+casers.toString());
		return casers;
	}
	
	
	//获取所有数据
	public List<TestCases> queryAll(){
		String sql="select * from testcases";
		List<TestCases> list=jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(TestCases.class));
		return list;
	}
	
}
