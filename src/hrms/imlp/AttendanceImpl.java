package hrms.imlp;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
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

import hrms.Attendance;


@WebServlet("/attendance")
public class AttendanceImpl extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
   		doPost(request, response);
   	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		Attendance attendances = null;
		Session session = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory().openSession();
		try {
			
			HttpSession sess = request.getSession();
			String employeeNumber = (String)sess.getAttribute("userName");
			Attendance attendance = new Attendance();
			String btn = request.getParameter("time");
			LocalDate localDate = LocalDate.now();
			LocalDateTime fromDateTime  = LocalDateTime.now();
			if(btn.equals("In Time")) {
				attendance.setDate(String.valueOf(localDate));
				attendance.setInTime(fromDateTime.toString());
				attendance.setEmployeeNumber(employeeNumber);
				Transaction transcation = session.beginTransaction();
				session.save(attendance);
				RequestDispatcher rd = request.getRequestDispatcher("attendance.jsp");
				rd.include(request, response);
				out.println("<p style= 'color:red ;' align = center > You have Pressed In. Have a happy day</p>");
				
				transcation.commit();

			}else {
				int count = checkInTime(localDate.toString(),employeeNumber,session);
				if(count>0) {
					RequestDispatcher rd = request.getRequestDispatcher("attendance.jsp");
					rd.include(request, response);
					out.println("<p style= 'color:red ;' align = center > Please press in Time. IN Time is not avaliable </p>");
					throw new Exception("Please press in Time. IN Time is not avaliable");
				}
				attendances = validateOutTime(fromDateTime.toString(),employeeNumber,session,localDate);
				Transaction transcation = session.beginTransaction();
				
				session.saveOrUpdate(attendances);
				RequestDispatcher rd = request.getRequestDispatcher("attendance.jsp");
				rd.include(request, response);
				out.println("<p style= 'color:red ;' align = center > You have Pressed Out Time. Thank you for coming</p>");
				transcation.commit();
			}
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	private int checkInTime(String date, String employeeNumber,Session session) {
      int count = 0;
      try {
    	  
    	  Transaction transcation = session.beginTransaction();
    	  Query query = session.createSQLQuery("SELECT * FROM docto.attendance where date = '"+date+"' and in_time is not null and out_time is  null");
    	  List<Object> object = query.list();
    	  if(object.size()==0) {
    		  count++;
    	  }
    	  transcation.commit();
	} catch (Exception e) {
         System.out.println(e);
	}
		return count;
	}

	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	private Attendance validateOutTime(String toDateTimeString, String employeeNumber,Session session,LocalDate localDate) {
		 Attendance attendance = new Attendance();
		 List<Attendance> attendanceList = new ArrayList();
		try {
			Transaction transcation = session.beginTransaction();
			Query query = session.createSQLQuery("SELECT * FROM docto.attendance where employee_number='"+employeeNumber+"' AND out_time is null AND in_time is not null AND date = '"+localDate.toString()+"'");
			List<Object[]> object = query.list();
			object.stream().forEach(o->{
				 Attendance attendances = new Attendance();
				attendances.setAttendanceId((int)o[0]);
				attendances.setDate((String)o[1]);
				attendances.setInTime((String)o[2]);
				attendances.setEmployeeNumber((String)o[6]);
				attendanceList.add(attendances);
			});
			attendance.setAttendanceId(attendanceList.get(0).getAttendanceId());
			attendance.setDate(attendanceList.get(0).getDate());
			attendance.setInTime(attendanceList.get(0).getInTime());
			attendance.setEmployeeNumber(attendanceList.get(0).getEmployeeNumber());
			attendance.setOutTime(toDateTimeString);
	    	transcation.commit();
	    	String cumulativeTiming = calculateCumulativeReadingForStatus(session,localDate,employeeNumber,toDateTimeString,attendance.getInTime());
			attendance.setCumulativeTime(cumulativeTiming);
			String attendanceStatus = setAttendanceStatus(cumulativeTiming);
			attendance.setAttendanceStatus(attendanceStatus);
		} catch (Exception e) {
			System.out.println(e);
		}
		return attendance;	
	}
	private String setAttendanceStatus(String cumulativeReading) {
		String attendanceStatus = null;
		String[] splitTime = cumulativeReading.split(":");
		int hours = Integer.parseInt(splitTime[0]);
		int minutes = Integer.parseInt(splitTime[1]);
		if(hours >=4 && hours <=8) {
			attendanceStatus = "Half Day";
		}else if(hours >=8 && minutes >=15) {
			attendanceStatus = "Full Day";
			System.out.println("Full Day");
		}else if(hours<=4) {
			attendanceStatus = "LOW WORKING HOURS";
			System.out.println("LOW WORKING HOURS");
		}else {
			attendanceStatus = "LOW WORKING HOURS";
		}
		return attendanceStatus;
	}
	private String inAndOutCalculation(String inTime, String toDateTimeString) {
		
		 StringBuilder stringBuilder = new StringBuilder();
	    if(Objects.nonNull(inTime) && Objects.nonNull(toDateTimeString)) {
		LocalDateTime tempDateTime = LocalDateTime.from( LocalDateTime.parse(inTime));
		LocalDateTime toDateTime  =LocalDateTime.parse(toDateTimeString);
		long hours = tempDateTime.until( toDateTime, ChronoUnit.HOURS);
    	tempDateTime = tempDateTime.plusHours( hours );
    	String hoursString = String.valueOf(hours);
    	long minutes = tempDateTime.until( toDateTime, ChronoUnit.MINUTES);
    	tempDateTime = tempDateTime.plusMinutes( minutes );
    	String minutesString = String.valueOf(minutes);
    	long seconds = tempDateTime.until( toDateTime, ChronoUnit.SECONDS);
    	String secondsString = String.valueOf(seconds);
    	stringBuilder.append(hoursString).append(":").append(minutesString).append(":").append(secondsString);
		 }
		return stringBuilder.toString();
	}
 
	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	private String calculateCumulativeReadingForStatus(Session session, LocalDate localDate, String employeeNumber,
			String outTime,String inTime) {
    	String cumulativeReading = null;
		 List<Attendance> attendanceList = new ArrayList();
		try {
			Transaction transcation = session.beginTransaction();
			Query query = session.createSQLQuery("SELECT * FROM docto.attendance where  date ='"+localDate +"' ");
			List<Object[]> object = query.list();
			object.stream().forEach(o->{
				 Attendance attendance = new Attendance();	
				attendance.setAttendanceId((int)o[0]);
				attendance.setDate((String)o[1]);
				attendance.setInTime((String)o[2]);
				attendance.setOutTime((String)o[3]);
				attendance.setEmployeeNumber((String)o[6]);
				attendanceList.add(attendance);
			});
			List<String> totalTimeList = new ArrayList<>();
			 attendanceList.stream().filter(i->i.getOutTime()!=null).forEach(q->{
				 String cumulative = inAndOutCalculation(q.getInTime(),q.getOutTime());
			   totalTimeList.add(cumulative);
				});
			//int outTimeCount = attendanceList.size();
			//String outTime = attendanceList.stream().map(Attendance::getOutTime).skip(outTimeCount-1).findFirst().get();
	          String reading = inAndOutCalculation(inTime,outTime);
	          totalTimeList.add(reading);
              String hours = null;
	          int totalHours = 0 ;
			  String minutes ;
			  int totalMinutes = 0;
			for(String s : totalTimeList) {
				String[] h = s.split(":");
				hours = h[0];
				totalHours = totalHours+Integer.parseInt(hours);
				minutes = h[1];
				totalMinutes = totalMinutes+Integer.parseInt(minutes);
				}
	          String minToHrsString = null;
	          minToHrsString =  fromMinutesToHHmm(totalMinutes);
		
	          String[] mh = minToHrsString.split(":");
	          if(Integer.parseInt(hours) != 0) {
	        	   
	        	   int mHoursInt = Integer.parseInt(mh[0]);
	        	   int mHours = totalHours + mHoursInt;
	        	   cumulativeReading = new StringBuilder().append(mHours).append(":").append(mh[1]).toString();
	          }else {
	        	  cumulativeReading = minToHrsString;
	          }
	          
			transcation.commit();
		} catch (Exception e) {
            System.out.println(e);
		}

		return cumulativeReading;
	}
	public  String fromMinutesToHHmm(int minutes) {
	    long hours = TimeUnit.MINUTES.toHours(Long.valueOf(minutes));
	    long remainMinutes = minutes - TimeUnit.HOURS.toMinutes(hours);
	    return String.format("%02d:%02d", hours, remainMinutes);
	
		}
    // HAVE TO INCULUDE IN PROJECT FOR SECINDS TO MINUTES CONVERSION
	public  String fromSecondsTommss(int seconds) {
	    long minutes = TimeUnit.SECONDS.toMinutes(Long.valueOf(seconds));
	    long remainSeconds = minutes - TimeUnit.MINUTES.toSeconds(minutes);
	    return String.format("%02d:%02d", minutes, remainSeconds);
	
		}
	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	   public static List<Attendance> getAllAttendance(String employeeNumber){
		   List<Attendance> attendanceList = new ArrayList<>();
		   try {
			Session session =  new Configuration().configure("hibernate.cfg.xml").buildSessionFactory().openSession();
			Transaction transcation = session.beginTransaction(); 
			Query query = session.createSQLQuery("SELECT * FROM docto.attendance where employee_number='"+employeeNumber +"'");
			List<Object[]> object = query.list();
			object.stream().forEach(j->{
				Attendance attendance = new Attendance();
				attendance.setAttendanceId((int)j[0]);
				attendance.setDate((String)j[1]);
				attendance.setInTime((String)j[2]);
				attendance.setOutTime((String)j[3]);
				attendance.setCumulativeTime((String)j[4]);
				attendance.setAttendanceStatus((String)j[5]);
				attendance.setEmployeeNumber((String)j[6]);
				attendanceList.add(attendance);
			});
		
			  transcation.commit();
		} catch (Exception e) {
	        System.out.println(e);
		}
		   return attendanceList;
	   }
}
