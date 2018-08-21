<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
     <%@ page import="hrms.imlp.*" %>
       <%@ page import="hrms.*" %>
   <%@page import = "java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>View Employee</title>
	<link rel="stylesheet" type="text/css" href="style.css"> 
</head>

<body >
	<div>
		
      		<nav>

				<p align="center"><img src="Doctologo.png" width="170px" ></p>
			
					<div class = 'link' >
                 		 <li><a href="doctorprofile.jsp">Home</a></li>
                 		 <li><a href="addemployee.jsp">Add Employee</a></li>
                  		<li><a href="employeeview.jsp">Employee Profile </a></li>
                  		<li><a href="salary.jsp">Salary Calculation</a></li>
                  			<li><a href="addsupervisory.jsp">Add Supervisory</a></li>
                   		<li style="float: right"><a href="index.html">Log out</a></li>

					</div>
			</nav>


</div>
<div>
  <p style=" font-size: 1.5em; font-weight: bold;" > VIEW ALL  AND DELETE EMPLOYEE </p>
	</div>

    <div >
		<table rules="rows" cellpadding='4px' align="center" style="width:100% ">
  <tr  bgcolor="#C70039"> 
    <th style="color: white;">Employee Name</th>
    <th style="color: white;">Employee Number</th>
    <th style="color: white;">Date of Joining</th>
    <th style="color: white;" >Specialist</th>
    <th style="color: white;">Email Id</th>
    <th style="color: white;">Mobile Number</th>
    <th style="color: white;">Address</th>
     <th style="color: white;">Supervisory</th>
     <th style="color: white;">Supervisory Id</th>
     <th style="color: white;">Status</th>
  </tr>
  <tr>
  <%
  String employeeNumber = null;
  
   EmployeeImpl employeeImpl = new EmployeeImpl();
   SupervisoryImpl supervisoryImpl = new SupervisoryImpl();
  List<Employee> employeeList = employeeImpl.getAllEmployeeDetails();
  Supervisory supervisory = new Supervisory();
  for(int i=0;i<employeeList.size();i++)
	{
	  
	  Employee pm = employeeList.get(i);
	  employeeNumber = pm.getEmployeeNumber();
	  supervisory = supervisoryImpl.getSupervisoryByEmployeeNumberAll(pm.getEmployeeNumber());
  %>
    <form action="delete" method = "POST">
    <td class="td" ><input size="5" class = "update" type="text" value="<%= pm.getName() %>" name="name"/></td>
    <td class="td" ><input  size="5" class = "update" type="text" value="<%= pm.getEmployeeNumber() %>" name="employeeNumber"/></td>
    <td class="td" ><input  size="10" class = "update" type="text" value="<%= pm.getDateOfJoining() %>" name="dob"/></td>
    <td class="td" ><input  size="5" class = "update" type="text" value="<%= pm.getSpecilaist() %>" name="specialist"/></td>
     <td class="td" ><input   class = "update" type="text" value="<%= pm.getMail() %>" name="mail"/></td>
     <td class="td" ><input  size="10" class = "update" type="text" value="<%= pm.getMobile() %>" name="mobile"/></td>
     <td class="td" ><input  size="5" class = "update" type="text" value="<%= pm.getAddress() %>" name="address"/></td>
       <td class="td" ><input  size="5" class = "update" type="text" value="<%= supervisory.getSupervisoryName() %>" readonly/></td>
       <td class="td" ><input  size="5" class = "update" type="text" value="<%= supervisory.getSupervisoryNumber() %>" /></td>
         <input  type="hidden" value=<%= supervisory.getSupervisoryId() %> name="sid" />
        <td>
    <!-- <button name="approve" >Approve</button> <button name= "hold">Hold</button> <button name ="reject" >Reject</button> -->
  
       <input  type="hidden" value=<%= employeeNumber %> name="employeeNumber" />
       
       <input   style="background: #F2901A" class="button7 " type="submit" value="Update" name="b" />
        <input   style="background: #F80A0A" class="button7 " type="submit" value="Delete" name="b" />
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