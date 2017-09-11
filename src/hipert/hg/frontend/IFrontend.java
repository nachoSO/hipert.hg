package hipert.hg.frontend;

import java.util.List;

import hipert.hg.core.IGui;
import hipert.hg.core.RTDag;

/**
 * Every frontend must implement this
 * @author Pol
 *
 */
public interface IFrontend {

	 public void Parse(List<RTDag> dags, String fileDst, IGui gui);
	 
	 public String []getFileExtensions();
}
