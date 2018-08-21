package hrms.imlp;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

import hrms.PerimissionManagement;
import hrms.SalaryCalculation;

/**
 * Servlet implementation class SalaryCalculationImpl
 */
@WebServlet("/salary")
public class SalaryCalculationImpl extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public SalaryCalculationImpl() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		doPost(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		try {

			Session session = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory().openSession();
			String employeeNumber = request.getParameter("employeeName");
			String salaryMonth = request.getParameter("month");
			String salaryAmount = request.getParameter("salary");
			String bonous = request.getParameter("bonous");
			String years = request.getParameter("year");
			SalaryCalculation salaryCalculation = new SalaryCalculation();
			String salaryWithLeaveCalculation = calculateSalaryForLeave(employeeNumber, session, salaryMonth,
					salaryAmount);
			salaryCalculation.setEmployeeNumber(employeeNumber);
			salaryCalculation.setSalaryMonth(salaryMonth);
			salaryCalculation.setSalaryAmount(salaryAmount);
			salaryCalculation.setYear(years);
			salaryCalculation.setBonous(bonous);
			int count = validateAlderlyCalculatedSalary(employeeNumber,salaryMonth,years,session);
			if(count>0) {
				RequestDispatcher rd=request.getRequestDispatcher("salary.jsp");
				
				rd.include(request, response);
				out.println("<p style= 'color:red ;' align = center >He has Recently only Jonined Please check employee details</p>");
				
				throw new Exception("He has Recently only Jonined Please check employee details");
			}
			int counts = validateAlderlyExits(employeeNumber,salaryMonth,years,session);
			if(counts>0) {
               RequestDispatcher rd=request.getRequestDispatcher("salary.jsp");
				
				rd.include(request, response);
				out.println("<p style= 'color:red ;' align = center >He has Alderly calculated the salary Please check it</p>");
				
				throw new Exception("He has Alderly calculated the salary Please check it");
			}
            String totalSalary = salaryWithBonous(salaryWithLeaveCalculation,bonous);
			Transaction transcation = session.beginTransaction();
			salaryCalculation.setTotalSalary(totalSalary);
			HttpSession sess = request.getSession();
			sess.setAttribute("totalSalary", totalSalary);
			session.save(salaryCalculation);
			RequestDispatcher rd=request.getRequestDispatcher("salary.jsp");
			//	out.println("<font color='red'>Successfully applyed Leave</font>");
				rd.include(request, response);
				out.println("<p style= 'color:red ;' align = center >Successfully calculated the Salary </p>");
			transcation.commit();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	private int validateAlderlyExits(String employeeNumber, String salaryMonth, String years, Session session) {
		int count = 0;
		int uniqueId=0;
		try {
			Transaction transaction = session.beginTransaction();
			Query query = session.createSQLQuery(
					"SELECT salary_calculation_id FROM docto.salary_calculation where employee_number = '"+ employeeNumber+"' AND salary_month = '"+salaryMonth +"' AND year = '"+years +"'");
			List<Object> object = query.list();
			uniqueId = (int)object.get(0);
			if(uniqueId>0) {
				count++;
			}
			transaction.commit();
		} catch (Exception e) {
			System.out.println(e);
		}
		return count;
	}

	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	private int validateAlderlyCalculatedSalary(String employeeNumber, String salaryMonth, String years,Session session) {
		int count = 0;
		String dateOfJoining = null;
		int sm = Integer.parseInt(salaryMonth);
		try {
			Transaction transaction = session.beginTransaction();
			Query query = session.createSQLQuery(
					"SELECT date_of_joining FROM docto.employee where employee_number='" + employeeNumber + "'");
			List<Object> object = query.list();
			dateOfJoining = (String)object.get(0);
			LocalDate joininDate = LocalDate.parse(dateOfJoining);
			int jd = joininDate.getMonthValue();
			int yearString = joininDate.getYear(); 
			 int givenYear =  Integer.parseInt(years);
			
			if(yearString>=givenYear &&jd<=sm ) {
				count++;
			}
			transaction.commit();
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return count;
	}

	private String salaryWithBonous(String salaryWithLeaveCalculation, String bonous) {
		if(bonous!=null) {
		long s= Long.valueOf(salaryWithLeaveCalculation);
		BigDecimal salary = BigDecimal.valueOf(s);
		long bs = Long.parseLong(bonous);
		BigDecimal percentage = new BigDecimal(100);
		BigDecimal b = BigDecimal.valueOf(bs).divide(percentage);
		BigDecimal finalValue = (salary.multiply(b).add(salary));
		return finalValue.toString();
		}
		return salaryWithLeaveCalculation;
	}

	@SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
	private String calculateSalaryForLeave(String employeeNumber, Session session, String salaryMonth,
			String salaryAmount) {
		String salaryWithLeaveCalculation = null;
		List<PerimissionManagement> permissionManagementList = new ArrayList<>();

		try {
			int sm = Integer.parseInt(salaryMonth);
			Transaction transaction = session.beginTransaction();
			Query query = session.createSQLQuery(
					"SELECT * FROM docto.permission_management where employee_number='" + employeeNumber + "'");

			List<Object[]> objectList = query.list();
			if (objectList.size() > 0) {
				objectList.stream().forEach(o -> {
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
					permissionManagementList.add(perimissionManagement);
				});

				List<String> endDateList = permissionManagementList.stream()
						.filter(p -> (LocalDate.parse(p.getEndDate()).getMonthValue()==sm))
						.map(PerimissionManagement::getEndDate).collect(Collectors.toList());
				LocalDate dateofSalaryTobe = LocalDate.parse(endDateList.get(0));
				Month getMonth = dateofSalaryTobe.getMonth();

				int totalDaysInMonth = getMonth.minLength();
				int salaryInteger = Integer.parseInt(salaryAmount);
				

				Optional<Integer> optionLeaveCountForCL = permissionManagementList.stream()
						.filter(p -> (LocalDate.parse(p.getEndDate()).getMonthValue()==sm))
						.map(PerimissionManagement::getLeaveTaken).reduce(Integer::sum);
				if (optionLeaveCountForCL.isPresent()) {
					int totalLeavetaken = optionLeaveCountForCL.get();
					int totalWorkingDays = totalDaysInMonth - totalLeavetaken;
					
					int perDaySalary = salaryInteger/totalDaysInMonth;
					
					int finalSalary = perDaySalary*totalWorkingDays;
					salaryWithLeaveCalculation = String.valueOf(finalSalary);

				}
			} else {
				salaryWithLeaveCalculation = salaryAmount;
			}
			transaction.commit();
		} catch (Exception e) {
			System.out.println(e);
		}

		return salaryWithLeaveCalculation;
	}

}
