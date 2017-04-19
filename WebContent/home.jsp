<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>卡说 --- 测试管理</title>
<style type="text/css">
.dark-matter {
	margin-left: auto;
	margin-right: auto;
	max-width: 500px;
	background: #555;
	padding: 20px 30px 20px 30px;
	font: 12px "Helvetica Neue", Helvetica, Arial, sans-serif;
	color: #D3D3D3;
	text-shadow: 1px 1px 1px #444;
	border: none;
	border-radius: 5px;
	-webkit-border-radius: 5px;
	-moz-border-radius: 5px;
}

.dark-matter h1 {
	padding: 0px 0px 10px 40px;
	display: block;
	border-bottom: 1px solid #444;
	margin: -10px -30px 30px -30px;
}

.dark-matter h1>span {
	display: block;
	font-size: 11px;
}

.dark-matter label {
	display: block;
	margin: 0px 0px 5px;
}

.dark-matter label>span {
	float: left;
	width: 20%;
	text-align: right;
	padding-right: 10px;
	margin-top: 10px;
	font-weight: bold;
}

.dark-matter input[type="text"], .dark-matter input[type="password"],
	.dark-matter textarea, .dark-matter select {
	border: none;
	color: #525252;
	height: 25px;
	line-height: 15px;
	margin-bottom: 16px;
	margin-right: 6px;
	margin-top: 2px;
	outline: 0 none;
	padding: 5px 0px 5px 5px;
	width: 70%;
	border-radius: 2px;
	-webkit-border-radius: 2px;
	-moz-border-radius: 2px;
	-moz-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075);
	background: #DFDFDF;
}

.dark-matter select {
	background: #DFDFDF url('down-arrow.png') no-repeat right;
	background: #DFDFDF url('down-arrow.png') no-repeat right;
	appearance: none;
	-webkit-appearance: none;
	-moz-appearance: none;
	text-indent: 0.01px;
	text-overflow: '';
	width: 70%;
	height: 35px;
	color: #525252;
	line-height: 25px;
}

.dark-matter textarea {
	height: 100px;
	padding: 5px 0px 0px 5px;
	width: 70%;
}

.dark-matter .button {
	background: #FFCC02;
	border: none;
	padding: 10px 25px 10px 25px;
	color: #585858;
	border-radius: 4px;
	-moz-border-radius: 4px;
	-webkit-border-radius: 4px;
	text-shadow: 1px 1px 1px #FFE477;
	font-weight: bold;
	box-shadow: 1px 1px 1px #3D3D3D;
	-webkit-box-shadow: 1px 1px 1px #3D3D3D;
	-moz-box-shadow: 1px 1px 1px #3D3D3D;
}

.dark-matter .button:hover {
	color: #333;
	background-color: #EBEBEB;
}
</style>
</head>
<body style="background-color: #FFF;">
	<!-- <a href="casers" style="">显示所有测试用例/添加登录界面</a><br/> -->

	<br />
	<br />
	<br />
	<form action="${pageContext.request.contextPath }/login" method="POST" class="dark-matter">
		<h1 align="center">自 动 化 测 试 平 台</h1>
		<br> <br> <label> <span>测 试 账 号</span> <input
			id="username" type="text" name="username" placeholder="账号" />
		</label> <label> <br> <br> <span> 密 码 </span> <input
			id="password" type="password" name="password" placeholder="输入密码" />
		</label> <br> <br>
		<!--
<label>
<span>Message :</span>
<textarea id="message" name="message" placeholder="Your Message to Us"></textarea>
</label>
<label>
<span>Subject :</span><select name="selection">
<option value="Job Inquiry">Job Inquiry</option>
<option value="General Question">General Question</option>
</select>
</label>
-->
		<label align="center"> </span> <input type="submit"
			class="button" value="登录" />
		</label>
	</form>
</body>
</html>