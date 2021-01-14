import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.lang.StringBuilder;
import java.util.Scanner;
import java.util.Date;
import java.text.SimpleDateFormat;


public class Todo {
	public static void main(String args[]){
		if(args.length>0){
			File toDoFile = new File("toDo.txt");
			File doneFile = new File("done.txt");
			String arg = args[0].toLowerCase();
			
			if(arg.equals("add")){
				if(args.length>=2){
					writeFile(toDoFile, args[1]);
					System.out.println("Added todo: \"" + args[1] + "\"");
				}
				else{
					System.out.println("Error: Missing todo string. Nothing added!");
				}
			}
			else if(arg.equals("ls")){
				ls(toDoFile);
			}
			else if(arg.equals("del")){
				if(args.length>=2){
					deleteLine(toDoFile, Integer.parseInt(args[1]));
					System.out.println("Deleted todo #" + args[1]);
				}
				else{
					System.out.println("Error: Missing NUMBER for deleting todo.");
				}
			}
			else if(arg.equals("done")){
				if(args.length>=2){
					done(doneFile, toDoFile, Integer.parseInt(args[1]));
				}
				else{
					System.out.println("Error: Missing NUMBER for marking todo as done.");
				}
			}
			else if(arg.equals("help")){
				usage();
			}
			else if(arg.equals("report")){
				Date date = new Date();
				String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
				System.out.println(formattedDate + " Pending : " + lineCount(toDoFile) + " Completed : " + lineCount(doneFile));
			}
			else{
				usage();
			}
		}
		else{
			usage();
		}
	}

	static void usage(){
		System.out.println("Usage :-\n" 
							+ "$ ./todo add \"todo item\"  # Add a new todo\n"
							+ "$ ./todo ls               # Show remaining todos\n"
							+ "$ ./todo del NUMBER       # Delete a todo\n"
							+ "$ ./todo done NUMBER      # Complete a todo\n"
							+ "$ ./todo help             # Show usage\n"
							+ "$ ./todo report           # Statistics\n"
							);
							
	}

	static void writeFile(File file, String str){
		try{
			if(!file.exists()){
				file.createNewFile();
			}

			FileWriter fileWriter = new FileWriter(file, true);
			fileWriter.write(str + "\n");
			fileWriter.close();
		}catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	static void ls(File file){
		try{
			if(!file.exists()){
				file.createNewFile();
			}
		
		StringBuilder str = new StringBuilder();
		Scanner scanner = new Scanner(file);
		int i = 1;

		//Adds each line to the beginning of the string in order to print the latest task first.
		while(scanner.hasNextLine()){
			str.insert(0, "[" + i++ + "] " + scanner.nextLine() + "\n");
		}
		scanner.close();

		if(i==1){
			System.out.println("There are no pending todos!");
			return;
		}
		System.out.println(str.toString());

		}catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}	
	
	static int lineCount(File file){
		int count = 0;
		try{
			Scanner scanner = new Scanner(file);
			while(scanner.hasNextLine()){
				scanner.nextLine();
				count++;
			}
			scanner.close();
		} catch (FileNotFoundException e){
			System.out.println("File not found");
			e.printStackTrace();
		}
		return count;
	}

	static void deleteLine(File file, int lineToBeRemoved){
		StringBuilder str = new StringBuilder();
		int currentLine = 1;
		try{
			Scanner scanner = new Scanner(file);
			while(scanner.hasNextLine()){
				if(currentLine != lineToBeRemoved){
					str.append(scanner.nextLine() + "\n");
				} else{
					scanner.nextLine();
				}
				currentLine++;
			}
			scanner.close();

			//currentLine contains the number of line in the file + 1
			if(lineToBeRemoved > currentLine-1 || lineToBeRemoved < 1){
				System.out.println("Error: todo #" + lineToBeRemoved +" does not exist. Nothing deleted.");
				return;
			}

			FileWriter fileWriter = new FileWriter(file, false);
			fileWriter.write(str.toString());
			fileWriter.close();

			
		} catch (Exception e){
			e.printStackTrace();
		} 
	}

	static void done(File doneFile, File toDoFile, int completedLine){
		try{
			if(!doneFile.exists()){
				doneFile.createNewFile();
			}
			int currentLine = 1;
			Scanner scanner = new Scanner(toDoFile);
			String completedTask = "";
			while(scanner.hasNextLine()){
				if(currentLine == completedLine){
					completedTask = scanner.nextLine();
					currentLine++;
					continue;
				}
				scanner.nextLine();
				currentLine++;
			}
			scanner.close();

			//completedLine contains the total number of lines in the file + 1
			if(completedLine > currentLine-1 || completedLine < 1){
				System.out.println("Error: todo #" + completedLine +" does not exist.");
				return;
			}

			FileWriter fileWriter = new FileWriter(doneFile, true);
			fileWriter.write(completedTask + "\n");
			fileWriter.close();

			deleteLine(toDoFile, completedLine);

			System.out.println("Marked todo #" + completedLine + " as done.");

		}catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
}
