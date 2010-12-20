package org.jimageviewer.actions;

import jimageviewer.ICommandIds;
import jimageviewer.View;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;

public class FlipImageAction extends Action{

    private final IWorkbenchWindow window;
    private boolean vertical;
    public FlipImageAction(String text, IWorkbenchWindow window,boolean vertical) {
        super(text);
        this.window = window;
        this.vertical=vertical;
        // The id is used to refer to the action in a menu or toolbar
        setId(ICommandIds.CMD_OPEN_MESSAGE);
        // Associate the action with a pre-defined command, to allow key bindings.
        setActionDefinitionId(ICommandIds.CMD_OPEN_MESSAGE);
       // setImageDescriptor(jimageviewer.JImageViewerPlugin.getImageDescriptor("/icons/sample3.gif"));
    }

    public void run() {
    if(window!=null){
    	if(View.picture!=null){
    		View.picture.flip(vertical);
    		}
    	}	
    } 

}
