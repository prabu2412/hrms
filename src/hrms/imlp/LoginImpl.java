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
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

/**
 * Servlet implementation class CURDServlet
 */
@WebServlet("/login")
public class LoginImpl extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
    
	@SuppressWarnings({ "rawtypes", "unchecked", "deprecation"})
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		String btn = request.getParameter("q");
		if(btn.equals("Login in")) {
			String userName = request.getParameter("username");
			String password = request.getParameter("password");
			Session session = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory().openSession();
			Transaction transaction = session.beginTransaction();
			Query query = session.createSQLQuery("SELECT user_name FROM docto.login where user_name='"+userName+"' AND password_key = '"+ password+"' AND ROLE = 'EMPLOYEE' ");
			
			List<Object[]> list = query.list();
			HttpSession sess = request.getSession();
			sess.setAttribute("userName", userName);
			if(list.size()>0) {
				 response.sendRedirect("employeeprofile.jsp");
			}else {
				Query query1 = session.createSQLQuery("SELECT user_name FROM docto.login where user_name='"+userName+"' AND password_key = '"+ password+"' AND ROLE = 'SUPERADMIN' ");
				List<Object[]> list1 = query1.list();
				if(list1.size()>0) {
					 response.sendRedirect("doctorprofile.jsp");
				}else {Query query2 = session.createSQLQuery("SELECT user_name FROM docto.login where user_name='"+userName+"' AND password_key = '"+ password+"' AND ROLE = 'SUPERVISORY' ");
				List<Object[]> list2 = query2.list();
				HttpSession sess1 = request.getSession();
				sess1.setAttribute("supervisoryName", userName);
				if(list2.size()>0) {
					 response.sendRedirect("supervisory.jsp");
				}else {
				
				RequestDispatcher rd=request.getRequestDispatcher("index.html");
				rd.include(request, response);
				out.println("<font color='red'>Login Credential is Wrong!!..Please check admin if you have any query</font>");
			
				}
				}
		}
			transaction.commit();
			
		}
		
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
