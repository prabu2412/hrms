package hrms.imlp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
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

/**
 * Servlet implementation class DeleteEmployee
 */
@WebServlet("/delete")
public class DeleteEmployee extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteEmployee() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		
		try {
			Session session = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory().openSession();
			Transaction transcation = session.beginTransaction();
			String employeeNumber = request.getParameter("employeeNumber");
			String btn = request.getParameter("b");
			if(btn.equals("Delete")) {
			Query query = 	session.createSQLQuery("DELETE FROM docto.employee where employee_number = '"+employeeNumber +"'");
			int i = query.executeUpdate();
			if(i>0) {
				RequestDispatcher rd=request.getRequestDispatcher("employeeview.jsp");
				
				rd.include(request, response);
				
				//out.println("<p style= 'color:red ;' align = center >You have been give "+ status+" for Enployee "+ employeeNumber+"</p>");
				out.println("<script>"
						+ "alert('Successfully deleted the Employee Id of "+employeeNumber +"'); "
								+ "window.location='employeeview.jsp'</script>");
			       }
			}else {
				Employee employee = new Employee();
				Query query = 	session.createSQLQuery("SELECT employee_id FROM docto.employee where employee_number = '"+employeeNumber +"'");
				List<Object> object = query.list();
				int employeeId = (int)object.get(0);
				employee.setEmployeeId(employeeId);
				employee.setEmployeeNumber(request.getParameter("employeeNumber"));
				employee.setName(request.getParameter("name"));
				employee.setDateOfJoining(request.getParameter("dob"));
				employee.setSpecilaist(request.getParameter("specialist"));
				employee.setMail(request.getParameter("mail"));
				employee.setMobile(request.getParameter("mobile"));
				employee.setAddress(request.getParameter("address"));
				employee.setPassword("SMI");
				int supervisoryId = Integer.parseInt(request.getParameter("sid"));
				employee.setSupervisoryId(supervisoryId);
						
				session.saveOrUpdate(employee);
				
				RequestDispatcher rd=request.getRequestDispatcher("employeeview.jsp");
				
				rd.include(request, response);
				
				
				out.println("<script>"
						+ "alert('Successfully Updated the Employee Id of "+employeeNumber +"'); "
								+ "window.location='employeeview.jsp'</script>");
			}
			transcation.commit();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}
