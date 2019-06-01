package edu.handong.analysis;

//import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class Runner {
	
	String input;
	String output;
	
	String analysis;
	String coursecode;
	
	String startyear;
	String endyear;
	
	boolean help;

	public void run(String[] args) {
		Options options = createOptions();
		
		if(parseOptions(options, args)){
			if (help){
				printHelp(options);
				return;
			}
			
			if(analysis.equals("1")) { // Count courses per 'semester'
				System.out.println("Counting courses per semeter");
				System.out.println("You provided \"" + input + "\" as the value of the option i");
				System.out.println("You provided \"" + output + "\" as the value of the option o");
				
				HGUCoursePatternAnalyzer analyzer = new HGUCoursePatternAnalyzer();
				
				String[] arg = {input, output, startyear, endyear};
				analyzer.run(arg);
				//
				
				
			}else if(analysis.equals("2")) { // Count courses per 'course name and year'
				System.out.println("Count courses per 'course name and year'");
				System.out.println("Not programmed yet");
				return;
				//
				
			}else {
				printHelp(options);
				return;
			}
		}
	}
	
	// Parsing Stage
	private boolean parseOptions(Options options, String[] args) {
		CommandLineParser parser = new DefaultParser();

		try {

			CommandLine cmd = parser.parse(options, args);

			input = cmd.getOptionValue("i");
			output = cmd.getOptionValue("o");
			coursecode = cmd.getOptionValue("c");
			startyear = cmd.getOptionValue("s");
			endyear = cmd.getOptionValue("e");
			analysis = cmd.getOptionValue("a");
			help = cmd.hasOption("h");
			
		} catch (Exception e) {
			printHelp(options);
			return false;
		}

		return true;
	}

	// Definition Stage
	private Options createOptions() {
		Options options = new Options();

		// add options by using OptionBuilder
		options.addOption(Option.builder("i").longOpt("input")
				.required()
				.hasArg()
				.argName("Input path")
				.desc("Set an input file path")
				.build());

		options.addOption(Option.builder("o").longOpt("output")
				.required()
				.hasArg()
				.argName("Output path")
				.desc("Set an output file path")
				.build());
		
		options.addOption(Option.builder("a").longOpt("analysis")
				.required()
				.hasArg()
				.argName("Analysis option")
				.desc("1: Count courses per semester, 2: Count per course name and year")
				.build());
		/*
		options.addOption(Option.builder("c").longOpt("coursecode")
				//.required()
				.hasArg()
				.argName("Course code")
				.desc("Course code for '-a 2' option")
				.build());
		*/
		options.addOption(Option.builder("s").longOpt("startyear")
				.required()
				.hasArg()
				.argName("Start year for analysis")
				.desc("Set the start year for analysis e.g., -s 2002")
				.build());
		
		options.addOption(Option.builder("e").longOpt("endyear")
				.required()
				.hasArg()
				.argName("End year for analysis")
				.desc("Set the end year for analysis e.g., -s 2005")
				.build());
		
		options.addOption(Option.builder("h").longOpt("help")
				.argName("Help")
		        .desc("Show a Help page")
		        .build());

		return options;
	}
	
	// Unexpected error from Parsing stage Execute it and finish program 
	private void printHelp(Options options) {
		// automatically generate the help statement
		HelpFormatter formatter = new HelpFormatter();
		String header = "HGU Course Analyzer";
		String footer =""; // Leave this empty
		formatter.printHelp("HGUCourseCounter", header, options, footer, true);
	}

}
