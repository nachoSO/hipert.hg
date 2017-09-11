package hipert.hg.frontend.amalthea;

import java.io.Serializable;

public class Label implements Serializable{
	
	String name;	
	int size;
	int frequencyAccess;
	String labelLocation;
	String typeOfAccess=""; //"R"/"W"
	boolean isShared=false;
	
	public Label(String name, int size, int frequencyAccess, String labelLocation, String typeOfAccess, boolean isShared){
		this.name=name;
		this.size=size;
		this.frequencyAccess=frequencyAccess;
		this.labelLocation=labelLocation;
		this.typeOfAccess=typeOfAccess;
		this.isShared=isShared;
	}
}
