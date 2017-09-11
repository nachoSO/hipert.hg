package hipert.hg.frontend.amalthea;

import java.util.List;

import hipert.hg.core.IGui;
import hipert.hg.core.RTDag;
import hipert.hg.frontend.IFrontend;

public class AmaltheaParser implements IFrontend{

	@Override
	public void Parse(List<RTDag> dags, String fileDst, IGui gui) {
		System.out.println("Generating code!");
	}

	@Override
	public String[] getFileExtensions() {
		return new String[] {"*.amxmi" };
	}

}
