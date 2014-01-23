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

import ofandroidclipse.ofReleaseDir.OFAndroidSetup;

import org.apache.tools.ant.BuildException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * The Class SetupOpenFrameworksAndroid.
 */
public class SetupOpenFrameworksAndroid extends Wizard implements INewWizard {

	/** The page1. */
	private SetupOpenFrameworksAndroidPage1 page1;

	/**
	 * Instantiates a new setup open frameworks android.
	 */
	public SetupOpenFrameworksAndroid() {
		setWindowTitle("Setup OpenFrameworks for Android");
		setNeedsProgressMonitor(true);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		page1 = new SetupOpenFrameworksAndroidPage1();
		addPage(page1);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		final String ndkRoot = page1.getNDKRoot();
		final String sdkRoot = page1.getSDKRoot();
		final String ofRoot = page1.getOFRoot();

		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException, InterruptedException {
				try {
					doFinish(ndkRoot, sdkRoot, ofRoot, monitor);
				} catch (BuildException e) {
					throw new InvocationTargetException(e);
				} catch (IOException e) {
					throw new InvocationTargetException(e);
				} catch (CoreException e) {
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
	 * Do finish.
	 *
	 * @param ndkRoot the ndk root
	 * @param sdkRoot the sdk root
	 * @param ofRoot the of root
	 * @param monitor the monitor
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws CoreException the core exception
	 */
	private void doFinish(String ndkRoot, String sdkRoot, String ofRoot,
			IProgressMonitor monitor) throws IOException, CoreException {
		monitor.beginTask("Unpack and Setup OpenFrameworks for Android", 2);

		OFAndroidSetup setup = new OFAndroidSetup(ndkRoot,
				sdkRoot, ofRoot);
		
		setup.executeSetup();
		monitor.worked(1);
		
		setup.importOFLibs(monitor);
		monitor.worked(1);
		
		monitor.done();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

}
