package org.jimageviewer.dialogs;

import jimageviewer.SWTImageCanvas;
import jimageviewer.View;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;

public class RGBDialog extends TitleAreaDialog {
	private Scale red;
	private Scale blue;
	private Scale green;
	
	
	public RGBDialog(Shell parentShell) {
		super(parentShell);
	}
	protected Control createContents(Composite parent){
	Control contents=super.createContents(parent);
	setTitle("Control RGB of image");
	setMessage("Red Blue Green Control");
	return contents;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		
		org.eclipse.swt.layout.GridLayout layout=new
			org.eclipse.swt.layout.GridLayout();
		GridData layoutData=new GridData();
		layout.numColumns=2;
		layoutData.verticalAlignment=GridData.CENTER;
		parent.setLayout(layout);
		final PaletteData palette=View.picture.getPalette();
		int redPosition = 0;
		Label redLabel=new Label(parent, SWT.NONE);
		redLabel.setText("Control red");
		red=new Scale(parent, SWT.NONE);
		red.setMinimum(0);
		red.setMaximum(20);
		red.setIncrement(1);
		View.picture.redPosition=(int)Math.sqrt(SWTImageCanvas.getImDataDialog().palette.redMask);
		red.setSelection(View.picture.redPosition);
		
		Label blueLabel=new Label(parent, SWT.NONE);
		blueLabel.setText("Control blue");
		blue=new Scale(parent, SWT.NONE);
		blue.setMinimum(0);
		blue.setMaximum(20);
		blue.setIncrement(1);
		View.picture.bluePosition=(int)Math.sqrt(SWTImageCanvas.getImDataDialog().palette.blueMask);
		blue.setSelection(View.picture.bluePosition);
		Label greenLabel=new Label(parent, SWT.NONE);
		greenLabel.setText("Control green:");
		green=new Scale(parent, SWT.NONE);
		green.setMinimum(0);
		green.setMaximum(20);
		green.setIncrement(1);
		View.picture.greenPosition=(int)Math.sqrt(SWTImageCanvas.getImDataDialog().palette.blueMask);
		green.setSelection(View.picture.greenPosition);

//Listerners
		blue.addSelectionListener(new SelectionListener(){

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void widgetSelected(SelectionEvent e) {
					if(View.picture.bluePosition>blue.getSelection()){
						if((View.picture.bluePosition<10)||(View.picture.bluePosition>1600000)) return;
						palette.blueMask=palette.blueMask/2;
						View.picture.bluePosition=blue.getSelection();}
					else if(View.picture.bluePosition<blue.getSelection()){
						if((View.picture.bluePosition<10)||(View.picture.bluePosition>1600000)) return;						palette.blueMask=palette.blueMask*2;
						View.picture.bluePosition=blue.getSelection();
						}
					View.picture.setPalette(palette);
					//MessageDialog.openInformation(getShell(), "Error", Integer.toString(palette.blueMask)+
						//		" "+Integer.toString(palette.greenMask)+" "+Integer.toString(palette.redMask)); 
			}
		});
		
		green.addSelectionListener(new SelectionListener(){

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void widgetSelected(SelectionEvent e) {
				if(View.picture.greenPosition>green.getSelection()){
					if((View.picture.greenPosition<10)||(View.picture.greenPosition>1600000)) return;
					palette.greenMask=palette.greenMask/2;
					View.picture.greenPosition=green.getSelection();}
				else if(View.picture.greenPosition<green.getSelection()){
					if((View.picture.greenPosition<10)||(View.picture.greenPosition>1600000)) return;
					palette.greenMask=palette.greenMask*2;
					View.picture.greenPosition=green.getSelection();
					}
				View.picture.setPalette(palette);

			}
			
		});
		
		red.addSelectionListener(new SelectionListener(){

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void widgetSelected(SelectionEvent e) {
				if(View.picture.redPosition>red.getSelection()){
					if((View.picture.redPosition<10)||(View.picture.redPosition>1600000)) return;
					palette.redMask=palette.redMask/2;
					View.picture.redPosition=red.getSelection();}
				else if(View.picture.redPosition<red.getSelection()){
					if((View.picture.redPosition<10)||(View.picture.redPosition>1600000)) return;
					palette.redMask=palette.redMask*2;
					View.picture.redPosition=red.getSelection();
					}
				View.picture.setPalette(palette);
				
			}
			
		});
		
		return parent;	
	}
}
