package hipert.hg.core;

public class Utils {

	public static int CalcStep(String stepString) {
		// Step access type
		int step = 0;
		if (stepString.equals("char")) {
			step = 1; //bits
		}
		else if(stepString.equals("int")) {
			step = 4;
		}
		else if(stepString.equals("double")) {
			step = 8;
		}
		else if(stepString.equals("long double")) {
			step = 16;
		}
		return step;
	}

}
