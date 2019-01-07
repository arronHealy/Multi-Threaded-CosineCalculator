package ie.gmit.sw;

import java.io.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Arron Healy
 * @version 1.0
 * @since 1.8
 * 
 * 
 * Cosine Calculator user interface.
 * 
 * Methods thread files & Directories - produces cosine distance result.
 *
 * Loop controlled menu prompts for directory of files to process then query file to compare.
 * 
 * Producer/Consumer design pattern for threads
 * 
 * Concurrent Maps process words/shingles
 * 
 * Callable threads return results from processed maps.
 */

public class CosineUI {
	
	//class variables
	
	private String queryFileName;
	
	private String[] fileList;
	
	private String directoryName;
	
	private String continueString;
	
	private File directoryFiles;
	
	private File queryFile;
	
	private Scanner console;
	
	private BlockingQueue<Word> queue;
	
	private ConcurrentMap<Integer, List<Index>> lookupMap;
	
	private ConcurrentMap<Integer, List<Index>> queryFileMap;
	
	private ExecutorService threadPool;
	
	private ArrayList<Future<Double>> cosineDistances;
	
//-----------------------------------------------------------------
	
	/**
	 * Default constructor for ui initializes scanner
	 */
	
	public CosineUI(){
		
		//initialize scanner
		
		console = new Scanner(System.in);
		
	}//constructor
	
//------------------------------------------------------------------
	
	/**
	 * Process method handles all menu prompts & threading calls 
	 * 
	 * loop while user enters y to continue 
	 * 
	 * Acts as applications menu
	 */
	
	public void process(){
		
		//do while true
		
		do{
			
			System.out.print("\n*** Document Comparison Service ***");
			
			//call method list in order
			processDirectory();
			
			threadSubjectDirectory();
			
			processFile();
			
			threadQueryFile();
			
			processCosineDistances();
			
			//prompt user to continue
			System.out.print("\nProcessing finished --- Would you like to continue ? \nEnter Y to continue or Enter Any Key to Exit: ");
			continueString = console.next();
			
		}while(continueString.equalsIgnoreCase("y"));
		
		//exit loop
		System.out.println("System Exit!!!");
		
		//close scanner
		console.close();
		
		//exit app
		System.exit(0);
	
	}//process
	
//-------------------------------------------------------------------------------
	
	/**
	 * thread Query File entered by user
	 * 
	 * Processes words through blocking queue and builds map for query file from thread pool.
	 * 
	 */
	
	private void threadQueryFile(){
		
		//initialize concurrent variables
		queryFileMap = new ConcurrentHashMap<>();
		
		queue = new ArrayBlockingQueue<>(200000);
		
		threadPool = Executors.newFixedThreadPool(2);
		
		//submit runnable objects to be processed
		
		threadPool.submit(new FileParser(queryFileName, queue));
		threadPool.submit(new ShingleProcessor(queue, queryFileMap));
		
		//request thread shutdown procedure
		
		threadPool.shutdown();
		
		//try wait for all threads to terminate
		try {
			threadPool.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
		
			e.printStackTrace();
		}//catch
		
		
	}//threadQueryFile
	
//--------------------------------------------------------------------------
	
	/**
	 * thread subject directory entered by user
	 * 
	 * Processes words through blocking queue and builds map for directory from thread pool.
	 * 
	 */
	
	private void threadSubjectDirectory(){
		
		//initialize concurrent variables
		lookupMap = new ConcurrentHashMap<>();
		
		threadPool = Executors.newFixedThreadPool(6);
		
		queue = new ArrayBlockingQueue<>(20000000);
		
		//create file list from directory
		fileList = directoryFiles.list();
		
		//loop for list of files submit runnable objects to process files in directory
		
		for(String s: fileList){
			
			threadPool.submit(new FileParser(directoryName + "/" + s, queue));
			threadPool.execute(new ShingleProcessor(queue, lookupMap));
		
		}//for
		
		//request shutdown procedure
		
		threadPool.shutdown();
		
		//try wait for threads to terminate
		
		try {
			threadPool.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}//catch
		
		
	}//threadSubjectDirectory
	
//--------------------------------------------------------------------------
	
