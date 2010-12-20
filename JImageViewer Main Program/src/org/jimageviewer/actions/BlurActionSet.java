package org.jimageviewer.actions;

import jimageviewer.View;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.actions.ActionFactory;
import org.jimageviewer.dialogs.BrightnessDialog;
import org.jimageviewer.dialogs.RGBDialog;

public class BlurActionSet implements IWorkbenchWindowActionDelegate {

	private Shell shell;
	private IWorkbenchWindow window;
	private boolean question;
	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void init(IWorkbenchWindow window) {
		shell=window.getShell();
		this.window=window;
	}
	
	public void run(IAction action) {
		View.picture.blurFilter();
		View.bars.getStatusLineManager().setMessage("Blur Successful");
	}

	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

}
