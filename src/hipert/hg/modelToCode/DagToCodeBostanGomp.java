/**
 * 
 */
package hipert.hg.modelToCode;

import java.util.ArrayList;

import com.upscale.PsocMapper;

import hipert.hg.Globals;

/**
 * @author Pol
 *
 */
public class DagToCodeBostanGomp extends DagToCode {

	/**
	 * 
	 */
	public DagToCodeBostanGomp() {
		super();
	}
	
	
	public void GenerateCode(ArrayList<String> fileNames) {
		super.GenerateCode(fileNames);
		try {
			PsocMapper.GenerateTaskTable(fileNames);
		}
		catch (java.lang.UnsatisfiedLinkError ule) {
			throw ule;
		}
	}

}
