<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
   <%@page import="java.time.LocalDate" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Attendance</title>
<link rel="stylesheet" type="text/css" href="style.css">
</head>
<body>
		<div>
			<div>
				<nav>
				
				<p align="center"><img src="Doctologo.png" width="170px" ></p>
					<div class = 'link'>
                 		  <li ><a href="employeeprofile.jsp">Home</a></li>
                 		 <li ><a href="applyleave.jsp">Apply Leave</a></li>
                 		 <li ><a href="attendance.jsp">Attendance</a></li>
                 		 
                  		 <li style="float: right;"><a href="index.html" >Log out</a></li>

					</div>
			</nav>
			</div>
			<div>
			<% LocalDate localDate = LocalDate.now(); %>
				<p style="font-size: 2em;"> Attendance for <%= localDate%></p>
			</div>
			<form action="attendance" method="POST" style=" padding-top: 100px">
			<div class="column" style="padding-left: 500px;   ">
				<input type="submit" name="time" value="In Time" class="button5">
			</div>
			<div class="column" style="padding-left: 100px;   ">
				<input type="submit" name="time" value="Out Time" class="button5">
			</div>
			</form>
		</div>

</body>
</html>