	/**
	 * process cosine distances - takes maps to be processed and prints results.
	 * 
	 * Processes maps through callable thread submitted by thread pool.
	 * 
	 * processes cosine distances per file in directory and query file entered.
	 * 
	 * prints cosine distances on completion.
	 */
	
	private void processCosineDistances(){
		
		//initialize thread pool & future collection
		
		threadPool = Executors.newFixedThreadPool(2);
		
		cosineDistances = new ArrayList<Future<Double>>();
		
		//control variable for loop
		int index = 0;
		
		//loop for files in directory
		
		for(String s: fileList){
			
			//submit maps to callable objects for results
			
			Future<Double> results = threadPool.submit(new CosineCalculator(lookupMap, queryFileMap, directoryName, s));
			
			//add future objects
			
			cosineDistances.add(results);
			
		}//for
		
		System.out.println("\nProcessing Cosine Distances! Please wait...");
		
		//request shutdown procedure
		
		threadPool.shutdown();
		
		//try wait for threads to terminate
		
		try {
			threadPool.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}//catch
		
		
		//loop for future objects
		
		for(Future<Double> results: cosineDistances)
		{
			try {
				//get results
				Double val = results.get();
				
				//print cosine distances per file
				
				System.out.println("\nCosine Distance - Query File name: " + queryFileName + " & Directory File name: " + fileList[index]);
				System.out.println("--------------------------------");
				System.out.printf("Cosine Distance is: %.3f %%\n", val);
				System.out.println("--------------------------------\n");
				
				//control var for file list
				index++;
				
			} catch (InterruptedException | ExecutionException e) {
			
				e.printStackTrace();
			}//catch
			
		}//for
		
	}//processCosineDistances
	
//--------------------------------------------------------------------------
	
	/**
	 *  process Directory - prompts user for subject Directory.
	 * 
	 *  checks that directory exists and that it is a valid directory.
	 *  
	 *  keeps prompting if directory can't be found
	 *  
	 *  sets name of directory to be processed on completion
	 */
	
	private void processDirectory(){
		
		//prompt user for directory name
		
		System.out.print("\nEnter Subject Directory Path > ");
		
		directoryName = console.next();
		
		directoryFiles = new File(directoryName);
		
		//check if directory exists prompt until directory found
		
		if(!directoryFiles.exists() && !directoryFiles.isDirectory()){
			
			do{
				System.out.print("\nNo such Directory Try Again \nEnter Subject Directory Path > ");
				
				directoryName = console.next();
				
				directoryFiles = new File(directoryName);
				
			}while(!directoryFiles.exists() && !directoryFiles.isDirectory());
		}//if
		
		System.out.println("Directory exists ok to continue --- Directory name: " + directoryName);
		
	}//processDirectory
	
//-------------------------------------------------------------------------------------------------------------------
	
	/**
	 *  process File - prompts user for query file.
	 * 
	 *  checks that file exists and that it is a valid file.
	 *  
	 *  keeps prompting if file can't be found
	 *  
	 *  sets name of file to be processed on completion
	 */
	
	private void processFile(){
		
		//prompt user for file
		
		System.out.print("\nEnter Query File Path > ");
		
		queryFileName = console.next();
		
		queryFile = new File(queryFileName);
		
		//check if file exists prompt user until file found
		
		if(!queryFile.exists() && !queryFile.isFile()){
			
			do{
				
				System.out.print("\nInvalid File or Format Try Again Format should be - Directory/File.txt \nEnter Query File Path > ");
				queryFileName = console.next();
				queryFile = new File(queryFileName);
				
			}while(!queryFile.exists() && !queryFile.isFile());
			
		}//if
		
		System.out.println("File exists ok to continue --- File name: " + queryFileName);
		
	}//processFile
	
//------------------------------------------------------------------------------------------------------------------

}//cosineUI

