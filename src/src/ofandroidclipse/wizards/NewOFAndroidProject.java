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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import ofandroidclipse.ofReleaseDir.OFUtil.OFAndroidProjectParams;
import ofandroidclipse.ofReleaseDir.OFAndroidProjectGenerator;

import org.apache.tools.ant.BuildException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

/**
 */

public class NewOFAndroidProject extends Wizard implements INewWizard {
	
	/** The page. */
	private NewOFAndroidProjectPage page;

	/**
	 * Constructor for NewOFXAndroidProject.
	 */
	public NewOFAndroidProject() {
		super();
		setWindowTitle("New OpenFrameworks Android Project");
		setNeedsProgressMonitor(true);
	}

	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		page = new NewOFAndroidProjectPage();
		addPage(page);
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We
	 * will create an operation and run it using wizard as execution context.
	 *
	 * @return true, if successful
	 */
	public boolean performFinish() {

		final OFAndroidProjectParams params = page.buildParams();

		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException, InterruptedException {
				try {
					doFinish(params, monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} catch (MalformedURLException e) {
					throw new InvocationTargetException(e);
				} catch (BuildException e) {
					throw new InvocationTargetException(e);
				} catch (URISyntaxException e) {
					throw new InvocationTargetException(e);
				} catch (IOException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error",
					realException.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * The worker method. It will find the container, create the file if missing
	 * or just replace its contents, and open the editor on the newly created
	 * file.
	 *
	 * @param params the params
	 * @param monitor the monitor
	 * @throws CoreException the core exception
	 * @throws BuildException the build exception
	 * @throws URISyntaxException the URI syntax exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws InvocationTargetException the invocation target exception
	 * @throws InterruptedException the interrupted exception
	 */

	private void doFinish(OFAndroidProjectParams params,
			IProgressMonitor monitor) throws CoreException, BuildException,
			URISyntaxException, IOException, InvocationTargetException,
			InterruptedException {

		monitor.beginTask("Importing project", 2);

		OFAndroidProjectGenerator generator = new OFAndroidProjectGenerator(params);

		if (params.shouldImportOFAndroidLib) {
			generator.importOFAndroidLibProject(monitor);
		}
		monitor.worked(1);

		generator.executeGenerate();
		monitor.worked(1);

		generator.importGeneratedProject(monitor);
		monitor.worked(1);
		
		generator.importExamples(monitor);

		monitor.done();
	}

	/**
	 * We will accept the selection in the workbench to see if we can initialize
	 * from it.
	 *
	 * @param workbench the workbench
	 * @param selection the selection
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}
}