<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>卡说 --- 测试管理</title>
<script type="text/javascript" src="scripts/jquery.min.js"></script>
<script type="text/javascript">
    $(function(){
        $(".delete").click(function(){
            var href = $(this).attr("href");
            $("form").attr("action", href).submit();
            return false;
        });
    })
</script>
<style type="text/css">
table, table tr th {
	padding-bottom: 20px;
	padding-left: 20px;
	padding-right: 20px;
	padding-top: 20px;
	border: 2px solid #474747;
}

table tr td {
	border: 2px solid #474747;
	padding-bottom: 10px;
	padding-left: 10px;
	padding-right: 10px;
	padding-top: 10px;
}

table {
	text-align: center;
	border-collapse: collapse;
	size: 12px;
}

.actionSpan {
	text-align: center;
	float: left;
}
</style>
</head>
<body>
	<form action="" method="POST">
		<input type="hidden" name="_method" value="DELETE" />
	</form>
	<h3 align="center">
		<script type="text/javascript">
	</script>
		POS自动化测试用例
	</h3>
	<br />
	<br />
	<a href="caser" style="padding-left: 30%">增加测试用例</a>
	<a href="appium" style="padding-left: 20px">启动Appium服务</a>
	<a href="action" style="padding-left: 20px">执行用例</a>
	<br />
	<br />
	<div class="actionSpan">
		POS机行为操作列表 <br /> <br />

		<label for="category">Category: </label>
            <form:select id="category" path="category.id" 
                items="${categories}" itemLabel="name" itemValue="id"/>
		<br /> <br />

		<li><a href="card">快速刷卡</a></li> <br /> <br />
		<li><a href="bank">银行活动</a></li> <br /> <br />
		<li><a href="store">商户储值</a></li> <br /> <br />
		<li><a href="member">会员结账</a></li> <br /> <br />
		<li><a href="revoke">交易撤销</a></li> <br />
	</div>


	<c:if test="${empty requestScope.testcase}">
		沒有测试用例请增加！
    </c:if>
	<c:if test="${!empty requestScope.testcase }">
		<div style="overflow-y: auto; height: 90%">
			<table style="margin-left: auto; margin-right: auto; width: 70%">
				<tr>
					<th>用例编号</th>
					<th>用例名称</th>
					<th>用例执行</th>
					<th>预期结果</th>
					<th>测试人员</th>
					<th colspan="2">操作</th>
				</tr>
				<c:forEach items="${requestScope.testcase}" var="caser">
					<tr>
						<td>${caser.caseId }</td>
						<td>${caser.caseName }</td>
						<td>${caser.caseAction }</td>
						<td>${caser.caseExpect }</td>
						<td>${caser.caseTester}</td>
						<td><a href="caser/${caser.caseId}">修改</a></td>
						<td><a href="caser/delete/${caser.caseId}">删除</a></td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</c:if>
	<br>
	<br>



</body>
</html>