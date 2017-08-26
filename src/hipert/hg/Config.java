package hipert.hg;

import java.io.File;

public class Config {

	public static String DefaultHGTDir = "D:\\UniMoRe\\Workspace\\hipert.hg\\";
	// Relative to source path
	public static String DefaultRuntimeSrcDir  = "runtime" + File.separator + "src" + File.separator;
	public static String DefaultToolsDir = "tools" + File.separator;
	public static String DefaultOutputDir   = "gen" + File.separator;
	public static String DefaultGraphvizDir = DefaultToolsDir + "graphviz" + File.separator;

}
