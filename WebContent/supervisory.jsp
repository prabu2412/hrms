<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
       <%@ page import="hrms.imlp.*" %>
       <%@ page import="hrms.*" %>
   <%@page import = "java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>HR</title>
	<link rel="stylesheet" type="text/css" href="style.css">
</head>
<body >
	<div>
      		<nav>
				<p align="center"><img src="Doctologo.png" width="170px" ></p>
					<div class = 'link' >
                 		 <li><a href="supervisory.jsp">Home</a></li>
                 		 <li style="float: right"><a href="index.html">Log out</a></li>

					</div>
			</nav>

     
		
	</div >
	<div>
	<% String supervisoryNumber = (String)session.getAttribute("supervisoryName"); 
	
		 SupervisoryImpl supervisoryImpl = new SupervisoryImpl();
		 String supervisoryName = supervisoryImpl.getSupervisoryName(supervisoryNumber);%>

   <p style=" font-size: 1.5em; font-weight: bold;" > Welcome <%=supervisoryName %></p>
	</div>
	<div>

   <p style=" font-size: 1.5em; font-weight: bold;" > VIEW ALL EMPLOYEE LEAVE </p>
	</div>

	<div>
		
		<table border='2px' cellpadding='4px'>
  <tr bgcolor="#C70039">
    <th style="color: white;" >Employee Name</th>
    <th style="color: white;">Employee Number</th>
    <th style="color: white;">From Date</th>
    <th style="color: white;">To Date</th>
    <th style="color: white;">Number of Days</th>
    <th style="color: white;">Reason</th>
    <th style="color: white;">Status</th>
  </tr>
  <tr>
  <%
  EmployeeImpl employeeImpl = new EmployeeImpl();
  List<PerimissionManagement> perimissionManagement = employeeImpl.getAllLeaveForApprovalBySupervisory(supervisoryNumber);
  for(int i=0;i<perimissionManagement.size();i++)
	{
	  
	  PerimissionManagement pm = perimissionManagement.get(i);
	  String employeeNumber = pm.getEmployeeNumber();
	  int permissionManagementId = pm.getPermissionManagementId();
	  String employeeName = employeeImpl.getEmployeeName(employeeNumber);
  %>
    <td><%= employeeName %></td>
    <td><%= employeeNumber %></td>
    <td><%= pm.getStartDate() %></td>
    <td><%= pm.getEndDate() %></td>
     <td><%= pm.getLeaveTaken() %></td>
     <td><%= pm.getReason() %></td>
    
    <td>
    <!-- <button name="approve" >Approve</button> <button name= "hold">Hold</button> <button name ="reject" >Reject</button> -->
    <form action="granted" method = "POST">
       <input  type="hidden" value=<%= permissionManagementId %> name="permissionManagementId" />
       <input  style="background: #23A3F6" class="button button2" type="submit" value="Approve" name="a" />
       <input   style="background: #F2901A" class="button button2" type="submit" value="Hold Leave" name="a" />
        <input   style="background: #F80A0A" class="button button2" type="submit" value="Reject" name="a" />
    </form>
</td>
  
  </tr>
     <%
     }
     %>
 
</table>
	</div>

	

</body>
</html>