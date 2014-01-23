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
import java.net.URL;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;

import com.google.common.io.ByteStreams;

/**
 * The Class OFAndroidSetup.
 */
public class OFAndroidSetup {

	/** The ndk root. */
	private String ndkRoot;
	
	/** The sdk root. */
	private String sdkRoot;
	
	/** The of root. */
	private String ofRoot;

	/**
	 * Instantiates a new OF android setup.
	 *
	 * @param ndkRoot the ndk root
	 * @param sdkRoot the sdk root
	 * @param ofRoot the of root
	 */
	public OFAndroidSetup(String ndkRoot, String sdkRoot, String ofRoot) {
		this.ndkRoot = ndkRoot;
		this.sdkRoot = sdkRoot;
		this.ofRoot = ofRoot;
	}

	/**
	 * Copy unpack and setup ant file.
	 *
	 * @return the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public File copyUnpackAndSetupAntFile() throws IOException {
		URL url = new URL(
				"platform:/plugin/ofAndroidclipse/ofAndroidUnpackAndSetup.xml");
		InputStream is = url.openConnection().getInputStream();

		File tmpFile = File.createTempFile("ofAndroidUnpackAndSetup", ".xml");
		ByteStreams.copy(is, new FileOutputStream(tmpFile));

		return tmpFile;
	}

	/**
	 * Execute setup.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void executeSetup() throws IOException {
		File setupAntFile = copyUnpackAndSetupAntFile();

		Project antPrj = new Project();

		antPrj.setUserProperty("ant.file", setupAntFile.getAbsolutePath());
		antPrj.init();

		ProjectHelper helper = ProjectHelper.getProjectHelper();
		antPrj.addReference("ant.projectHelper", helper);

		antPrj.setUserProperty("NDK_ROOT", ndkRoot);
		antPrj.setUserProperty("SDK_ROOT", sdkRoot);
		antPrj.setUserProperty("OF_ROOT", ofRoot);

		helper.parse(antPrj, setupAntFile);

		antPrj.executeTarget("SetSDK");

		setupAntFile.delete();
	}

	/**
	 * Import of libs.
	 *
	 * @param monitor the monitor
	 * @throws CoreException the core exception
	 */
	public void importOFLibs(IProgressMonitor monitor) throws CoreException {
		IPath pathToOFLibs = new Path(ofRoot).append(OFUtil.LIBS);
		OFUtil.importProjectFromPath(monitor, pathToOFLibs);
		
		IPath pathToOpenFrameworks = new Path(ofRoot).append(OFUtil.OPEN_FRAMEWORKS);
		OFUtil.importProjectFromPath(monitor, pathToOpenFrameworks);
	}

}
