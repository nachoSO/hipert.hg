/**
 * 
 */
package hipert.hg;

/**
 * @author Paolo Burgio
 *
 */
public class Globals {
	
	/* General */
	public static String ProgramVersion = "0.2";
	
	/* Useful */
	public static String Separator = "!";
	
	public static String HGTDir = Config.DefaultHGTDir;
	public static String OutputDir = Config.DefaultHGTDir + Config.DefaultOutputDir;
	public static String RuntimeDir  = Config.DefaultHGTDir + Config.DefaultRuntimeSrcDir;
	public static String GraphvizDir = Config.DefaultHGTDir + Config.DefaultGraphvizDir;
	public static String FrontendsDir = Config.DefaultHGTDir + Config.DefaultFrontendsDir;

	public static String GetTempDir()
	{
		return "modelToCode";
	}
	
	public static String ModelTempFileName = "dagParsed.model";
	/* Filename of the TDG table */
	public static String PsocTableFileName  = "psocrates_tdg.c";


}
