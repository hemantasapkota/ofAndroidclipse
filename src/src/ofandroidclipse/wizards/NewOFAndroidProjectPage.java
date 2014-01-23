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

import ofandroidclipse.Activator;
import ofandroidclipse.ofReleaseDir.OFUtil;
import ofandroidclipse.ofReleaseDir.OFUtil.OFAndroidProjectParams;
import ofandroidclipse.ofReleaseDir.OFUtil.OFAndroidProjectParamsBuilder;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.wb.swt.ResourceManager;

import com.google.common.base.Optional;

/**
 * The Class NewOFAndroidProjectPage.
 */
public class NewOFAndroidProjectPage extends WizardPage {
	
	/**
	 * The Class TableLabelProvider.
	 */
	private class TableLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		
		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
		 */
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
		 */
		public String getColumnText(Object element, int columnIndex) {
			File f = (File) element;
			return f.getName();
		}
	}

	/**
	 * The Class ContentProvider.
	 */
	private class ContentProvider implements IStructuredContentProvider {
		
		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
		 */
		public Object[] getElements(Object inputElement) {
			return ofxAndroidExamples.toArray();
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
		 */
		public void dispose() {
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
		 */
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	/** The txt project name. */
	private Text txtProjectName;

	/** The lblproject name. */
	private Label lblprojectName;
	
	/** The gd_txt project name. */
	private GridData gd_txtProjectName;
	
	/** The lbl path to of. */
	private Label lblPathToOf;
	
	/** The txt pathto of. */
	private Text txtPathtoOF;
	
	/** The btn browse. */
	private Button btnBrowse;
	
	/** The btn import of android lib. */
	private Button btnImportOFAndroidLib;
	
	/** The tab folder. */
	private TabFolder tabFolder;
	
	/** The tbtm import examples. */
	private TabItem tbtmImportExamples;

	/** The delegate. */
	private NewOFAndroidProjectPageDelegate delegate;
	
	/** The cmp examples. */
	private Composite cmpExamples;
	
	/** The table. */
	private Table table;
	
	/** The checkbox table viewer. */
	private CheckboxTableViewer checkboxTableViewer;
	
	/** The ofx android examples. */
	private List<File> ofxAndroidExamples = new ArrayList<File>();
	
	/** The tblclmn select all. */
	private TableColumn tblclmnSelectAll;
	
	/** The table viewer column. */
	private TableViewerColumn tableViewerColumn;
	
	/** The tbtm template. */
	private TabItem tbtmTemplate;
	
	/** The form toolkit. */
	private final FormToolkit formToolkit = new FormToolkit(
			Display.getDefault());
	
	/** The scrolled form. */
	private ScrolledForm scrolledForm;
	
	/** The cmp template. */
	private Composite cmpTemplate;

	/** The template project. */
	private String templateProject = OFUtil.OF_ANRDOID_EMPTY_EXAMPLE;

	/**
	 * Constructor for SampleNewWizardPage.
	 */
	public NewOFAndroidProjectPage() {
		super("wizardPage");
		setMessage("OpenFrameworks for Android requires ADT plugin. If ADT plugin is not installed, please do so from the Eclipse Marketplace.\n\n");
		setImageDescriptor(ResourceManager.getPluginImageDescriptor("OFAndroidClipse", "icons/ofIcon68x68.png"));
		setTitle("OpenFrameworks Android Project");
		setDescription("This wizard creates new android project for OpenFrameworks.");

		delegate = new NewOFAndroidProjectPageDelegate();
	}

	/**
	 * Creates the control.
	 *
	 * @param parent the parent
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 3;
		layout.verticalSpacing = 9;
		container.setLayout(layout);
		layout.numColumns = 3;
		lblprojectName = new Label(container, SWT.NULL);
		lblprojectName.setText("&Project name:");

		txtProjectName = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd_txtProjectName = new GridData(GridData.FILL_HORIZONTAL);
		txtProjectName.setLayoutData(gd_txtProjectName);
		txtProjectName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				validate();
			}
		});
		initialize();
		setControl(container);
		new Label(container, SWT.NONE);

		lblPathToOf = new Label(container, SWT.NONE);
		lblPathToOf.setText("Path to OF");

		txtPathtoOF = new Text(container, SWT.BORDER);
		txtPathtoOF.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		btnBrowse = new Button(container, SWT.NONE);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					doBrowse();
				} catch (CoreException e1) {
					Activator.logException(e1);
				}
			}
		});
		btnBrowse.setText("Browse");
		new Label(container, SWT.NONE);

		btnImportOFAndroidLib = new Button(container, SWT.CHECK);
		btnImportOFAndroidLib.setText("Import ofAndroidLib");
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);

		tabFolder = new TabFolder(container, SWT.NONE);
		GridData gd_tabFolder = new GridData(SWT.FILL, SWT.FILL, false, true,
				1, 1);
		gd_tabFolder.widthHint = 414;
		gd_tabFolder.heightHint = 65;
		tabFolder.setLayoutData(gd_tabFolder);

		tbtmTemplate = new TabItem(tabFolder, SWT.NONE);
		tbtmTemplate.setText("Template");

		scrolledForm = formToolkit.createScrolledForm(tabFolder);
		tbtmTemplate.setControl(scrolledForm);
		formToolkit.paintBordersFor(scrolledForm);
		scrolledForm.getBody().setLayout(new FillLayout(SWT.HORIZONTAL));

		cmpTemplate = new Composite(scrolledForm.getBody(), SWT.NONE);
		formToolkit.adapt(cmpTemplate);
		formToolkit.paintBordersFor(cmpTemplate);
		cmpTemplate.setLayout(new GridLayout(2, true));

		tbtmImportExamples = new TabItem(tabFolder, SWT.NONE);
		tbtmImportExamples.setText("Import Extra Samples");

		cmpExamples = new Composite(tabFolder, SWT.NONE);
		tbtmImportExamples.setControl(cmpExamples);
		cmpExamples.setLayout(new FillLayout(SWT.HORIZONTAL));

		checkboxTableViewer = CheckboxTableViewer.newCheckList(cmpExamples,
				SWT.BORDER | SWT.FULL_SELECTION);
		table = checkboxTableViewer.getTable();
		table.setHeaderVisible(true);

		tableViewerColumn = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		tableViewerColumn.getColumn().addSelectionListener(
				new SelectionListener() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						selectAllExamples();
					}

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
					}
				});
		tblclmnSelectAll = tableViewerColumn.getColumn();
		tblclmnSelectAll.setWidth(374);
		tblclmnSelectAll.setText("Select All");
		checkboxTableViewer.setLabelProvider(new TableLabelProvider());
		checkboxTableViewer.setContentProvider(new ContentProvider());
		new Label(container, SWT.NONE);

		validate();
	}

	/**
	 * Select all examples.
	 */
	private void selectAllExamples() {
		checkboxTableViewer.setAllChecked(true);
	}

	/**
	 * Do browse.
	 *
	 * @throws CoreException the core exception
	 */
	private void doBrowse() throws CoreException {
		DirectoryDialog dd = new DirectoryDialog(getShell());
		String p = dd.open();

		Optional<String> path = Optional.fromNullable(p);
		if (!path.isPresent()) {
			return;
		}

		txtPathtoOF.setText(path.get());
		setPageComplete(true);
		setErrorMessage(null);

		/* List all the examples */
		ofxAndroidExamples = delegate.getExamples(path.get());
		checkboxTableViewer.setInput(ofxAndroidExamples);
		checkboxTableViewer.refresh();

		for (File f : ofxAndroidExamples) {
			final Button chk = new Button(cmpTemplate, SWT.RADIO);
			chk.setText(f.getName());
			chk.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					templateProject = chk.getText();
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}
			});
		}
		scrolledForm.reflow(true);
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {
	}

	/**
	 * Ensures that both text fields are set.
	 */

	private void validate() {
		if (StringUtils.isEmpty(txtProjectName.getText())) {
			setErrorMessage("Project name is required");
			setPageComplete(false);
			return;
		}

		if (StringUtils.isEmpty(txtPathtoOF.getText())) {
			setErrorMessage("Path to OpenFrameworks is required");
			setPageComplete(false);
			return;
		}

		setErrorMessage(null);
		setPageComplete(true);
	}

	/**
	 * Builds the params.
	 *
	 * @return the OF android project params
	 */
	public OFAndroidProjectParams buildParams() {
		OFAndroidProjectParamsBuilder builder = OFAndroidProjectParamsBuilder.newBuilder()
				.setProjectName(getProjectName()).setPathToOF(getPathToOF())
				.setShouldImportOFAndroidLib(shouldImportOFAndroidLib())
				.setTemplateProject(getTemplateProject());
		
		for (Object o : checkboxTableViewer.getCheckedElements()) {
			builder.addExampleToImport(o.toString());
		}
		
		return builder.build();
	}

	/**
	 * Gets the project name.
	 *
	 * @return the project name
	 */
	private String getProjectName() {
		return txtProjectName.getText().trim();
	}

	/**
	 * Gets the path to of.
	 *
	 * @return the path to of
	 */
	private String getPathToOF() {
		return txtPathtoOF.getText().trim();
	}

	/**
	 * Gets the template project.
	 *
	 * @return the template project
	 */
	private String getTemplateProject() {
		return templateProject;
	}

	/**
	 * Should import of android lib.
	 *
	 * @return the boolean
	 */
	private Boolean shouldImportOFAndroidLib() {
		return btnImportOFAndroidLib.getSelection();
	}
}