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
package ofandroidclipse.util;

import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;

import com.google.common.base.Optional;

/**
 * The Class DirectoryUtil.
 */
public class DirectoryUtil {

	/**
	 * Browse.
	 *
	 * @param shell the shell
	 * @return the optional
	 */
	public static Optional<String> browse(Shell shell) {
		DirectoryDialog dd = new DirectoryDialog(shell);
		String p = dd.open();
		return Optional.fromNullable(p);
	}

}
