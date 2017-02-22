package hipert.hg.modelToCode;

import java.io.File;
import java.util.ArrayList;

public class PsocMapper {
	static {
		try {

			String currentDir = new java.io.File( "." ).getCanonicalPath();
	        //System.out.println("Current dir: " + currentDir);
	        //String userDir = System.getProperty("user.dir");
	        //System.out.println("Current dir using System: " + userDir);
			//String libpath = System.getProperty("java.library.path");
	        //System.out.println("java.library.path: " + libpath);
			
			String libDir = currentDir + File.separator + "lib";
			String dllName = "psoc_mapper.dll";
	        System.load(libDir + File.separator + dllName);
		}
		catch (java.lang.UnsatisfiedLinkError ule) {
			ule.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static native int GenerateTaskTable(String dotFileNames[], String runtimeDir, String tableFileName);

	public static int GenerateTaskTable(String dotFileName) {
		ArrayList<String> dotFileNames = new ArrayList<String>();
    	dotFileNames.add(dotFileName);
    	return GenerateTaskTable(dotFileNames);
    }
	
	public static int GenerateTaskTable(ArrayList<String> dotFileNames) {
    	String[] stringArr = new String[dotFileNames.size()];
    	stringArr = dotFileNames.toArray(stringArr);
    	
    	// TODO create a Globals class
    	String runtimeDir = "";
    	String tableFileName = "filename.c";
    	
    	try {
    		return PsocMapper.GenerateTaskTable(stringArr, runtimeDir, tableFileName);
    	}
		catch (java.lang.UnsatisfiedLinkError ule) {
			throw ule;
		}
		catch (java.lang.Exception ex) {
			throw ex;
		}
	}
}
