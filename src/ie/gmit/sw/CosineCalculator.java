package ie.gmit.sw;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;

/**
 * 
 * @author Arron Healy
 * @version 1.0
 * @since 1.8
 * 
 * 
 * CosineCalculator <i>implements</i> the Callable interface and Returns a Double.
 * Calculates the Magnitude of two documents and also their Dot product.
 * Returns Cosine Distance of two Documents.
 *
 */

public class CosineCalculator implements Callable<Double>{

	private ConcurrentMap<Integer, List<Index>> dirMap;
	
	private ConcurrentMap<Integer, List<Index>> queryMap;
	
	private String directoryName;
	
	private String fileName;
	
	private String filePath;
	
	private int directoryMag;
	
	private int queryMag;
	
	private int dotProduct;
	
	private double query;
	
	private double dir;
	
	private double cosineDistance;
	
//------------------------------------------------------------------------
	
	/**
	 * Paramaterized constuctor
	 * 
	 * Constructs Callable Object
	 * 
	 * @param dMap - Concurrent Map - contains directory files word references
	 *  
	 * @param qMap - Concurrent Map - contains query file word references
	 * 
	 * @param dir - String - directory name for file processing
	 * 
	 * @param file - String - name of file to be processed 
	 * 
	 * @param filePath - String - file path for file matching
	 */

	
	public CosineCalculator(ConcurrentMap<Integer, List<Index>> dMap, ConcurrentMap<Integer, List<Index>> qMap, String dir, String file) {
	
		this.dirMap = dMap;
		this.queryMap = qMap;
		this.directoryName = dir;
		this.fileName = file;
		this.filePath = this.directoryName + "/" + this.fileName;
		
	}//cosineCalculator
	
//------------------------------------------------------------------------

	/**
	 * 
	 * Returns an integer which is the Magnitude of the query document.
	 * 
	 * Magnitude depends on file input size.
	 * 
	 * Synchronized for Thread safety.
	 * 
	 * @return int
	 */
	
	private synchronized int getQueryMapMagnitude()
	{
		
		int mag = 0;
		
		//loop for query map entry set
		
		for(Map.Entry<Integer, List<Index>> m: this.queryMap.entrySet())
		{
			//assume query file has only one entry per list increment files word magnitude
			
			mag += m.getValue().get(0).getWordFrequency();
		
		}//for
		
		return mag;
	
	}//getQueryMagnitude
	
//------------------------------------------------------------------------
	
	/**
	 * Returns an integer which is the Magnitude of the directory document.
	 * 
	 * Magnitude depends on file input size.
	 * 
	 * Synchronized for Thread safety.
	 * 
	 * @return int
	 */
	
	private synchronized int getDirectoryMapMagnitude()
	{
		int mag = 0;
		
		//loop for directory maps entry set
		
		for(Map.Entry<Integer, List<Index>> m: this.dirMap.entrySet())
		{
			//loop for directory maps list values
			
			for(Index i: m.getValue())
			{
				//if indexed value matches input file increment files word magnitude
				
				if(i.getFileName().equals(this.filePath)){
					
					//increment & break out of loop after file match
					
					mag += i.getWordFrequency();
					break;
				}//if
				
			}//for
			
		}//for

		return mag;
		
	}//getDirectoryMagnitude
	
//------------------------------------------------------------------------

	/**
	 * Returns an integer which is the Magnitude of the query document.
	 * 
	 * Magnitude depends on file input size.
	 * 
	 * Synchronized for Thread safety.
	 * 
	 * @return int
	 */
	
	private synchronized int getDotProduct()
	{
		int dot = 0;
		
		//loop for directory map entry set
		
		for(Map.Entry<Integer, List<Index>> m: this.queryMap.entrySet())
		{
			//if both maps contain same keys enter block
			
			if(this.dirMap.containsKey(m.getKey()))
			{
				//loop for directory maps list values
				
				for(Index i: this.dirMap.get(m.getKey()))
				{
					//if indexed file matches multiply both values word frequencies
					
					if(i.getFileName().equals(this.filePath)){
						
						//increase dot product by maps values & break from loop
						
						dot += i.getWordFrequency() * this.queryMap.get(m.getKey()).get(0).getWordFrequency();
						break;
					}//if
					
				}//for
				
			}//if
			
		}//for
		
		return dot;
	
	}//getDotProduct
	
//------------------------------------------------------------------------

	/**
	 * 
	 * Override call method from Callable interface.
	 * 
	 * Method throws Exception.
	 * 
	 * Returns Cosine Distance of Documents.
	 * 
	 * @return Double
	 */
	
	@Override
	public Double call() throws Exception {
	
		//get query file magnitude
		this.queryMag = getQueryMapMagnitude(); 
		
		//get directory file magnitude
		this.directoryMag = getDirectoryMapMagnitude();
		
		//get dot product of files
		this.dotProduct = getDotProduct();
		
		//get square root of query file magnitude
		this.query = Math.sqrt((double)this.queryMag);
		
		//get square root of directory file magnitude
		this.dir = Math.sqrt((double)this.directoryMag);
		
		//compute Cosine Distance formula
		this.cosineDistance = this.dotProduct / (this.query * this.dir);
		
		return cosineDistance;
	
	}//call
	
//------------------------------------------------------------------------

}//CosineCalculator
