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

import ofandroidclipse.util.DirectoryUtil;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.google.common.base.Optional;
import org.eclipse.wb.swt.ResourceManager;

/**
 * The Class SetupOpenFrameworksAndroidPage1.
 */
public class SetupOpenFrameworksAndroidPage1 extends WizardPage {
	
	/** The form toolkit. */
	private final FormToolkit formToolkit = new FormToolkit(
			Display.getDefault());
	
	/** The txt ndk root. */
	private Text txtNDKRoot;
	
	/** The txt sdk root. */
	private Text txtSDKRoot;
	
	/** The txt of root. */
	private Text txtOFRoot;

	/**
	 * Create the wizard.
	 */
	public SetupOpenFrameworksAndroidPage1() {
		super("wizardPage");
		setImageDescriptor(ResourceManager.getPluginImageDescriptor("ofAndroidclipse", "icons/ofIcon68x68.png"));
		setTitle("Setup OpenFrameworks for Android");
		setDescription("This wizard will automatically try to set up OpenFrameworks for Android.");
	}

	/**
	 * Create contents of the wizard.
	 *
	 * @param parent the parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(4, false));

		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblNewLabel.setText("NDK_ROOT");

		txtNDKRoot = formToolkit.createText(container, "New Text", SWT.NONE);
		txtNDKRoot.setText("");
		txtNDKRoot.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		txtNDKRoot.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				validate();
			}
		});

		Button btnBrowseNDK = formToolkit.createButton(container, "Browse",
				SWT.NONE);
		btnBrowseNDK.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Optional<String> path = DirectoryUtil.browse(getShell());
				if (path.isPresent()) {
					txtNDKRoot.setText(path.get());
				}
			}
		});

		Button btnViewNDKWeb = formToolkit.createButton(container, "Download",
				SWT.NONE);

		Label lblSdkroot = new Label(container, SWT.NONE);
		lblSdkroot.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblSdkroot.setText("SDK_ROOT");

		txtSDKRoot = formToolkit.createText(container, "New Text", SWT.NONE);
		txtSDKRoot.setText("");
		txtSDKRoot.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		txtSDKRoot.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				validate();
			}
		});

		Button btnBrowseSDK = formToolkit.createButton(container, "Browse",
				SWT.NONE);
		btnBrowseSDK.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Optional<String> path = DirectoryUtil.browse(getShell());
				if (path.isPresent()) {
					txtSDKRoot.setText(path.get());
				}
			}
		});

		Button btnViewSDKWeb = formToolkit.createButton(container, "Download",
				SWT.NONE);

		Label label = formToolkit.createSeparator(container, SWT.HORIZONTAL);
		GridData gd_label = new GridData(SWT.FILL, SWT.CENTER, false, false, 4,
				1);
		gd_label.widthHint = 48;
		label.setLayoutData(gd_label);

		Label lblOfZipFile = new Label(container, SWT.NONE);
		lblOfZipFile.setText("OF_ROOT");

		txtOFRoot = formToolkit.createText(container, "New Text",
				SWT.NONE);
		txtOFRoot.setText("");
		txtOFRoot.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		txtOFRoot.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				validate();
			}
		});

		Button btnBrowseOFArchive = formToolkit.createButton(container,
				"Browse", SWT.NONE);
		btnBrowseOFArchive.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Optional<String> path = DirectoryUtil.browse(getShell());
				if (path.isPresent()) {
					txtOFRoot.setText(path.get());
				}
			}
		});

		Button btnViewOFArchiveWeb = formToolkit.createButton(container,
				"Download", SWT.NONE);
		
		validate();
	}
	
	/**
	 * Validate.
	 */
	private void validate() {
		if (StringUtils.isEmpty(txtNDKRoot.getText())) {
			setErrorMessage("Path to NDK root is required");
			setPageComplete(false);
			return;
		}
		
		if (StringUtils.isEmpty(txtSDKRoot.getText())) {
			setErrorMessage("Path to SDK root is required");
			setPageComplete(false);
			return;
		}
		
		if (StringUtils.isEmpty(txtOFRoot.getText())) {
			setErrorMessage("Path to OpenFrameworks root is required");
			setPageComplete(false);
			return;
		}
		
		setErrorMessage(null);
		setPageComplete(true);
	}
	
	/**
	 * Gets the NDK root.
	 *
	 * @return the NDK root
	 */
	public String getNDKRoot() {
		return txtNDKRoot.getText().trim();
	}
	
	/**
	 * Gets the SDK root.
	 *
	 * @return the SDK root
	 */
	public String getSDKRoot() {
		return txtSDKRoot.getText().trim();
	}
	
	/**
	 * Gets the OF root.
	 *
	 * @return the OF root
	 */
	public String getOFRoot() {
		return txtOFRoot.getText().trim();
	}

}
