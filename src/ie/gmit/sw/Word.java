package ie.gmit.sw;

/**
 * 
 * @author Arron Healy
 * @version 1.0
 * @since 1.8
 * 
 * 
 * Word object used to keep track of words in a file & kepp track of file word came from.
 * Basic object used for getter & setter purposes.
 *
 */

public class Word {

	//class variables
	
	private String fileReferenceName;
	
	private String shingle;
	
//--------------------------------------------------------------
	//constructors
	
	/**
	 * Empty default constructor to initialize object
	 */
	
	public Word(){
		
	}//word
	
	/**
	 * Paramaterized constuctor
	 * 
	 * @param file - sets name of file word belongs too.
	 * 
	 * @param s - set word/shingle to be processed
	 */
	
	public Word(String file, String s){
		
		this.fileReferenceName = file;
		this.shingle = s;
		
	}//word

//--------------------------------------------------------------
	
	//Getters & Setters
	
	/**
	 * Returns name of file a given word belongs too.
	 * 
	 * @return String
	 */
	
	public String getFileReferenceName() {
	
		return fileReferenceName;
	
	}//getFileReferenceName

	/**
	 * 
	 * @param fileReferenceName - sets name of file for a given word.
	 */
	
	public void setFileReferenceName(String fileReferenceName) {
		
		this.fileReferenceName = fileReferenceName;
		
	}//setFileReferenceName
	
//--------------------------------------------------------------------

	/**
	 * Returns a int which is Strings hash code value which is the word/shingle from a given file.
	 * 
	 * @return int
	 */
	
	public int getShingle() {
		
		return shingle.hashCode();
		
	}//getShingle

	/**
	 * 
	 * @param shingle - sets the word/shingle for a given file to be processed.
	 */
	
	public void setShingle(String shingle) {
	
		this.shingle = shingle;
	
	}//setShingle
	
//-----------------------------------------------------------------	
	
}//Word

