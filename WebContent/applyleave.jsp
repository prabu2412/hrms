<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
   <%@ page import="hrms.imlp.*" %>
   <%@page import = "java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Employee</title>
	<link rel="stylesheet" type="text/css" href="style.css">
	
</head>
<body >
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
		

		<div>
			<p style="font-size: 1.5em; font-weight: bold;" >Apply Leave </p>
		</div>

         <form action="PermissionManagementImpl" method = "POST">
		         	<div class="column" style="padding-left: 510px;">
		         	Leave Type :
		         	</div>
		         	<div class="column" style="padding-left: 28px;">
		         		<select name = "leavetype" style="padding: 0px 10px;">
		         		<%
		         		PermissionManagementImpl permissionManagementImpl = new PermissionManagementImpl();
		         		List<String> leaveTypeList = permissionManagementImpl.getAllLeaveType();
		         		for(int i=0;i<leaveTypeList.size();i++)
		         		{
		         		%>
		         		<option><%= leaveTypeList.get(i) %></option>
		         		<%} %>
		         		
		         		</select>
		         	</div> <br>
		  			<div align="center">
		         	Start Date :<input class = "btn start" type="Date" name="startdate" placeholder="Start Date" > <br>
		
		         	End Date   :<input class = "btn start" type="Date" name="enddate" placeholder="End Date" > <br>
		            </div>
		            <div style="padding:0px 609px">
		            	 <textarea name = "reason" placeholder="Reason" style=" width: 300px; height: 100px"></textarea>
		       		</div>
		
		             <div align="center" style="padding: 25px 0px;">
		              		 <input class="button button3" type="submit" value="Apply Leave" name="q" />
		             </div>
             </form>
	</div>

</body>
</html>