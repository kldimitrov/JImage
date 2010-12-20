package org.jimageviewer.actions;

import jimageviewer.ICommandIds;
import jimageviewer.NavigationView;
import jimageviewer.View;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;


public class UndoFilterAction extends Action {

    private final IWorkbenchWindow window;
    private boolean undo;
    public UndoFilterAction(String text, IWorkbenchWindow window, boolean undo) {
        super(text);
        this.window = window;
        this.undo=undo;
        // The id is used to refer to the action in a menu or toolbar
        setId(ICommandIds.CMD_UNDO);
        // Associate the action with a pre-defined command, to allow key bindings.
        setActionDefinitionId(ICommandIds.CMD_UNDO);
       // setImageDescriptor(jimageviewer.JImageViewerPlugin.getImageDescriptor("/icons/sample3.gif"));
    }

    public void run() {
    if(window!=null){
    	if(View.picture!=null){
    		if(undo==true)
    	View.picture.undo();
    		View.bars.getStatusLineManager().setMessage("Undo Successful");
    		
    	}
    	}	
    } 
}