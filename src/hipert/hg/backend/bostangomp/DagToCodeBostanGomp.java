/**
 * 
 */
package hipert.hg.backend.bostangomp;

import java.util.ArrayList;

import com.upscale.PsocMapper;

import hipert.hg.Globals;
import hipert.hg.backend.ptask.DagToCode;

/**
 * @author Pol
 *
 */
public class DagToCodeBostanGomp extends DagToCode {

	@Override
	public boolean Post(ArrayList<String> fileNames) {
		
		try {
			PsocMapper.GenerateTaskTable(fileNames, Globals.OutputDir + "/" + Globals.PsocTableFileName);
			return true;
		}
		catch (java.lang.UnsatisfiedLinkError ule) {
			throw ule;
		}
	}
	
	@Override
	public String getFriendlyName() {
		return "Bostan Gomp";
	}

}
