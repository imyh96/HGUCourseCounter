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
		ArrayList<String> lines = Utils.getLines(dataPath, true);
		
		students = loadStudentCourseRecords(lines);
		
		// To sort HashMap entries by key values so that we can save the results by student ids in ascending order.
		Map<String, Student> sortedStudents = new TreeMap<String,Student>(students); 
		
		// Generate result lines to be saved.
		ArrayList<String> linesToBeSaved = countNumberOfCoursesTakenInEachSemester(sortedStudents);
		
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
	private ArrayList<String> countNumberOfCoursesTakenInEachSemester(Map<String, Student> sortedStudents) {
		
		ArrayList<String> result = new ArrayList<String>();
		int idx = 1;
		int allSmstr = 0;
		int numOfClss = 0;
		Set<String> ks = sortedStudents.keySet();
		
		result.add(0, "StudentID, TotalNumberOfSemestersRegistered, Semester, NumCoursesTakenInTheSemester");
		
		for(String stdId : ks) {
			
			allSmstr = sortedStudents.get(stdId).getSemestersByYearAndSemester().size();
			
			for(int i = 1; i <= allSmstr; i++) {
				numOfClss = sortedStudents.get(stdId).getNumCourseInNthSemester(i);
				result.add(idx, stdId + "," + Integer.toString(allSmstr) + "," + Integer.toString(i) + "," + Integer.toString(numOfClss));
				idx++;
			}
		}
		
		
		return result; // do not forget to return a proper variable.
	}
}
