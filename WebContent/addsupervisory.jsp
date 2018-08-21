<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
  
   
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

 <p style="font-size: 1.5em; font-weight: bold;" >ADD Supervisory</p>
	</div>
	<form action = "supervisory" method = "POST">
    <div >
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
			<input class = "btn design" type="text" name="supervisoryid" placeholder="Identification" required="" >
		</div>
		<div class="column" >
			<input class = "btn design" type="Date" name="dateofjoining" placeholder="Date of Joining"  required="" >
		</div>
	</div>

	<div class="row">
		<div class="column" >
			<input class = "btn design" type="text" name="mail" placeholder="Email" required="" >
		</div>
		<div class="column" >
			<input class = "btn design" type="text" name="department" placeholder="Department" required="" >
		</div>
	</div>

	</div>

	<div align = "center">
		
		 <input class="button"  type="submit" value="Save" name="z" />
	</div >
          </form>
	</div>

</body>
</html>