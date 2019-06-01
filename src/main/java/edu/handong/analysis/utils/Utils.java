package edu.handong.analysis.utils;

import java.util.ArrayList;

import java.util.Scanner;
import java.io.File;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;


import java.io.FileReader;
import java.io.Reader;

public class Utils {
	
	/*
	// store each lines of passed data.csv file into the ArrayList fileLines. 
		public static ArrayList<String> getLines(String file,boolean removeHeader){
			ArrayList<String> fileLines = new ArrayList<String>();
			
			Reader in = null;
			try {
				in = new FileReader(file);
			} catch(FileNotFoundException e) {
				System.out.println("The file path does not exist. Please check your CLI argument! " + file);
				System.exit(0);
			}
			
			int i = 0, j = 0;
			String temp;
			Iterable<CSVRecord> records = null;
			try {
				records = CSVFormat.RFC4180.parse(in);
			} catch(IOException d) {
				System.out.println("Error opening the file" + in);
				System.exit(0);
			}
			for (CSVRecord record : records) {
				if(removeHeader) {
					j++;
					removeHeader = false;
				}
				temp = record.get(j);
				fileLines.add(i,temp);  
			}
			return fileLines;
		}
	*/
	
	// store each lines of passed data.csv file into the ArrayList fileLines. 
	public static ArrayList<String> getLines(String file,boolean removeHeader){
		ArrayList<String> fileLines = new ArrayList<String>();
		Scanner inputStream = null;
		try {
			inputStream = new Scanner(new File(file));
		} catch(FileNotFoundException e) {
			System.out.println("The file path does not exist. Please check your CLI argument! " + file);
			System.exit(0);
		}
		
		for(int i = 0; inputStream.hasNextLine(); i++) {
			if(removeHeader) {
				inputStream.nextLine(); // go through one line without storing 
				removeHeader = false;
			}
			fileLines.add(i,inputStream.nextLine());
		}
		inputStream.close();
		return fileLines;
	}
	
	
	
	public static void writeAFile(ArrayList<String> lines, String targetFileName) {
		PrintWriter outputStream = null;
		try {
			outputStream = new PrintWriter(targetFileName);
		} catch(FileNotFoundException e) {
			System.out.println("Error opening the file" + targetFileName + ". Creating directory.");
			Path path = Paths.get(targetFileName);
			try {
				path = Files.createFile(path);
			} catch(IOException d) {
				System.out.println("Error creating the file" + targetFileName); 
				System.exit(0);
			}
			try {
				outputStream = new PrintWriter(targetFileName);
			} catch(FileNotFoundException f) {
				System.out.println("Error opening the file" + targetFileName);
				System.exit(0);
			}
		}
		for(int i = 0; i < lines.size(); i++) {
			outputStream.println(lines.get(i));
		}
		outputStream.close();
	}
	
}
