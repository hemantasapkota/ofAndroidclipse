/*
 * Copyright (c) 2013, 2014 Hemanta Sapkota.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Hemanta Sapkota (laex.pearl@gmail.com)
 */
package ofandroidclipse.wizards;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ofandroidclipse.ofReleaseDir.OFUtil;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

/**
 * The Class NewOFAndroidProjectPageDelegate.
 */
public class NewOFAndroidProjectPageDelegate {
	
	/**
	 * Instantiates a new new of android project page delegate.
	 */
	public NewOFAndroidProjectPageDelegate() {
	}
	
	/**
	 * Gets the examples.
	 *
	 * @param pathToOF the path to of
	 * @return the examples
	 * @throws CoreException the core exception
	 */
	public List<File> getExamples(String pathToOF) throws CoreException {
		final List<File> examples = new ArrayList<File>();
		IPath pathToExamples = new Path(pathToOF).append(OFUtil.OF_ANRDOID_EXAMPLES);
		
		File f = pathToExamples.toFile();
		
		for (File fo : f.listFiles()) {
			if (fo.isDirectory()) {
				examples.add(fo);
			}
		}
		return examples;
	}
 
}
