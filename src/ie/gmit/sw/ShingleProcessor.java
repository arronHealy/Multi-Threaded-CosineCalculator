package ie.gmit.sw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;

/**
 * 
 * @author Arron Healy
 * @version 1.0
 * @since 1.8
 * 
 * 
 * ShingleProcessor <i>implements</i> Runnable interface.
 * 
 * Consumes words/shingles from blocking queue given by producer thread.
 *
 */

public class ShingleProcessor implements Runnable{
	
	//class variables
	
	private BlockingQueue<Word> queue;
	
	private ConcurrentMap<Integer, List<Index>> map;
	
	private Word word;
	
	private int shingle;
	
	private List<Index> frequencyList;
	
	private boolean keepRunning = true;
	
//--------------------------------------------
	
	/**
	 * Empty default constructor to initialze object
	 */
	
	public ShingleProcessor() {
		
		
	}//constructor
	
	/**
	 * 
	 * @param q - Blocking queue passes word/shingle types to be processed 
	 * 
	 * @param m - Concurrent Map to store word types and frequency of each word.
	 * 
	 */
	
	public ShingleProcessor(BlockingQueue<Word> q, ConcurrentMap<Integer, List<Index>> m){
		
		this.queue = q;
		this.map = m;
	
	}//constructor
	
//--------------------------------------------------------------
	
	/**
	 * Override run method from Runnable interface.
	 * 
	 * 
	 */
	
	@Override
	public void run() {
		
		//loop while true
		
		while(keepRunning){
			
			try {
				
				//take word from queue
				
				word = queue.take();
				
				//create shingle from word type
				
				shingle = word.getShingle();
				
				//check if poison encountered
				
				if(word instanceof Poison){
					
					//terminate loop if poisoned
					
					keepRunning = false;
				}
				else
				{
					
					//synchronize map object as operations check for key and put in map may not be thread safe
					
					synchronized (map) {
						
						//check if map hasn't key shingle yet
						
						if(!map.containsKey(shingle))
						{
							//set list to a synchronized list
							frequencyList = Collections.synchronizedList(new ArrayList<>());
							
							//add new index to list
							frequencyList.add(new Index(1, word.getFileReferenceName()));
							
							//put new key and new list value in map
							map.putIfAbsent(shingle, frequencyList);
							
						}
						else
						{
							//if has map has shingle as key get the list
							frequencyList = map.get(shingle);
							
							//loop for that keys list value
							
							for(Index i: frequencyList)
							{
								//if words file already indexed increase that files word qty
								
								if(i.getFileName().equals(word.getFileReferenceName())){
									
									//set new word frequency & break
									i.setWordFrequency(i.getWordFrequency() + 1);
									break;
								
								}//if
								
								//if words file doesn't match & at last index of list add new word and file to map
								
								if(!i.getFileName().equals(word.getFileReferenceName()))
								{
									
									if(frequencyList.indexOf(i) == frequencyList.size() - 1){
										
										//add new file to word list and word qty
										frequencyList.add(new Index(1, word.getFileReferenceName()));
										break;
									
									}//if
								
								}//if
								
							}//for
							
						}//if
						
					}//sync
					
				}//if
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//catch
			
		
		}//while
		
	}//run
	
//-----------------------------------------------------------------

}//ShingleProcesor
