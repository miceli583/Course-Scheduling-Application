/******************************************************************************
Contributors:
Matthew Miceli
Christopher Chee
Shaun McFadden
Kareem Abdo 
*******************************************************************************/

//Test Cases
public class SchedulerTestCases{
	public static void main(String[] args){
		// Test 1: Given a porper input of semester, year, and list of course, the scheduler should be able to generate a list of possible schedules
		// with no time conflicts.
		System.out.println("Test1 --------------------------------------------------------------------------------------");
		String input1[] = new String[] {"Fall","2019","Computer Science","3200","Computer Science","3380","Computer Science","4444","Economics","2030","Mechanical Engineering","4183","Mechanical Engineering","4201"};
		Main.main(input1);	
		System.out.println("--------------------------------------------------------------------------------------------\n");
		


		//Test 2: Given improper input or misspeled classes, the scheduler should report an error
		System.out.println("Test2 --------------------------------------------------------------------------------------");
		String input2[] = new String[] {"Fal","209","Coputer Science","3200"};
		Main.main(input2);
		System.out.println("--------------------------------------------------------------------------------------------\n");

		//Test 3: Given an input oof a course that does not exist, the scheduler should report an error
		System.out.println("Test3 --------------------------------------------------------------------------------------");
		String input3[] = new String[] {"Fall","2019","Computer Science","3200","Economics","3200"};
		Main.main(input3);
		System.out.println("--------------------------------------------------------------------------------------------\n");
	}
}