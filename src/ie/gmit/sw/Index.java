package ie.gmit.sw;

/**
 * 
 * @author Arron Healy
 * @version 1.0
 * @since 1.8 
 * 
 * 
 * Object used to keep track of the frequency of a word & the file it came from.
 * Basic Object only used for get & set purposes.
 * 
 */

public class Index {
	
	private int wordFrequency;
	
	private String fileName;
	
//---------------------------------------------------------------
	
	/**
	 * Empty default Constructor to initialize an object.
	 */
	
	public Index(){
		
	}//index
	
	/**
	 * Paramaterized constuctor
	 * 
	 * @param frequency - variable initalizes first word of file.
	 * 
	 * @param name - variable to store name of File word came from.
	 */
	
	public Index(int frequency, String name){
		
		this.wordFrequency = frequency;
		this.fileName = name;
	}//index
	
//---------------------------------------------------------------
	
	//Getter & Setter Methods.
	
	/**
	 * 
	 * Returns frequency of a word in a given file.
	 * 
	 * @return int
	 */

	public int getWordFrequency() {
		
		return wordFrequency;
	
	}//getWordFrequency
	
	/**
	 * 
	 * @param wordFrequency - sets frequency of a word in a given file.
	 */

	public void setWordFrequency(int wordFrequency) {
		
		this.wordFrequency = wordFrequency;
	
	}//setWordFrequency
	
//---------------------------------------------------------------

	/**
	 * 
	 * Returns name of file a given word belongs too.
	 * 
	 * @return String
	 */

	public String getFileName() {
		
		return fileName;
	
	}//getFileName
	
	/**
	 * 
	 * @param fileName - sets name of file a given word belongs too.
	 */

	public void setFileName(String fileName) {
		
		this.fileName = fileName;
	
	}//setFileName
	
//---------------------------------------------------------------

}//Index