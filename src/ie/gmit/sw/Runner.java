package ie.gmit.sw;

/**
 * 
 * @author Arron Healy
 * @version 1.0
 * @since 1.8
 * 
 * Runner class contains main method.
 * 
 * Creates instance of Cosine UI class to process files.
 *
 */

public class Runner {

	/**
	 * contains main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		//create instance of the ui and begin
		
		new CosineUI().process();
	
	}//main

}//Runner
