package edu.handong.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import edu.handong.analysis.datamodel.Course;
import edu.handong.analysis.datamodel.Student;
import edu.handong.analysis.utils.NotEnoughArgumentException;
import edu.handong.analysis.utils.Utils;
import java.util.Set;

public class HGUCoursePatternAnalyzer {

	private HashMap<String,Student> students;
	
	/**
	 * This method runs our analysis logic to save the number courses taken by each student per semester in a result file.
	 * Run method must not be changed!!
	 * @param args
	 */
	public void run(String[] args) {
		
		try {
			// when there are not enough arguments from CLI, it throws the NotEnoughArgmentException which must be defined by you.
			if(args.length<2)
				throw new NotEnoughArgumentException();
		} catch (NotEnoughArgumentException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
		String dataPath = args[0]; // csv file to be analyzed
		String resultPath = args[1]; // the file path where the results are saved.
		
		String startYear = args[2];
		String endYear = args[3];
		
		ArrayList<String> lines = Utils.getLines(dataPath, true);
		
		students = loadStudentCourseRecords(lines);
		
		// To sort HashMap entries by key values so that we can save the results by student ids in ascending order.
		Map<String, Student> sortedStudents = new TreeMap<String,Student>(students); 
		
		// Generate result lines to be saved.
		ArrayList<String> linesToBeSaved = countNumberOfCoursesTakenInEachSemester(sortedStudents, startYear, endYear);
		
		// Write a file (named like the value of resultPath) with linesTobeSaved.
		Utils.writeAFile(linesToBeSaved, resultPath);
	}
	
	/**
	 * This method create HashMap<String,Student> from the data csv file. Key is a student id and the corresponding object is an instance of Student.
	 * The Student instance have all the Course instances taken by the student.
	 * @param lines
	 * @return
	 */
	private HashMap<String,Student> loadStudentCourseRecords(ArrayList<String> lines) {
		HashMap<String,Student> map = new HashMap<String,Student>();
		String curr = null;
		String prev = null;
		String studentid = null;
		
		for(int i = 0; i < lines.size(); i++) {
			curr = lines.get(i).split(",")[0];
			// put into the hashmap if it is different student
			if(prev != curr) {
				Student student = new Student(curr);
				map.put(curr, student);
			}
			prev = curr;
		}
		
		//create course instance for each student instance
		studentid = lines.get(0).split(",")[0];
		
		for(int i = 0; i < lines.size(); i++) {
			curr = lines.get(i).split(",")[0];
			if(studentid != curr) {
				studentid = curr;
			}
			Course NewRecord = new Course(lines.get(i));
			map.get(studentid).addCourse(NewRecord);
		}
		
		return map; // do not forget to return a proper variable.
	}

	/**
	 * This method generate the number of courses taken by a student in each semester. The result file look like this:
	 * StudentID, TotalNumberOfSemestersRegistered, Semester, NumCoursesTakenInTheSemester
	 * 0001,14,1,9
     * 0001,14,2,8
	 * ....
	 * 
	 * 0001,14,1,9 => this means, 0001 student registered 14 semeters in total. In the first semeter (1), the student took 9 courses.
	 * 
	 * 
	 * @param sortedStudents
	 * @return
	 */
	private ArrayList<String> countNumberOfCoursesTakenInEachSemester(Map<String, Student> sortedStudents, String startYear, String endYear) {
		
		ArrayList<String> result = new ArrayList<String>();
		int idx = 1;
		int allSmstr = 0;
		int numOfClss = 0;
		Set<String> ks = sortedStudents.keySet(); // store keys of HashMap 'sortedStudents' 
		
		Map<String, Integer> tempHash = null;
		int sta = 0;
		int end = 0;
		int size = 0;
		int diff = 0;
		int sDiff = 0;
		int eDiff = 0;
		
		result.add(0, "StudentID, TotalNumberOfSemestersRegistered, Semester, NumCoursesTakenInTheSemester");
		
		for(String stdId : ks) {
			size = sortedStudents.get(stdId).getSemestersByYearAndSemester().size();
			sta = (sortedStudents.get(stdId).getYearOfNthSemester(1)) - Integer.parseInt(startYear);
			end = Integer.parseInt(endYear) - (sortedStudents.get(stdId).getYearOfNthSemester(size));
			
			if(sta >= 0 && end >= 0) { //case 1 : 
				allSmstr = sortedStudents.get(stdId).getSemestersByYearAndSemester().size();
				
				for(int i = 1; i <= allSmstr; i++) {
					numOfClss = sortedStudents.get(stdId).getNumCourseInNthSemester(i);
					result.add(idx, stdId + "," + Integer.toString(allSmstr) + "," + Integer.toString(i) + "," + Integer.toString(numOfClss));
					idx++;
				}
				
			}else if(sta < 0 && end >= 0) { //case 2:
				tempHash = sortedStudents.get(stdId).getSemestersByYearAndSemester();
				
				diff = (tempHash.get(startYear + "-1")) - 1;
				allSmstr = size - diff;
				
				for(int i = 1; i <= allSmstr; i++) {
					numOfClss = sortedStudents.get(stdId).getNumCourseInNthSemester(i + diff);
					result.add(idx, stdId + "," + Integer.toString(allSmstr) + "," + Integer.toString(i) + "," + Integer.toString(numOfClss));
					idx++;
				}
			}else if(sta >= 0 && end < 0) { // case 3:
				int ey = Integer.parseInt(endYear) + 1;
				
				tempHash = (Map<String, Integer>)sortedStudents.get(stdId).getSemestersByYearAndSemester().clone();
				allSmstr = sortedStudents.get(stdId).getSemestersByYearAndSemester().size();
				String tempString = String.valueOf(ey) + "-1";
				int tempor = tempHash.get(tempString) - 1;
				diff = size - tempor;
				allSmstr = allSmstr - diff;
				
				for(int i = 1; i <= allSmstr; i++) {
					numOfClss = sortedStudents.get(stdId).getNumCourseInNthSemester(i);
					result.add(idx, stdId + "," + Integer.toString(allSmstr) + "," + Integer.toString(i) + "," + Integer.toString(numOfClss));
					idx++;
				}
				
			}else { // case 4:
				tempHash = (Map<String, Integer>)sortedStudents.get(stdId).getSemestersByYearAndSemester().clone();
				int ey = Integer.parseInt(endYear) + 1;
				String tempstring = startYear + "-" + "1";
				sDiff = (tempHash.get(tempstring)) - 1;
				eDiff = size - ((tempHash.get(String.valueOf(ey) + "-1")) - 1);
				
				allSmstr = size - eDiff - sDiff;
				
				for(int i = 1 + sDiff; i <= size - eDiff; i++) {
					numOfClss = sortedStudents.get(stdId).getNumCourseInNthSemester(i);
					result.add(idx, stdId + "," + Integer.toString(allSmstr) + "," + Integer.toString(i - sDiff) + "," + Integer.toString(numOfClss));
					idx++;
				}
			}
			
		}
		
		
		return result; // do not forget to return a proper variable.
	}
}
