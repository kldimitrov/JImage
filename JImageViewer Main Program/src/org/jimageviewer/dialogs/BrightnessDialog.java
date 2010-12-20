package org.jimageviewer.dialogs;

import java.awt.GridLayout;

import jimageviewer.View;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;

public class BrightnessDialog extends TitleAreaDialog {
	private Scale kiki;
	
	
	public BrightnessDialog(Shell parentShell) {
		super(parentShell);
	}
	protected Control createContents(Composite parent){
		Control contents=super.createContents(parent);
	setTitle("Control brightness of image");
	setMessage("Brightness Control");
	return contents;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		GridLayout layout=new GridLayout();
		kiki=new Scale(parent, SWT.NONE);
		kiki.setMaximum(40);
		kiki.setIncrement(1);
		kiki.setSelection(View.picture.bright);
		
		kiki.addSelectionListener(new SelectionListener(){
			
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void widgetSelected(SelectionEvent e) {
				float j=(short) (kiki.getSelection()-View.picture.bright)%10;
				float mod=0.0f;
				if(j<0){
					j=-j;
					mod=0.75f;
				}
					else
					mod=1.25f;
				
				
				View.picture.bright=kiki.getSelection();
				if (j==0) return;
				if((j>2)||(j<-2)) return;
					View.picture.brightnessFilter((float) (mod*j));
				}
			
		});
	return parent;
	}
	

}
