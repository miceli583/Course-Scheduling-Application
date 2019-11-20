To update lists of semesters and departments:

	Compile: javac -d . SemDepListBuilder.java
	Run: java SemDepListBuilder

2 lists (Semesters.txt and Departments.txt) will be generated in the directory SemDepLists.
They are necessary for the execution of CourseScraper.

To update all course info for all semesters:
	
	Compile: javac -d . CourseScraper.java
	Run: java CourseScraper

Requires Semesters.txt and Departments.txt in the SemDepLists directory to run.
Creates a new directory CourseLists and a directory for every semester in CourseLists.
Generates a text file listing course information for every department in that semester directory.
Creates a Logs directory with an error log listing any departments that could not be scraped.
Takes a long time to scrape all the data (~6 hours).

To update a single semester:
	
	Compile: javac -d . UpdateSem.java
	Run: java UpdateSem [semester name]

Example: java UpdateSem Spring 2020
Does not require Semesters.txt or Departments.txt to run.
This call will only update course information for departments in Spring 2020.
Does not create an error log.
Much faster than CourseScraper (~5 minutes).