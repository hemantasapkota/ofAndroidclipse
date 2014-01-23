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
package ofandroidclipse;

import java.io.File;
import java.io.IOException;
import java.util.List;

import ofandroidclipse.ofReleaseDir.OFUtil;
import ofandroidclipse.ofReleaseDir.OFUtil.OFAndroidProjectParams;
import ofandroidclipse.ofReleaseDir.OFAndroidProjectGenerator;
import ofandroidclipse.wizards.NewOFAndroidProjectPageDelegate;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * The Class OFAndroidclipseTest.
 */
public class OFAndroidclipseTest {

	/** The importer. */
	private OFAndroidProjectGenerator importer;
	
	/** The delegate. */
	private NewOFAndroidProjectPageDelegate delegate;
	
	/** The p. */
	private OFAndroidProjectParams p;
	
	/** The file. */
	private File file;

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
		delegate = new NewOFAndroidProjectPageDelegate();

		p = new OFAndroidProjectParams();
		p.pathToOF = OFUtil.pathToOF();
		p.projectName = OFUtil.OF_ANRDOID_EMPTY_EXAMPLE;
		p.shouldImportOFAndroidLib = true;

		for (File f : delegate.getExamples(p.pathToOF)) {
			p.examplesToImport.add(f.toString());
		}

		importer = new OFAndroidProjectGenerator(p);
	}

	/**
	 * Tear down.
	 *
	 * @throws Exception the exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test list android examples.
	 *
	 * @throws CoreException the core exception
	 */
	@Test
	public void testListAndroidExamples() throws CoreException {
		List<File> examples = delegate.getExamples(OFUtil.pathToOF());
		assert (examples.size() != 0);
	}

	/**
	 * Test copy generator ant file.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws CoreException the core exception
	 */
	@Test
	public void testCopyGeneratorAntFile() throws IOException, CoreException {
		file = importer.copyGeneratorANTFile();
		Assert.assertNotNull(file);
		Assert.assertTrue(file.exists());
		file.delete();
	}

	/**
	 * Test import of android lib.
	 *
	 * @throws CoreException the core exception
	 */
	@Test
	public void testImportOFAndroidLib() throws CoreException {
		IProject ofAndroidLibPrj = ResourcesPlugin.getWorkspace().getRoot()
				.getProject("ofAndroidLib");
		Assert.assertFalse(ofAndroidLibPrj.exists());

		importer.importOFAndroidLibProject(new NullProgressMonitor());

		Assert.assertTrue(ofAndroidLibPrj.exists());

		ofAndroidLibPrj.delete(IResource.NEVER_DELETE_PROJECT_CONTENT,
				new NullProgressMonitor());
	}

//	@Test
//	public void testImportOFExamples() throws CoreException {
//
//		for (IProject prj : ResourcesPlugin.getWorkspace().getRoot()
//				.getProjects()) {
//			prj.delete(IResource.NEVER_DELETE_PROJECT_CONTENT,
//					new NullProgressMonitor());
//		}
//
//		importer.importExamples(new NullProgressMonitor());
//
//		int importCount = ResourcesPlugin.getWorkspace().getRoot()
//				.getProjects().length;
//
//		Assert.assertTrue(importCount == delegate.getExamples(p.pathToOF)
//				.size());
//	}

}
