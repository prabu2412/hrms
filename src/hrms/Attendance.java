package hrms;

public class Attendance {

	private int attendanceId;
	
	private String date;
	
	private String inTime;
	
	private String outTime;
	
	private String cumulativeTime;
	
	private String attendanceStatus;
	
	private String employeeNumber;
	
	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public int getAttendanceId() {
		return attendanceId;
	}

	public void setAttendanceId(int attendanceId) {
		this.attendanceId = attendanceId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getInTime() {
		return inTime;
	}

	public void setInTime(String inTime) {
		this.inTime = inTime;
	}

	public String getOutTime() {
		return outTime;
	}

	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}

	public String getCumulativeTime() {
		return cumulativeTime;
	}

	public void setCumulativeTime(String cumulativeTime) {
		this.cumulativeTime = cumulativeTime;
	}

	public String getAttendanceStatus() {
		return attendanceStatus;
	}

	public void setAttendanceStatus(String attendanceStatus) {
		this.attendanceStatus = attendanceStatus;
	}
	
	
}
