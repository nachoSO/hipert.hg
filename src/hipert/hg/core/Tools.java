package hipert.hg.core;

import java.io.File;
import java.io.IOException;
import hipert.hg.Globals;

public class Tools {

	public static String CreateDagImage(String fileName) throws Exception
	{
		try {
			String graphVizPath = Globals.GraphvizDir;
			String imageFileName = fileName.replace(".dot", ".png");
			String cmdLine = graphVizPath + "bin" + File.separator + "dot.exe "+ "-Tpng " + fileName + " -o " + imageFileName;
			// TODO under linux?
			
		    Runtime rt = Runtime.getRuntime();
		    rt.exec(cmdLine).waitFor();
			
			return imageFileName;
		} catch (IOException e) {
			throw new Exception("Unable to find GraphViz");
		}
	}

}
