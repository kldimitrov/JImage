package org.jimageviewer.actions;

import jimageviewer.ICommandIds;
import jimageviewer.View;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.jimageviewer.dialogs.RGBDialog;

public class RGBAction extends Action {
    private final IWorkbenchWindow window;
    int filter;
    public RGBAction(String text, IWorkbenchWindow window) {
        super(text);
        this.window = window;
        this.filter=filter;
        // The id is used to refer to the action in a menu or toolbar
        setId(ICommandIds.CMD_OPEN_MESSAGE);
        // Associate the action with a pre-defined command, to allow key bindings.
        setActionDefinitionId(ICommandIds.CMD_OPEN_MESSAGE);
       // setImageDescriptor(jimageviewer.JImageViewerPlugin.getImageDescriptor("/icons/sample3.gif"));
    }

    public void run() {
    if(window!=null){
    	if(View.picture!=null){
			RGBDialog dialog=new RGBDialog(window.getShell());
			dialog.open();

    }else MessageDialog.openInformation(window.getShell(), "Error", "Load image first"); 

}
}
}