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
package ofandroidclipse.ofReleaseDir;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import ofandroidclipse.ofReleaseDir.OFUtil.OFAndroidProjectParams;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;

import com.google.common.io.ByteStreams;

/**
 * The Class OFAndroidProjectGenerator.
 */
public class OFAndroidProjectGenerator {

	/** The dest dir. */
	private IPath destDir;
	
	/** The p. */
	private OFAndroidProjectParams p;

	/**
	 * Instantiates a new OF android project generator.
	 *
	 * @param params the params
	 */
	public OFAndroidProjectGenerator(OFAndroidProjectParams params) {
		this.p = params;

		this.destDir = new Path(p.pathToOF).append(OFUtil.OF_APPS_DIR)
				.append(p.projectName);
	}

	/**
	 * Copy generator ant file.
	 *
	 * @return the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public File copyGeneratorANTFile() throws IOException {
		URL url = new URL(
				"platform:/plugin/OFAndroidClipse/ofAndroidclipseProjectGenerator.xml");
		InputStream is = url.openConnection().getInputStream();

		File tmpFile = File.createTempFile("ofAndroidclipse", ".xml");
		ByteStreams.copy(is, new FileOutputStream(tmpFile));

		return tmpFile;
	}

	/**
	 * Execute generate.
	 *
	 * @throws BuildException the build exception
	 * @throws URISyntaxException the URI syntax exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws CoreException the core exception
	 */
	public void executeGenerate() throws BuildException, URISyntaxException,
			IOException, CoreException {
		File generatorAntFile = copyGeneratorANTFile();

		Project antPrj = new Project();

		antPrj.setUserProperty("ant.file", generatorAntFile.getAbsolutePath());
		antPrj.init();

		ProjectHelper helper = ProjectHelper.getProjectHelper();
		antPrj.addReference("ant.projectHelper", helper);

		antPrj.setProperty("DEST_DIR", this.destDir.toString());
		antPrj.setProperty("PATH_TO_OF", p.pathToOF);
		antPrj.setProperty("NEW_PROJECT_NAME", p.projectName);
		antPrj.setProperty("TEMPLATE_PRJ", p.templateProject);

		helper.parse(antPrj, generatorAntFile);

		antPrj.executeTarget("GenerateOFProject");
		antPrj.executeTarget("CustomizeOFProject");

		generatorAntFile.delete();
	}

	/**
	 * Import generated project.
	 *
	 * @param monitor the monitor
	 * @throws CoreException the core exception
	 */
	public void importGeneratedProject(IProgressMonitor monitor)
			throws CoreException {
		OFUtil.importProjectFromPath(monitor, destDir);
	}

	/**
	 * Import of android lib project.
	 *
	 * @param monitor the monitor
	 * @throws CoreException the core exception
	 */
	public void importOFAndroidLibProject(IProgressMonitor monitor)
			throws CoreException {

		IPath pathToOFAndroidLib = new Path(p.pathToOF)
				.append(OFUtil.OF_ANDROIDLIB);

		OFUtil.importProjectFromPath(monitor, pathToOFAndroidLib);

	}
	
	/**
	 * Import examples.
	 *
	 * @param monitor the monitor
	 * @throws CoreException the core exception
	 */
	public void importExamples(IProgressMonitor monitor) throws CoreException {
		for (String example : p.examplesToImport) {
			IPath path = new Path(example);
			OFUtil.importProjectFromPath(monitor, path);
			monitor.worked(1);
		}
	}


}
