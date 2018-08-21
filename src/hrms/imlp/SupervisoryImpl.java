package hrms.imlp;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
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

import hrms.Login;
import hrms.Supervisory;


@WebServlet("/supervisory")
public class SupervisoryImpl extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("deprecation")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		try {
			Session session = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory().openSession();
			Transaction transcation = session.beginTransaction();

			Supervisory supervisory = new Supervisory();
			String btn = request.getParameter("z");
			if (btn.equals("Save")) {
				Login login = new Login();
				supervisory.setSupervisoryName(request.getParameter("firstname"));
				supervisory.setSupervisoryMobile(request.getParameter("mobile"));
				String supervisoryNumber = request.getParameter("supervisoryid");
				supervisory.setSupervisoryNumber(supervisoryNumber);
				supervisory.setSupervisoryDateOfJoining(request.getParameter("dateofjoining"));
				supervisory.setDepartmentHead(request.getParameter("department"));
				
				supervisory.setSupervisoryMail(request.getParameter("mail"));
				supervisory.setSupervisoryMobile(request.getParameter("mobile"));
				
				

				Criteria criteria = session.createCriteria(Supervisory.class);
				Supervisory validSupervisory = (Supervisory) criteria.add(Restrictions.eq("supervisoryNumber", supervisoryNumber)).uniqueResult();
				if (Objects.isNull(validSupervisory)) {
					login.setUserName(supervisory.getSupervisoryNumber());
					login.setPasswordKey("SMI");
					login.setRole("SUPERVISORY");
					session.save(login);
					session.save(supervisory);
					RequestDispatcher rd=request.getRequestDispatcher("addsupervisory.jsp");
					//	out.println("<font color='red'>Successfully applyed Leave</font>");
						rd.include(request, response);
						out.println("<p style= 'color:red ;' align = center >Successfully Add an Supervisory</p>");
					
				} else {
					RequestDispatcher rd = request.getRequestDispatcher("addsupervisory.jsp");
					//out.println("<font color='red'>Employee Number Alderly exist</font>");
					rd.include(request, response);
					out.println("<p style= 'color:red ;' align = center >Supervisory Number Alderly exist</p>");
					throw new Exception("Employee Number Alderly exist");
				}
			}
			transcation.commit();
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked"})
	public   List<Supervisory> getAllSupervisory() {
	
		List<Supervisory> supervisoryList = new ArrayList<>();
		try {
			Session session = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory().openSession();
			Transaction transcation = session.beginTransaction();
			Query query = session.createSQLQuery("SELECT * FROM docto.supervisory");
             
	         List<Object[]> object = query.list();
               object.stream().forEach(s->{
            	   Supervisory supervisory = new Supervisory();
            	   supervisory.setSupervisoryId((int)s[0]);
            	   supervisory.setSupervisoryName((String)s[1]);
            	   supervisory.setDepartmentHead((String)s[2]);
            	   supervisory.setSupervisoryDateOfJoining((String)s[3]);
            	   supervisory.setSupervisoryNumber((String)s[4]);
            	   supervisory.setSupervisoryMobile((String)s[5]);
            	   supervisory.setSupervisoryMail((String)s[6]);
            	   supervisoryList.add(supervisory);
               });
			
			transcation.commit();
		} catch (Exception e) {
            System.out.println(e);
		}
		return supervisoryList;
	}

	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked"})
	public   String getSupervisoryName(int supervisoryId) {
 	   String supervisoryName = null;
		
		try {
			Session session = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory().openSession();
			Transaction transcation = session.beginTransaction();
			Query query = session.createSQLQuery("SELECT supervisory_name FROM docto.supervisory where supervisory_id = '"+ supervisoryId+"'");
             
	         List<String> object = query.list();
	         supervisoryName = object.get(0);
			transcation.commit();
		} catch (Exception e) {
            System.out.println(e);
		}
		return supervisoryName;
	}
	
	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked"})
	public   String getSupervisoryNameByEmployeeNumber(String employeeNumber) {
 	   String supervisoryName = null;
		
		try {
			Session session = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory().openSession();
			Transaction transcation = session.beginTransaction();
			Query query = session.createSQLQuery("SELECT supervisory_id FROM docto.employee where employee_number = '"+ employeeNumber+"'");
             
	         List<Integer> object = query.list();
	         int supervisoryId = object.get(0);
	         supervisoryName = getSupervisoryName(supervisoryId);
			transcation.commit();
		} catch (Exception e) {
            System.out.println(e);
		}
		return supervisoryName;
	}
	
	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked"})
	public static String getSupervisoryName(String supervisoryNumber) {
		
		String supervisoryName = null;
		try {
			Session session = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory().openSession();
			Transaction transcation = session.beginTransaction();
			
			Query query = session.createSQLQuery("SELECT supervisory.supervisory_name FROM docto.supervisory where supervisory_number='"+supervisoryNumber+"'");
			List<Object> object = query.list();
			supervisoryName = (String)object.get(0);
			transcation.commit();
		} catch (Exception e) {
			System.out.println(e);
		}
		return supervisoryName;
	}
	
	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked"})
	public   Supervisory getSupervisoryByEmployeeNumberAll(String employeeNumber) {

        List<Supervisory> supervisoryList = new ArrayList<>();
		
		try {
			Session session = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory().openSession();
			Transaction transcation = session.beginTransaction();
			Query query = session.createSQLQuery("SELECT supervisory_id FROM docto.employee where employee_number = '"+ employeeNumber+"'");
			 List<Integer> object = query.list();
	         int supervisoryId = object.get(0);
	        
	         Query query1 = session.createSQLQuery("SELECT * FROM docto.supervisory where supervisory_id = '"+ supervisoryId+"'");
			 List<Object[]> object1 = query1.list();
             object1.stream().forEach(s->{
          	   Supervisory supervisory = new Supervisory();
          	   supervisory.setSupervisoryId((int)s[0]);
          	   supervisory.setSupervisoryName((String)s[1]);
          	   supervisory.setDepartmentHead((String)s[2]);
          	   supervisory.setSupervisoryDateOfJoining((String)s[3]);
          	   supervisory.setSupervisoryNumber((String)s[4]);
          	   supervisory.setSupervisoryMobile((String)s[5]);
          	   supervisory.setSupervisoryMail((String)s[6]);
          	   supervisoryList.add(supervisory);
             });
			transcation.commit();
		} catch (Exception e) {
            System.out.println(e);
		}
		return supervisoryList.get(0);
	}
	
 public static void main(String[] args) {
	 Date date = new Date();
	 LocalDate date1 = LocalDate.now();
	 LocalDateTime date2 = LocalDateTime.now();
	 LocalTime date3 = LocalTime.now();
	 System.out.println(date     +" "+ date1+" "+ date2  +" "+ date3);
}
}
