package org.jimageviewer.actions;

import jimageviewer.ICommandIds;
import jimageviewer.View;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.jimageviewer.dialogs.BrightnessDialog;

public class QuitAction extends Action{
	    private final IWorkbenchWindow window;
	    public QuitAction(String text, IWorkbenchWindow window) {
	        super(text);
	        this.window = window;
	        // The id is used to refer to the action in a menu or toolbar
	        setId(ICommandIds.CMD_OPEN_MESSAGE);
	        // Associate the action with a pre-defined command, to allow key bindings.
	        setActionDefinitionId(ICommandIds.CMD_OPEN_MESSAGE);
	       // setImageDescriptor(jimageviewer.JImageViewerPlugin.getImageDescriptor("/icons/sample3.gif"));
	    }

	    public void run() {
	    if(window!=null){
			boolean question = MessageDialog.openQuestion(this.window.getShell(), "Are you sure you want to quit?", "Quit?");
			if(question==true){
				IWorkbenchAction action=ActionFactory.QUIT.create(window);
				action.run();}
				}

	    }
 }