package hrms.imlp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import hrms.Employee;
import hrms.Login;
import hrms.PerimissionManagement;

@WebServlet("/employee")
public class EmployeeImpl extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("deprecation")
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		try {
			Session session = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory().openSession();
			Transaction transcation = session.beginTransaction();

			Employee employee = new Employee();
			String btn = request.getParameter("q");
			if (btn.equals("Save")) {
				Login login = new Login();
				employee.setName(request.getParameter("firstname"));
				employee.setMobile(request.getParameter("mobile"));
				String employeeNumber = request.getParameter("employeeid");
				employee.setEmployeeNumber(employeeNumber);
				employee.setDateOfJoining(request.getParameter("dateofjoining"));
				employee.setSpecilaist(request.getParameter("specilaist"));
				employee.setMail(request.getParameter("mail"));
				employee.setAddress(request.getParameter("address"));
				employee.setPassword("SMI");
				String supervisoryId = request.getParameter("supervisoryName");
				employee.setSupervisoryId(Integer.parseInt(supervisoryId));

				Criteria criteria = session.createCriteria(Employee.class);
				Employee validEmployee = (Employee) criteria.add(Restrictions.eq("employeeNumber", employeeNumber)).uniqueResult();
				if (Objects.isNull(validEmployee)) {
					login.setUserName(employee.getEmployeeNumber());
					login.setPasswordKey("SMI");
					login.setRole("EMPLOYEE");
					session.save(login);
					session.save(employee);
					RequestDispatcher rd=request.getRequestDispatcher("addemployee.jsp");
					//	out.println("<font color='red'>Successfully applyed Leave</font>");
						rd.include(request, response);
						out.println("<p style= 'color:red ;' align = center >Successfully Add an Employee</p>");
					
				} else {
					RequestDispatcher rd = request.getRequestDispatcher("addemployee.jsp");
					//out.println("<font color='red'>Employee Number Alderly exist</font>");
					rd.include(request, response);
					out.println("<p style= 'color:red ;' align = center >Employee Number Alderly exist</p>");
					throw new Exception("Employee Number Alderly exist");
				}
			}
			transcation.commit();
			

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}
	
	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked"})
	public  List<PerimissionManagement> getAllLeaveForApproval(){
		
		List<PerimissionManagement> permissionManagementList = new ArrayList<>();
		try {
			
			Session session = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory().openSession();
			Transaction transcation = session.beginTransaction();
			
			Query query = session.createSQLQuery("SELECT * FROM docto.permission_management where status ='PENDING'");
			
			List<Object[]> objectList = query.list();
		
				objectList.stream().filter(Objects::nonNull).forEach(o -> {
					PerimissionManagement perimissionManagement = new PerimissionManagement();
					perimissionManagement.setPermissionManagementId((int) o[0]);
					perimissionManagement.setStartDate((String) o[1]);
					perimissionManagement.setReason((String) o[2]);
					perimissionManagement.setStatus((String) o[3]);
					perimissionManagement.setEndDate((String) o[4]);
					perimissionManagement.setLeaveTaken((int) o[5]);
					perimissionManagement.setLeaveId((int) o[6]);
					perimissionManagement.setEmployeeNumber((String) o[7]);
					perimissionManagement.setLeaveCount((int) o[8]);
					perimissionManagement.setLeavePeriod((String) o[9]);
					perimissionManagement.setSupervisoryId((int) o[10]);
					perimissionManagement.setSupervisoryNumber((String) o[11]);
					perimissionManagement.setComments((String) o[12]);
					permissionManagementList.add(perimissionManagement);
				});
				transcation.commit();
		} catch (Exception e) {
			System.out.println(e);
		}
		return permissionManagementList;
	}
	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked"})
	public static String getEmployeeName(String employeeNumber) {
		
		String employeeName = null;
		try {
			Session session = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory().openSession();
			Transaction transcation = session.beginTransaction();
			
			Query query = session.createSQLQuery("SELECT employee.name FROM docto.employee where employee_number='"+employeeNumber+"'");
			List<Object> object = query.list();
			employeeName = (String)object.get(0);
			transcation.commit();
		} catch (Exception e) {
			System.out.println(e);
		}
		return employeeName;
	}

	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked"})
	public List<Employee> getAllEmployeeDetails(){
		
		List<Employee> employeeList = new ArrayList<>();
		try {
			
			Session session = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory().openSession();
			Transaction transcation = session.beginTransaction();
			
			Query query = session.createSQLQuery("SELECT * FROM docto.employee");
			
			List<Object[]> objectList = query.list();
		
				objectList.stream().filter(Objects::nonNull).forEach(o -> {
					Employee employee = new Employee();
					employee.setEmployeeId((int) o[0]);
					employee.setName((String) o[1]);
					employee.setEmployeeNumber((String) o[2]);
					employee.setDateOfJoining((String) o[3]);
					employee.setSpecilaist((String) o[4]);
					employee.setMail((String) o[5]);
					employee.setAddress((String) o[6]);
					employee.setPassword((String) o[7]);
					employee.setMobile((String) o[8]);
					employee.setSupervisoryId((int) o[9]);
					employeeList.add(employee);
				});
				transcation.commit();
		} catch (Exception e) {
			System.out.println(e);
		}
		return employeeList;
	}
	
	
	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked"})
	public List<PerimissionManagement> getAllLeaveStatus(String employeeNumber){
		
		List<PerimissionManagement> permissionManagementList = new ArrayList<>();
		try {
			
			Session session = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory().openSession();
			Transaction transcation = session.beginTransaction();
			
			Query query = session.createSQLQuery("SELECT * FROM docto.permission_management where employee_number ='"+employeeNumber+"'");
			
			List<Object[]> objectList = query.list();
		
				objectList.stream().filter(Objects::nonNull).forEach(o -> {
					PerimissionManagement perimissionManagement = new PerimissionManagement();
					perimissionManagement.setPermissionManagementId((int) o[0]);
					perimissionManagement.setStartDate((String) o[1]);
					perimissionManagement.setReason((String) o[2]);
					perimissionManagement.setStatus((String) o[3]);
					perimissionManagement.setEndDate((String) o[4]);
					perimissionManagement.setLeaveTaken((int) o[5]);
					perimissionManagement.setLeaveId((int) o[6]);
					perimissionManagement.setEmployeeNumber((String) o[7]);
					perimissionManagement.setLeaveCount((int) o[8]);
					perimissionManagement.setLeavePeriod((String) o[9]);
					perimissionManagement.setSupervisoryId((int) o[10]);
					perimissionManagement.setSupervisoryNumber((String) o[11]);
					perimissionManagement.setComments((String) o[12]);
					permissionManagementList.add(perimissionManagement);
				});
				transcation.commit();
		} catch (Exception e) {
			System.out.println(e);
		}
		return permissionManagementList;
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	public String getLeaveType(int leaveId) {
		String leaveType = null;
		
		try {
			Session session = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory().openSession();
			Transaction transcation = session.beginTransaction();
			Query query = session.createSQLQuery("SELECT leave_type FROM docto.leave where leave_id = '"+ leaveId+"'");
			List<Object> object = query.list();
			leaveType = (String)object.get(0);
			transcation.commit();
		} catch (Exception e) {
			System.out.println(e);
		}
		return leaveType;
	}
	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked"})
	public List<PerimissionManagement> getAllLeaveForApprovalBySupervisory(String supervisoryNumber){
		
		List<PerimissionManagement> permissionManagementList = new ArrayList<>();
		try {
			
			Session session = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory().openSession();
			Transaction transcation = session.beginTransaction();
			
			Query query = session.createSQLQuery("SELECT * FROM docto.permission_management where status ='PENDING' AND supervisory_number = '"+ supervisoryNumber+"'");
			
			List<Object[]> objectList = query.list();
		
				objectList.stream().filter(Objects::nonNull).forEach(o -> {
					PerimissionManagement perimissionManagement = new PerimissionManagement();
					perimissionManagement.setPermissionManagementId((int) o[0]);
					perimissionManagement.setStartDate((String) o[1]);
					perimissionManagement.setReason((String) o[2]);
					perimissionManagement.setStatus((String) o[3]);
					perimissionManagement.setEndDate((String) o[4]);
					perimissionManagement.setLeaveTaken((int) o[5]);
					perimissionManagement.setLeaveId((int) o[6]);
					perimissionManagement.setEmployeeNumber((String) o[7]);
					perimissionManagement.setLeaveCount((int) o[8]);
					perimissionManagement.setLeavePeriod((String) o[9]);
					perimissionManagement.setSupervisoryId((int) o[10]);
					perimissionManagement.setSupervisoryNumber((String) o[11]);
					permissionManagementList.add(perimissionManagement);
				});
				transcation.commit();
		} catch (Exception e) {
			System.out.println(e);
		}
		return permissionManagementList;
	}

	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked"})
	public Employee  getByEmployeeName(String employeeNumber) {
		
		List<Employee> employeeList = new ArrayList<>();
		try {
			Session session = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory().openSession();
			Transaction transcation = session.beginTransaction();
			
			Query query = session.createSQLQuery("SELECT * FROM docto.employee where employee_number='"+employeeNumber+"'");
			List<Object[]> objectList = query.list();
			
			objectList.stream().filter(Objects::nonNull).forEach(o -> {
				Employee employee = new Employee();
				employee.setEmployeeId((int) o[0]);
				employee.setName((String) o[1]);
				employee.setEmployeeNumber((String) o[2]);
				employee.setDateOfJoining((String) o[3]);
				employee.setSpecilaist((String) o[4]);
				employee.setMail((String) o[5]);
				employee.setAddress((String) o[6]);
				employee.setPassword((String) o[7]);
				employee.setMobile((String) o[8]);
				employee.setSupervisoryId((int) o[9]);
				employeeList.add(employee);
			});
			transcation.commit();
		} catch (Exception e) {
			System.out.println(e);
		}
		return employeeList.get(0);
	}
}
