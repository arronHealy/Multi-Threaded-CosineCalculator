package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;

/**
 * 
 * @author Arron Healy
 * @version 1.0
 * @since 1.8
 * 
 * 
 * FileParser <i>implements</i> Runnable interface.
 * 
 * Processes files into words/shingles for processing by consumer thread.
 *
 */

public class FileParser implements Runnable {

	//class variables
	
	private BlockingQueue<Word> queue;
	
	private String fileName;
	
	private BufferedReader br;
	
	private String line;
	
	private String[] words;
	
//---------------------------------------------
	
	/**
	 * Empty default constructor to initialize object
	 */
	
	public FileParser(){
		
	}//fileParser
	
	/**
	 * 
	 * @param file - passes in file name to be processed into words/shingles.
	 * 
	 * @param q - Blocking queue passed in for thread to produce words for consumer thread.
	 */
	
	public FileParser(String file, BlockingQueue<Word> q){
		
		this.fileName = file;
		this.queue = q;
		
	}//fileParser
	
	

//---------------------------------------------------------------------
	
	/**
	 * Override run method from Runnable interface.
	 * 
	 * Processes file contents into words/shingles for consumer thread.
	 * 
	 * Adds words/shingles to Blocking queue.
	 */
	
	
	@Override
	public void run() {
		
		try {
			
				//read file from buffered reader
			
				br = new BufferedReader(new InputStreamReader(new FileInputStream(this.fileName)));
				
				//handle null exception incase no file found
				
				if(br == null){
					
					System.out.println("Problem occured can't Find File - make sure enter text format Directory/File.txt");
					return;
				
				}//if
				
				
				line = "";
					
				//read file contents until null encountered
				
				while((line = br.readLine()) != null){
				
					//convert file text to lower case remove special characters.
					
					line = line.trim().toLowerCase().replaceAll("[^a-z\\s]", "");
					
					//convert file text to string array
					
					words = line.split(" ");
					
					//loop for array 
					
					for(String s: words)
					{
						//add new word to queue
						
						queue.put(new Word(this.fileName, s));
					
					}//for
					
				}//while
				
				//poison queue
				queue.put(new Poison("", ""));
				
				//close reader
				br.close();

		} 
		catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}//catch
		
	}//run
	
//----------------------------------------------------
	
}//FileParser