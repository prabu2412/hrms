package hrms;

public class PerimissionManagement {

	int permissionManagementId;
	
	String startDate,endDate,reason,status;
	
	int leaveTaken;
	
	String leavePeriod;
	
     int leaveId,supervisoryId;
	
	String employeeNumber,supervisoryNumber,comments;
	
	int leaveCount;
	

	
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public int getSupervisoryId() {
		return supervisoryId;
	}

	public void setSupervisoryId(int supervisoryId) {
		this.supervisoryId = supervisoryId;
	}

	public String getSupervisoryNumber() {
		return supervisoryNumber;
	}

	public void setSupervisoryNumber(String supervisoryNumber) {
		this.supervisoryNumber = supervisoryNumber;
	}

	public String getLeavePeriod() {
		return leavePeriod;
	}

	public void setLeavePeriod(String leavePeriod) {
		this.leavePeriod = leavePeriod;
	}

	public int getLeaveTaken() {
		return leaveTaken;
	}

	public void setLeaveTaken(int leaveTaken) {
		this.leaveTaken = leaveTaken;
	}

	public int getPermissionManagementId() {
		return permissionManagementId;
	}

	public void setPermissionManagementId(int permissionManagementId) {
		this.permissionManagementId = permissionManagementId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public int getLeaveId() {
		return leaveId;
	}
	public void setLeaveId(int leaveId) {
		this.leaveId = leaveId;
	}
	
	public String getEmployeeNumber() {
		return employeeNumber;
	}
	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public int getLeaveCount() {
		return leaveCount;
	}
	public void setLeaveCount(int leaveCount) {
		this.leaveCount = leaveCount;
	}	
	
	
}
