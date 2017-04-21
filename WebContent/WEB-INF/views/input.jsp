<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">
	table,table tr th{ 
	padding-bottom:20px;
	padding-left:20px;
	padding-right:20px;
	padding-top:20px;
	border:0px ; }
	table tr td{
		border:2px solid#474747; 
		padding-bottom:10px;
	padding-left:2px;
	padding-right:5px;
	padding-top:10px;
	border: 0px;
	}
	table{
		text-align: center;
		border-collapse: collapse;
		size: 12px;
	}
	
	.descinput {
	height: 100px;
	padding: 5px 0px 0px 5px;
	width: 150%;
	
	}
</style>
<title>卡说---增加测试用例</title>
</head>
<body >
	<br/>
	<br/>
	<h4 align="center">增加测试用例</h4>
	<br/>
	<br/>

	<form:form action="${pageContext.request.contextPath }/caser" method="POST" modelAttribute="testcase" >
		<!-- path 属性对应html表单标签的name属性 -->
		      <table style="margin-left: auto;margin-right: auto;">
    	      	<tr>
    	      	 <td>用例编号</td>
    	      	 <td><form:input path="caseId" style="height:35px;" size="30"/>
    	      	</tr>
   
    	      	<tr>
    	      	 <td>用例名称</td>
    	      	 <td><form:input path="caseName" style="height:35px;" size="30"/>
    	      	</tr>
   
    	      	<tr>
    	      	 <td>操作步骤</td>
    	      	 <td><form:textarea path="caseAction" cssClass="descinput"/>
    	      	</tr>
  
    	      	<tr>
    	      	 <td>预期结果</td>
    	      	 <td><form:textarea path="caseExpect" cssClass="descinput"/>
    	      	</tr>

    	      	<tr>
    	      	 <td>备注描述<br/>(提供的手<br/>动测试用<br/>例的检查点)</td>
    	      	 <td><form:textarea path="testDesc" class="descinput"/>
    	      	</tr>
  
    	      	<tr>
    	      	<td colspan="2"><input type="submit" value="确 定" style="font-size: 20px;font-style: italic;"/>
    	      	</tr>
    	      </table>   	 
	</form:form>
</body>
</html>