package hrms.imlp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import hrms.Employee;
import hrms.PerimissionManagement;

/**
 * Servlet implementation class PermissionGrantedServlet
 */
@WebServlet("/granted")
public class PermissionGrantedServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public PermissionGrantedServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		doPost(request, response);
	}

	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked", "static-access" })
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		SendMailSSL mail = new SendMailSSL();
		String comments = "";
		int leaveTaken = 0;
		int leaveCount = 0;
		String from;
		String to;
		String sub;
		String msg;
		String password = null;
		response.setContentType("text/html");
		try {
			Session session = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory().openSession();
			Transaction transcation = session.beginTransaction();
			String permissionManagementIdString = request.getParameter("permissionManagementId");
			
			
			comments = request.getParameter("comments");
		
			int permissionManagementId = Integer.parseInt(permissionManagementIdString);
			String status = null;
			String btn = request.getParameter("a");
		
			List<PerimissionManagement> permissionManagementList =  new ArrayList<>();
			//Query query = session.createSQLQuery("UPDATE docto.permission_management SET permission_management.status = '"+status +"' and permission_management.comments = '"+comments+"' where permission_management.permission_management_id = '"+permissionManagementId+"'");
			Query query = session.createSQLQuery("Select * FROM  docto.permission_management where permission_management.permission_management_id = '"+permissionManagementId+"'");

			List<Object[]> objectList = query.list();
			
			objectList.stream().filter(Objects::nonNull).forEach(o -> {
				PerimissionManagement perimissionManagement = new PerimissionManagement();
				perimissionManagement.setPermissionManagementId((int) o[0]);
				perimissionManagement.setStartDate((String) o[1]);
				perimissionManagement.setReason((String) o[2]);
				//perimissionManagement.setStatus((String) o[3]);
				perimissionManagement.setEndDate((String) o[4]);
				perimissionManagement.setLeaveTaken((int) o[5]);
				perimissionManagement.setLeaveId((int) o[6]);
				perimissionManagement.setEmployeeNumber((String) o[7]);
				perimissionManagement.setLeaveCount((int) o[8]);
				perimissionManagement.setLeavePeriod((String) o[9]);
				perimissionManagement.setSupervisoryId((int) o[10]);
				perimissionManagement.setSupervisoryNumber((String) o[11]);
				//perimissionManagement.setComments((String) o[12]);
				permissionManagementList.add(perimissionManagement);
			});
			EmployeeImpl employeeImpl = new EmployeeImpl();
			Employee employee = employeeImpl.getByEmployeeName(permissionManagementList.get(0).getEmployeeNumber());
			to = employee.getMail();
			from = null;
			if(btn.equals("Approve")) {
				status = "APPROVED";
				
			}else if(btn.equals("Hold Leave")) {
				status = "PENDING";
				
			}else if(btn.equals("Reject")) {
				status = "REJECTED";
				leaveTaken = 0;
				leaveCount = leaveCount + leaveTaken;
				
				
			}
			sub="Leave Applicaton has been "+btn +"";
			msg = " Dear "+ employee.getName()+" <br>  The status of the leave you've applied for the period"+permissionManagementList.get(0).getStartDate() +" to "+permissionManagementList.get(0).getStartDate()  +" has been changed to "+btn+ " <br> Comeents: "+comments+"";
			permissionManagementList.get(0).setComments(comments);
			permissionManagementList.get(0).setStatus(status);
			if(permissionManagementList.get(0).getLeaveId()!=3) {
				permissionManagementList.get(0).setLeaveCount(leaveCount);
			}else {
				permissionManagementList.get(0).setLeaveCount(0);
			}
			permissionManagementList.get(0).setLeaveTaken(leaveTaken);
			
			session.saveOrUpdate(permissionManagementList.get(0));
			mail.send(from, password, to, sub, msg);
			
			response.sendRedirect("doctorprofile.jsp");
		
			transcation.commit();
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
public static void main(String[] args) {
	Base64.Encoder encoder = Base64.getEncoder();
	
	String s = "SMi$%TYMmmftjndt212122457y654322";

	String se = new String(encoder.encodeToString(s.getBytes().clone()));

	byte[] decodedBytes = Base64.getDecoder().decode(se);
	String sd = new String(decodedBytes);
	
	System.out.println(decodedBytes.toString());
	System.out.println("Encoder------------------>" + se);
	System.out.println("Decoder------------------>" + sd);
	
	String output;
	if(sd.equals(s)) {
		output = "success";
	}else {
		output = "Password is not same";
	}
	System.out.println(output);
}
}
