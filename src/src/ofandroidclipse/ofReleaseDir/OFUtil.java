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

import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import ofandroidclipse.Activator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * The Class OFUtil.
 */
public final class OFUtil {

	/**
	 * Instantiates a new OF util.
	 */
	private OFUtil() {
	}

	/** The Constant LIBS. */
	public static final String LIBS = "libs";

	/** The Constant OPEN_FRAMEWORKS. */
	public static final String OPEN_FRAMEWORKS = "libs/openFrameworks";

	/** The Constant OF_ANDROIDLIB. */
	public static final String OF_ANDROIDLIB = "addons/ofxAndroid/ofAndroidLib";

	/** The Constant OF_ANRDOID_EXAMPLES. */
	public static final String OF_ANRDOID_EXAMPLES = "examples/android";

	/** The Constant OF_ANRDOID_EMPTY_EXAMPLE. */
	public static final String OF_ANRDOID_EMPTY_EXAMPLE = "androidEmptyExample";

	/** The Constant OF_APPS_DIR. */
	public static final String OF_APPS_DIR = "apps";

	private static Properties props = null;

	public enum BROWSE {
		NDK_ROOT, SDK_ROOT, OF_ROOT
	};

	/**
	 * The Class OFAndroidProjectParamsBuilder.
	 */
	public static class OFAndroidProjectParamsBuilder {

		/** The params. */
		private OFAndroidProjectParams params;

		/**
		 * New builder.
		 * 
		 * @return the OF android project params builder
		 */
		public static OFAndroidProjectParamsBuilder newBuilder() {
			return new OFAndroidProjectParamsBuilder();
		}

		/**
		 * Instantiates a new OF android project params builder.
		 */
		private OFAndroidProjectParamsBuilder() {
			params = new OFAndroidProjectParams();
		}

		/**
		 * Sets the project name.
		 * 
		 * @param projectName
		 *            the project name
		 * @return the OF android project params builder
		 */
		public OFAndroidProjectParamsBuilder setProjectName(String projectName) {
			params.projectName = projectName;
			return this;
		}

		/**
		 * Sets the path to of.
		 * 
		 * @param pathToOF
		 *            the path to of
		 * @return the OF android project params builder
		 */
		public OFAndroidProjectParamsBuilder setPathToOF(String pathToOF) {
			params.pathToOF = pathToOF;
			return this;
		}

		/**
		 * Sets the should import of android lib.
		 * 
		 * @param shouldImportOFAndroidLib
		 *            the should import of android lib
		 * @return the OF android project params builder
		 */
		public OFAndroidProjectParamsBuilder setShouldImportOFAndroidLib(
				Boolean shouldImportOFAndroidLib) {
			params.shouldImportOFAndroidLib = shouldImportOFAndroidLib;
			return this;
		}

		/**
		 * Sets the template project.
		 * 
		 * @param templatePrj
		 *            the template prj
		 * @return the OF android project params builder
		 */
		public OFAndroidProjectParamsBuilder setTemplateProject(
				String templatePrj) {
			params.templateProject = templatePrj;
			return this;
		}

		/**
		 * Adds the example to import.
		 * 
		 * @param example
		 *            the example
		 * @return the OF android project params builder
		 */
		public OFAndroidProjectParamsBuilder addExampleToImport(String example) {
			params.examplesToImport.add(example);
			return this;
		}

		/**
		 * Builds the.
		 * 
		 * @return the OF android project params
		 */
		public OFAndroidProjectParams build() {
			return params;
		}
	}

	/**
	 * The Class OFAndroidProjectParams.
	 */
	public static class OFAndroidProjectParams {

		/** The project name. */
		public String projectName;

		/** The path to of. */
		public String pathToOF;

		/** The should import of android lib. */
		public Boolean shouldImportOFAndroidLib;

		/** The template project. */
		public String templateProject;

		/** The examples to import. */
		public List<String> examplesToImport = new ArrayList<String>();
	}

	/**
	 * Path to of.
	 * 
	 * @return the string
	 */
	public static final String pathToOF() {
		if (props == null) {
			loadProperties();
		}

		return props.getProperty("Plugin.pathToOF");
	}

	private static void loadProperties() {
		props = new Properties();
		URL url;
		try {
			url = new URL("platform:/plugin/OFAndroidClipse/plugin.properties");
			InputStream is = url.openConnection().getInputStream();
			props.load(is);
		} catch (MalformedURLException e) {
			Activator.logException(e);
		} catch (IOException e) {
			Activator.logException(e);
		}
	}

	/**
	 * Import project from path.
	 * 
	 * @param monitor
	 *            the monitor
	 * @param pathToProject
	 *            the path to project
	 * @throws CoreException
	 *             the core exception
	 */
	public static void importProjectFromPath(IProgressMonitor monitor,
			IPath pathToProject) throws CoreException {
		IProjectDescription prjDesc = ResourcesPlugin.getWorkspace()
				.loadProjectDescription(pathToProject.append(".project"));

		IProject prj = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(prjDesc.getName());

		prj.create(prjDesc, monitor);

		prj.open(monitor);
	}

	public static void browse(BROWSE urlType) {

		if (props == null) {
			loadProperties();
		}

		String url = "";

		switch (urlType) {
		case NDK_ROOT:
			url = props.getProperty("Plugin.NDK_URL");
			break;

		case SDK_ROOT:
			url = props.getProperty("Plugin.SDK_URL");
			break;

		case OF_ROOT:
			url = props.getProperty("Plugin.OF_ANDROID_URL");
			break;

		default:
			return;
		}

		try {
			Desktop.getDesktop().browse(java.net.URI.create(url));
		} catch (IOException e) {
			Activator.logException(e);
		}
	}

}
