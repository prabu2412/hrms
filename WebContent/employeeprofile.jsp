<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
         <%@ page import="hrms.imlp.*" %>
       <%@ page import="hrms.*" %>
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
		<%
		String employeeNumber = (String)session.getAttribute("userName");
		 EmployeeImpl employeeImpl1 = new EmployeeImpl();
		 SupervisoryImpl supervisoryImpl = new SupervisoryImpl();
		 String employeeName = employeeImpl1.getEmployeeName(employeeNumber);
		 String supervisoryName = supervisoryImpl.getSupervisoryNameByEmployeeNumber(employeeNumber);
		%>

		<div class="link">
		
		<li style="font-size: 1.5em;">	Welcome   <%= employeeName %> </li>
		
		
		<li style="font-size: 1.5em;float:right"> Your Supervisory <%=supervisoryName %></li>
		
		</div>
		
		
		
		
		<div><p style="font-size: 1.5em; font-weight: bold;"> Attendance status </p>
		
		
		<table border='2px' cellpadding='4px'>
  <tr bgcolor="#C70039">
    <th style="color: white;" >S.No</th>
    <th style="color: white;" >Employee Number</th>
     <th style="color: white;" >Date</th>
    <th style="color: white;"> Total Working Hours</th>
    <th style="color: white;"> Status</th>
    
   
  </tr>
  
  <%
  
  AttendanceImpl attendanceImpl = new AttendanceImpl();
  List<Attendance> attendanceList = attendanceImpl.getAllAttendance(employeeNumber);
  int count = attendanceList.size();

  for(int i=0,j=1;i<count;i++)
	{
	  
	  Attendance pm = attendanceList.get(i);
	
	 
  %>
  <tr>
    <td  ><%= j++ %></td>
    <td  ><%= pm.getEmployeeNumber() %></td>
    <td  ><%= pm.getDate() %></td>
    <td  > <%= pm.getCumulativeTime()%> </td>
    <td ><%= pm.getAttendanceStatus() %></td>
   
  </tr>
     <%
     }
     %>
 
</table>
		
		
		</div>
		
		
		
		<div>
		   <div> <p style="font-size: 1.5em; font-weight: bold;padding-top:50px;"> Leave status </p></div>
		   
			<table border='2px' cellpadding='4px'>
  <tr bgcolor="#C70039">
    <th style="color: white;" >S.No</th>
    <th style="color: white;" >Employee Number</th>
    <th style="color: white;" >Leave Start date</th>
    <th style="color: white;" >Leave End Date</th>
    <th style="color: white;"> Number of Leave</th>
    <th style="color: white;" >Status</th>
     <th style="color: white;" >Comments</th>
    <th style="color: white;" >Leave Type</th>
    <th style="color: white;" >Remaining Leave</th>
   
  </tr>
  
  <%
  
  EmployeeImpl employeeImpl = new EmployeeImpl();
  List<PerimissionManagement> permissionList = employeeImpl.getAllLeaveStatus(employeeNumber);
  for(int i=0,j=1;i<permissionList.size();i++)
	{
	  
	  PerimissionManagement pm = permissionList.get(i);
	  String leaveType = employeeImpl.getLeaveType(pm.getLeaveId());
	 
  %>
  <tr  <%
      String bgcolor = null;
      if(pm.getStatus().equals("PENDING")){ //YELLOW%>
    	  bgcolor = "#F2901A";    
   <%   }else if(pm.getStatus().equals("REJECTED")){  //RED %>
    	  bgcolor = "#F80A0A"; 
     <%    } else if(pm.getStatus().equals("APPROVED")){  //Blue %>
    	  bgcolor = "#23A3F6";
    <%   }   %>
  >
    <td style="color: white;" ><%= j++ %></td>
    <td style="color: white;" ><%= pm.getEmployeeNumber() %></td>
    <td style="color: white;" ><%= pm.getStartDate() %></td>
    <td style="color: white;" ><%= pm.getEndDate() %></td>
    <td style="color: white;" ><%=pm.getLeaveTaken()%></td>
    <td style="color: white;" ><%= pm.getStatus() %></td>
     <td style="color: white;" ><%= pm.getComments() %></td>
    <td style="color: white;" ><%= leaveType %></td>
    <td style="color: white;" ><%= pm.getLeaveCount() %></td>    
   
  </tr>
     <%
     }
     %>
 
</table>
		
		
		</div>
	</div>

</body>
</html>