package hipert.hg.core;

import java.io.File;

public class RTDag {

	private String fileName = null;
	private String fileFolder = null;
	
	public String getFileName() {
		return fileName;
	}

	public String getFileFolder() {
		return fileFolder;
	}
	
	public String getType()
	{
		int i = fileName.lastIndexOf('.');
		if (i > 0)
		    return fileName.substring(i+1);
		return "???";		
	}
	
	public String getFullFileName()
	{
		if(fileName == null)
			return "N/A";
		return ( fileFolder != null ? fileFolder : "." )+ File.separator + fileName;
	}
	
	public String getName()
	{
		return "TODO";
	}
	
	public RTDag(String fileName, String folder) {
		this.fileName = fileName;
		this.fileFolder = folder;
	}

}
