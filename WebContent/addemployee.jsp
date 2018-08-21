<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
           <%@ page import="hrms.imlp.*" %>
   <%@page import = "java.util.*" %>
   <%@page import="hrms.*" %>
  
   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Add Employee</title>
<link rel="stylesheet" type="text/css" href="style.css">
</head>
<body style="font-family: calibri">
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

   <div>

 <p style="font-size: 1.5em; font-weight: bold;" >ADD EMPLOYEE </p>
	</div>
	<form action = "employee" method = "POST">
    <div >
    <div class="row">
        
    	<div class= "row">
    	<div class="column" style="padding-left:200px">
    	Supervisory Name:
    	</div>
    	<div class="column" style="padding-left:20px">
    	    <select name = "supervisoryName" style="padding-left: 50px;">
    	    	<%
    	    	 SupervisoryImpl supervisoryImpl = new SupervisoryImpl();
		         		List<Supervisory> supervisoryList = supervisoryImpl.getAllSupervisory();
		         		for(int i=0;i<supervisoryList.size();i++)
		         		{ 
		         		%>
    	    	<option value="<%=supervisoryList.get(0).getSupervisoryId()%>"><%=  supervisoryList.get(0).getSupervisoryName()  %></option>
    	    	<%  }  %> 
    	    </select>
    	    </div>
    	</div>
    </div>
	<div class="row">
		<div class="column" >
			<input class = "btn design" type="text" name="firstname" placeholder="Name"  required="">
		</div>
		<div class="column" >
			<input class = "btn design" type="text" name="mobile" placeholder="Mobile Number"  required="">
		</div>
	</div>
	<div class="row">
		<div class="column" >
			<input class = "btn design" type="text" name="employeeid" placeholder="Identification" required="" >
		</div>
		<div class="column" >
			<input class = "btn design" type="Date" name="dateofjoining" placeholder="Date of Joining"  required="" >
		</div>
	</div>

	<div class="row">
		<div class="column" >
			<input class = "btn design" type="text" name="age" placeholder="Age" required="" >
		</div>
		<div class="column" >
			<input class = "btn design" type="text" name="specilaist" placeholder="Specilaist" required="" >
		</div>
	</div>

	<div class="row">
		<div class="column" >
			<input class = "btn design" type="text" name="mail" placeholder="Email"  required="" >
		</div>
		<div class="column" >
			<input class = "btn design" type="text" name="address" placeholder="Address" required="" >
		</div>
	</div>
	

	</div>

	<div align = "center">
		
		 <input class="button"  type="submit" value="Save" name="q" />
	</div >
          </form>
	</div>

</body>
</html>