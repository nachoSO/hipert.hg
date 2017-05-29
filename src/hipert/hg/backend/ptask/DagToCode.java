/*******************************************************************************
 * Copyright (c) 2008 The University of York.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Dimitrios Kolovos - initial API and implementation
 ******************************************************************************/
package hipert.hg.backend.ptask;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.egl.EglFileGeneratingTemplateFactory;
import org.eclipse.epsilon.egl.EglTemplateFactoryModuleAdapter;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.eol.IEolModule;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.models.IRelativePathResolver;

import hipert.hg.backend.IBackend;

public class DagToCode implements IBackend {
	
	protected IEolModule module;
	
	protected Object result;
	
	
	public DagToCode() {
	}

	
	protected EmfModel createEmfModel(String name, String model, 
			String metamodel, boolean readOnLoad, boolean storeOnDisposal) 
					throws EolModelLoadingException, URISyntaxException {
		EmfModel emfModel = new EmfModel();
		StringProperties properties = new StringProperties();
		properties.put(EmfModel.PROPERTY_NAME, name);
		properties.put(EmfModel.PROPERTY_FILE_BASED_METAMODEL_URI,metamodel);
		properties.put(EmfModel.PROPERTY_MODEL_URI,model);
		properties.put(EmfModel.PROPERTY_READONLOAD, readOnLoad + "");
		properties.put(EmfModel.PROPERTY_STOREONDISPOSAL, 
				storeOnDisposal + "");
		emfModel.load(properties, (IRelativePathResolver) null);
		return emfModel;
	}
	
	/* Inherited by IBackend */
	
	@Override
	public void GenerateCode(String modelFileName) {
		try {
			EglTemplateFactoryModuleAdapter module = new EglTemplateFactoryModuleAdapter(new EglFileGeneratingTemplateFactory()); 
			module.parse(new File("modelToCode/driver.egl"));

			
			module.getContext().getModelRepository().addModel(
					createEmfModel("Model", modelFileName, "./metamodel/graphMetamodel.ecore", true, true));
			result = module.execute();
			module.getContext().getModelRepository().dispose();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void Post(ArrayList<String> fileNames) {
		// Nothing...
		
	}

}