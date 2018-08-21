package hrms.imlp;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

import hrms.Leave;
import hrms.PerimissionManagement;
import hrms.Supervisory;

@WebServlet("/PermissionManagementImpl")
public class PermissionManagementImpl extends HttpServlet {
	private static final long serialVersionUID = 1L;
	

	public Session getSession() {
		return new Configuration().configure("hibernate.cfg.xml").buildSessionFactory().openSession();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		try {
			HttpSession sess = request.getSession();
			String username = (String) sess.getAttribute("userName");
			Session session = getSession();
			PerimissionManagement perimissionManagement = new PerimissionManagement();
			String btn = request.getParameter("q");
			if (btn.equals("Apply Leave")) {
				String startDate = request.getParameter("startdate");
				String endDate = request.getParameter("enddate");
				int wrongDate = validateDate(startDate, endDate, username, session);
				if (wrongDate > 0) {
					RequestDispatcher rd = request.getRequestDispatcher("applyleave.jsp");
					rd.include(request, response);
					out.println("<p style= 'color:red ;' align = center >Please provide the valide date</p>");
					throw new Exception("Please provide the valide date");
				}
				perimissionManagement.setStartDate(startDate);
				perimissionManagement.setEndDate(endDate);
				String leaveType = request.getParameter("leavetype");
				perimissionManagement.setReason(request.getParameter("reason"));
				perimissionManagement.setStatus("PENDING");
				if (Objects.nonNull(leaveType)) {
					List<Leave> leaveList = getLeaveByLeaveType(leaveType, session);
					int leaveId = leaveList.get(0).getLeaveId();
					String leavePeriod = leaveList.get(0).getLeavePeriod();
					int givenLeaveCount = Integer.parseInt(leaveList.get(0).getLeaveCount());
					int leaveCount = dateCount(startDate, endDate);
					int period = validatePropagationPeriod(leaveType, username, session, startDate, endDate);
					if (period > 0) {
						RequestDispatcher rd = request.getRequestDispatcher("applyleave.jsp");
						rd.include(request, response);
						out.println(
								"<p style= 'color:red ;' align = center >YOU ARE IN PROPAGATION PERIOD , cabtexcess leave for  "
										+ leaveType + "</p>");

						throw new Exception("YOU ARE IN PROPAGATION PERIOD , cantexcess leave for " + leaveType);
					}
				
					perimissionManagement.setEmployeeNumber(username);
					perimissionManagement.setLeaveId(leaveId);
					perimissionManagement.setLeavePeriod(leavePeriod);
					perimissionManagement.setLeaveTaken(leaveCount);
					if (!leaveType.equals("L.O.P")) {
						if (givenLeaveCount < leaveCount) {
							RequestDispatcher rd = request.getRequestDispatcher("applyleave.jsp");
							rd.include(request, response);
							out.println("<p style= 'color:red ;' align = center >You dont have any excess leave for   "
									+ leaveType + "</p>");
							throw new Exception("You dont have any excess leave for " + leaveType);
						}
						int validException = validateAvaliableLeave(username, leaveId, endDate, session);
						if (validException > 0) {
							RequestDispatcher rd = request.getRequestDispatcher("applyleave.jsp");
							rd.include(request, response);
							out.println("<p style= 'color:red ;' align = center >You dont have any excess leave for   "
									+ leaveType + "</p>");
							throw new Exception("Dont Have leave");
						}
						PerimissionManagement existingLeaveEmplyee = leaveBalance(username, leaveId, session);
						if (Objects.nonNull(existingLeaveEmplyee)) {
							int exleavecount = existingLeaveEmplyee.getLeaveCount();
							perimissionManagement.setLeaveCount(exleavecount - leaveCount);

						} else {
							perimissionManagement.setLeaveCount(givenLeaveCount - leaveCount);

						}
					} else {
						perimissionManagement.setLeaveCount(0);

					}

				}
			}
			Transaction transcation = session.beginTransaction();
			SupervisoryImpl supervisoryImpl = new SupervisoryImpl();
			Supervisory supervisory = supervisoryImpl.getSupervisoryByEmployeeNumberAll(username);
			perimissionManagement.setSupervisoryId(supervisory.getSupervisoryId());
			perimissionManagement.setSupervisoryNumber(supervisory.getSupervisoryNumber());
			perimissionManagement.setComments("");
			session.save(perimissionManagement);
			transcation.commit();
			RequestDispatcher rd = request.getRequestDispatcher("applyleave.jsp");
			rd.include(request, response);
			out.println("<p style= 'color:red ;' align = center >Successfully applyed Leave </p>");
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
	private List<Leave> getLeaveByLeaveType(String leaveType, Session session) {
		List<Leave> leaveList = new ArrayList<>();
		try {
			Transaction transcation = session.beginTransaction();

			Query query = session.createSQLQuery("SELECT * FROM docto.leave where leave_type = '" + leaveType + "' ");
			List<Object[]> objectList = query.list();

			objectList.stream().forEach(p -> {
				Leave leave = new Leave();

				leave.setLeaveId((int) p[0]);
				leave.setLeaveType((String) p[1]);
				leave.setLeavePeriod((String) p[2]);
				leave.setLeaveCount((String) p[3]);
				leave.setValid((int) p[4]);
				leaveList.add(leave);
			});
			transcation.commit();
		} catch (Exception e) {
			System.out.println(e);
		}
		return leaveList;
	}

	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	private int validatePropagationPeriod(String leaveType, String username, Session session, String startDate,
			String endDate) {
		int propagationPeriod = 0;

		try {
			Transaction transcation = session.beginTransaction();
			if (!leaveType.equals("L.O.P")) {

				Query query = session.createSQLQuery(
						"SELECT date_of_joining FROM docto.employee where employee_number='" + username + "'");
				List<Object> list = query.list();
				String dateOfJoining = (String) list.get(0);
				LocalDate date = LocalDate.parse(dateOfJoining);
				LocalDate date1 = LocalDate.parse(startDate);
				LocalDate date2 = LocalDate.parse(endDate);
				Month monthofJoining = date.getMonth();
				int j = monthofJoining.getValue();
				Month leaveFrom = date1.getMonth();
				Month leaveTo = date2.getMonth();
				Month afterFourMonthFrom = leaveFrom.plus(4);
				int i = afterFourMonthFrom.getValue();
				Month afterFourMonthTo = leaveTo.plus(4);
				int k = afterFourMonthTo.getValue();
				if (leaveType.equals("C.L") && (monthofJoining.equals(leaveTo) || monthofJoining.equals(leaveFrom))) {
					propagationPeriod++;
				} else if (leaveType.equals("S.L") && ((j - i) <= 4 || (i - j) <= 4 || (j - k) <= 4 || (k - k) <= 4)) {
					propagationPeriod++;
				}
			}
			transcation.commit();
		} catch (Exception e) {
			System.out.println(e);
		}

		return propagationPeriod;
	}

	@SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
	private PerimissionManagement leaveBalance(String username, int leaveId, Session session) {
		List<PerimissionManagement> permissionManagementList = new ArrayList<>();
		PerimissionManagement pm = null;
		try {
			Transaction transcation = session.beginTransaction();
			Query query = session.createSQLQuery("select * from docto.permission_management where employee_number='"
					+ username + "' and leave_id='" + leaveId + "'and leave_count !=0 order by leave_count desc");
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
				permissionManagementList.add(perimissionManagement);
			});
			pm = permissionManagementList.get(0);
			transcation.commit();
		} catch (Exception e) {
			System.out.println(e);
		}
		return pm;
	}

	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	private int validateAvaliableLeave(String username, int leaveId, String endDate, Session session) {

		int count = 0;
		List<PerimissionManagement> permissionManagementList = new ArrayList<>();
		try {
			Transaction transcation = session.beginTransaction();
			Query query = session.createSQLQuery("select * from docto.permission_management where employee_number='"
					+ username + "' and leave_id='" + leaveId + "'");
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

				List<PerimissionManagement> leaveMonthOver = permissionManagementList.stream()
						.filter(p -> p.getLeaveCount() == 0 && p.getLeavePeriod().equals("MONTH"))
						.collect(Collectors.toList());
				if (leaveMonthOver.size() > 0) {
					List<String> endDateList = permissionManagementList.stream().map(PerimissionManagement::getEndDate)
							.collect(Collectors.toList());
					for (String date : endDateList) {
						Month m = LocalDate.parse(date).getMonth();
						Month m2 = LocalDate.parse(endDate).getMonth();
						if (m.equals(m2)) {
							count++;
						}
					}

				}
				List<PerimissionManagement> leaveYearOver = permissionManagementList.stream()
						.filter(p -> p.getLeaveCount() == 0 && p.getLeavePeriod().equals("YEAR"))
						.collect(Collectors.toList());
				if (leaveYearOver.size() > 0) {
					List<String> endDateList = permissionManagementList.stream().map(PerimissionManagement::getEndDate)
							.collect(Collectors.toList());
					for (String date : endDateList) {
						int y = LocalDate.parse(date).getYear();
						int currentDate = LocalDate.parse(endDate).getYear();
						if (y == currentDate) {
							count++;
						}
					}
				}
			}
			transcation.commit();
		} catch (Exception e) {
			System.out.println(e);
		}
		return count;
	}

	private int dateCount(String date1, String date2) {

		Period period = Period.between(LocalDate.parse(date1), LocalDate.parse(date2));
		int daysCount = period.getDays() + 1;
		System.out.println(daysCount);
		return daysCount;
	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	private int validateDate(String startDate, String endDate, String employeeNumber, Session session)
			throws Exception {

		int count = 0;
		LocalDate date1 = LocalDate.parse(startDate);
		LocalDate date2 = LocalDate.parse(endDate);
		LocalDate currentDate = LocalDate.now();
		if (date1.isBefore(currentDate) || date2.isBefore(date1)) {
			count++;
		}
		try {
			Transaction transcation = session.beginTransaction();
			Query query = session.createSQLQuery("SELECT * FROM docto.permission_management where employee_number = '"
					+ employeeNumber + "' AND (start_date BETWEEN  '" + startDate + "' AND '" + endDate + "') or "
					+ " (end_date BETWEEN  '" + startDate + "' AND '" + endDate + "');");
			List list = query.list();
			if (list.iterator().hasNext()) {
				count++;
			}
			transcation.commit();
		} catch (Exception e) {
			System.out.println(e);
		}
		return count;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);

	}

	@SuppressWarnings({ "unchecked", "deprecation", "rawtypes" })
	public static List<String> getAllLeaveType() {
		List<Leave> leaveList = new ArrayList<>();
		List<String> map = new ArrayList<>();
		try {
			Session session = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory().openSession();
			Transaction transcation = session.beginTransaction();
			Query query = session.createSQLQuery("select * From docto.leave");
			List<Object[]> objectList = query.list();
			objectList.stream().forEach(p -> {
				Leave leave = new Leave();
				leave.setLeaveId((int) p[0]);
				leave.setLeaveType((String) p[1]);
				leave.setLeavePeriod((String) p[2]);
				leave.setLeaveCount((String) p[3]);
				leave.setValid((int) p[4]);
				leaveList.add(leave);
			});
			map = leaveList.stream().map(Leave::getLeaveType).collect(Collectors.toList());
			System.out.println(map);
			transcation.commit();
		} catch (Exception e) {
			System.out.println(e);
		}
		return map;
	}
}
