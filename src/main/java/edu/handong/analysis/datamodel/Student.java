package edu.handong.analysis.datamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Student {
	
	private String studentId;
	private ArrayList<Course> coursesTaken = new ArrayList<Course>(); // List of courses Student has taken
	private HashMap<String,Integer> semestersByYearAndSemester = new HashMap<String,Integer>(); // key: Year-Semester
																								// e.g., 2003-1,
	
	public Student(String studentId) {
		this.studentId = studentId;
	}
	
	public void addCourse(Course NewRecord) {
		coursesTaken.add(NewRecord);
	}
	
	public HashMap<String,Integer> getSemestersByYearAndSemester(){
		
		String prev = null;
		String curr = null;
		int size = coursesTaken.size();
		
		// 하나씩 HashMap에 집어넣기
		for(int i = 0, j = 1; i < size; i++) {
			curr = Integer.toString(coursesTaken.get(i).getyearTaken()) + "-" + Integer.toString(coursesTaken.get(i).getsemesterCourseTaken());
			
			if(!(curr.equals(prev))) {
				semestersByYearAndSemester.put(curr, j);
				prev = curr;
				j++;
			}
		}
		
		return semestersByYearAndSemester;
	}
	
	public int getNumCourseInNthSemester(int semester) {
		Set<String> ks = semestersByYearAndSemester.keySet();
		int cnt = 0;
		int year = 0;
		int smstr = 0;
		int size = coursesTaken.size();
		
		// get year and semester number for passed Nth semester
		for(String n : ks) { 
			if(semestersByYearAndSemester.get(n) == semester) {
				year = Integer.parseInt(n.split("-")[0]);
				smstr = Integer.parseInt(n.split("-")[1]);
				break;
			}	
		}
		
		// go through coursesTaken ArrayList to find corresponding course and increase counter
		for(int i = 0; i < size; i++){
			if(year == coursesTaken.get(i).getyearTaken() && smstr == coursesTaken.get(i).getsemesterCourseTaken())
				cnt++;
		}
		
		return cnt;
	}
	
	public int getYearOfNthSemester(int semester) {
		Set<String> ks = semestersByYearAndSemester.keySet();
		int year = 0;
		
		// get year number for passed Nth semester
		for(String n : ks) { 
			if(semestersByYearAndSemester.get(n) == semester) {
				year = Integer.parseInt(n.split("-")[0]);
				break;
			}	
		}
		
		return year;
	}
	
	public int getsemestersByYearAndSemester(String key) {
		return semestersByYearAndSemester.get(key);
	}

}
