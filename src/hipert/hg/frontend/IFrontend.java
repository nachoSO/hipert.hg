package hipert.hg.frontend;

import hipert.hg.frontend.rtdot.DAG;

/**
 * Every frontend must implement this
 * @author Pol
 *
 */
public interface IFrontend {

	 public void Parse(DAG dagsParam[], String fileDst);
}
