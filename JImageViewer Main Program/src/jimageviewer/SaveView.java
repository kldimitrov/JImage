package jimageviewer;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISaveablePart;
import org.eclipse.ui.part.ViewPart;

public class SaveView extends ViewPart implements ISaveablePart {

	protected boolean dirty=false;
	
	public SaveView(){}
	@Override
	public void createPartControl(Composite parent) {
		if(SWTImageCanvas.isDirty())
			firePropertyChange(ISaveablePart.PROP_DIRTY);

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public void doSave(IProgressMonitor monitor) {
		//
		SWTImageCanvas.setDirty();
		firePropertyChange(ISaveablePart.PROP_DIRTY);

	}

	public void doSaveAs() {
		// TODO Auto-generated method stub


	}

	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isSaveOnCloseNeeded() {
		// TODO Auto-generated method stub
		return true;
	}

}
