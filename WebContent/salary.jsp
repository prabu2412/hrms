<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
       <%@ page import="hrms.imlp.*" %>
   <%@page import = "java.util.*" %>
   <%@page import="hrms.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Salary calculation</title>
	<link rel="stylesheet" type="text/css" href="style.css">
</head>
<style type="text/css">
		
h1 {
  text-align: center;
 
  color: #06D85F;
  margin: 80px 0;
}

.box {
  width: 40%;
  margin: 0 auto;
  background: rgba(255,255,255,0.2);
  padding: 35px;
  border: 2px solid #fff;
  border-radius: 50%;
  background-clip: padding-box;
  text-align: center;
}


.overlay {
  position: fixed;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgba(0, 0, 0, 0.7);
  transition: opacity 500ms;
  visibility: hidden;
  opacity: 0;
}
.overlay:target {
  visibility: visible;
  opacity: 1;
}

.popup {
  margin: 70px auto;
  padding: 20px;
  background: #fff;
  border-radius: 5.0px;
  width: 30%;
  position: relative;
  transition: all 5s ease-in-out;
}

.popup h2 {
  margin-top: 0;
  color: #333;
 
}
.popup .close {
  position: absolute;
  top: 20px;
  right: 30px;
  transition: all 200ms;
  font-size: 30px;
  font-weight: bold;
  text-decoration: none;
  color: #333;
}
.popup .close:hover {
  color: #06D85F;
}
.popup .content {
  max-height: 30%;
  overflow: auto;
}

@media screen and (max-width: 70px){
  .box{
    width: 70%;
  }
  .popup{
    width: 70%;
  }
}
}

	</style>
	


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
<div style="font-size: 2em; font-weight: bold;">
  Salary Calculation
	</div>
<form action="salary" method="Post">
   
   <div align="center">

<div class="row" style="position: absolute; left:200px;">
  <div class="column" style="padding: 20px 20px 20px 10px;">
         	Employee Number :
         	</div>
         	<div class="column">
         		<select name="employeeName" style="padding: 0px 27px;">
         		
         		<%
		         		EmployeeImpl employeeImpl = new EmployeeImpl();
		         		List<Employee> employeeList = employeeImpl.getAllEmployeeDetails();
		         		for(int i=0;i<employeeList.size();i++)
		         		{
		         		%>
		         		
         		<option><%= employeeList.get(i).getEmployeeNumber() %></option>
         		<% } %>
         	</select>
         	</div> 

         	
         	
        <div class="column" style="padding: 10px 0px 20px 19px;">
         	Select an Month :
         	</div>
         	<div class="column" style="padding-left: 28px;">
         		<select name= "month" style="padding: 0px 10px;">
         		<option value = "1">January</option>
         		<option value = "2" >February</option>
         		<option value = "3">March </option>
         		<option value = "4">April </option>
         		<option value = "5">May</option>
         		<option value = "6">June</option>
         		<option value = "7">July</option>
         		<option value = "8">August</option>
         		<option value = "9"> September</option>
         		<option value = "10">October</option>
         		<option value = "11">November</option>
         		<option value = "12">December</option>
         	</select>
         	</div> 
         	
         	     	
        <div class="column" style="padding: 10px 0px 20px 19px;">
         	Select an Year :
         	</div>
         	<div class="column" style="padding-left: 28px;">
         		<select name="year" style="padding: 0px 10px;">
         		<% for(int i=1993;i<=2050;i++) { %>
         		<option><%=i %></option>
         		<%} %>
         	</select>
         	</div>  

         
</div>

<div style="margin-left: 10px; padding-top: 100px;">
         		<div class="row" >
         	Salary :<input  class = "btn end" type="Text" name="salary" placeholder="Salary" > (in Rupees <span style='font-family:Arial;'>&#8377;</span> )<br>
               </div>
               <div class="row" style="padding-top: 30px;">
               	Bonous :<input class = "btn end" type="text"   name="bonous" placeholder="Bonous" > (in Percentage % )<br>
         	</div>
            <div  class= "column" style="padding: 40px 10px;">
            	<div class="box">
            	
            		<button style="background: #F80A0A" class=" button4"  value="Calculate" name="c" type="submit" ><a  href="#popup1" name="c" value = "Calu"> Calculate  </a> </button>
         	
         	 </div>
         	 <div id="popup1" class="overlay">
	<div class="popup">
		<h2>Total Salary calculated</h2>
		<a class="close" href="#">&times;</a>
		<div class="content">
			<input  id="totalamount" class = "btn end" type="Text" name="totalamount" placeholder="Total Amount" readonly="readonly" value="<%=null==(session.getAttribute("totalSalary"))?"Please Enter Valid Salary":session.getAttribute("totalSalary") %>"> (in Rupees <span style='font-family:Arial;'>&#8377;</span> )<br>
		</div>
	</div>
</div>
             </div>
         	 


</div>
      
      </div>   

</form>
	</div>

</body>
</html>