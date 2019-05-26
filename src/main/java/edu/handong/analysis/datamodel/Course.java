package edu.handong.analysis.datamodel;

public class Course {

	private String studentId;
	private String yearMonthGraduated;
	private String firstMajor;
	private String secondMajor;
	private String courseCode;
	private String courseName;
	private String courseCredit;
	private int yearTaken;
	private int semesterCourseTaken;
		
	public Course(String line) {
		studentId = line.split(",")[0];
		yearMonthGraduated = line.split(",")[1];
		firstMajor = line.split(",")[2];
		secondMajor = line.split(",")[3];
		courseCode = line.split(",")[4];
		courseName = line.split(",")[5];
		courseCredit = line.split(",")[6];
		yearTaken = Integer.parseInt(line.split(",")[7].trim());
		semesterCourseTaken = Integer.parseInt(line.split(",")[8].trim());
	}
	
	public int getyearTaken() {
		return yearTaken;
	}
	
	public int getsemesterCourseTaken() {
		return semesterCourseTaken;
	}
	
}
