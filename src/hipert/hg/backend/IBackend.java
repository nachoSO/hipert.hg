/**
 * 
 */
package hipert.hg.backend;

import java.util.ArrayList;

/**
 * Every backend should implement this
 * @author Pol
 *
 */
public interface IBackend {
	public String getFriendlyName();
	
	public void GenerateCode(String modelFileName) throws Exception;

	public boolean Post(ArrayList<String> fileNames);
}
