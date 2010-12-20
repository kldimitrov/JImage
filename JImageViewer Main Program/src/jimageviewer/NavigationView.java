package jimageviewer;

import javax.sound.sampled.Line;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

public class NavigationView<FormToolkit> extends ViewPart {
	public static final String ID = "JImageViewer.navigationView";
	public static Text text;
	private FontRegistry fontRegister;
	public static Composite composite;
	public NavigationView() {
		super();
	}
	@Override
	public void createPartControl(Composite parent) {
		composite=parent;
		text=new Text(parent,SWT.MULTI|SWT.BORDER);
		fontRegister=new FontRegistry(parent.getDisplay());
		fontRegister.put("title", new FontData[]{new FontData("Arial",20,SWT.BOLD)});
		fontRegister.put("text", new FontData[]{new FontData("Arial",9,SWT.NONE)});
		text.setEditable(false);
		String welcomeString=new String();
		text.setBackground(new Color(parent.getDisplay(), 245,245,220));
		text.setFont(fontRegister.get(("text")));
		welcomeString="        Welcome!!!"+"\n";
		for(int i=0;i<3;i++)
			welcomeString+='\n';
		
		welcomeString+="JImage is a photo\n" +
						"editing software.\n" +
						"It can be used to \n"+
						"enhance your images\n"+
						"whether are are\n"+
						"novice or expert.";
		for(int i=0;i<3;i++)
			welcomeString+='\n';
		welcomeString+="With this product\n"+
					   "you can use filters,\n" +
					   "control RGB schema,\n" +
					   "scale and rotate your\n" +
					   "pictures.";
		for(int i=0;i<3;i++)
			welcomeString+='\n';		
		welcomeString+="JImage is an open\n" +
					   "source product written\n" +
					   "in Java using RCP,SWT,\n" +
					   "JFace and Java2D API.";
		
		text.setText(welcomeString);
		}
	static public void fillView(){
		String infoString=new String();
		text.setBackground(new Color(composite.getDisplay(), 245,245,220));
		infoString="Original Image Info\n";
		for(int i=0;i<3;i++)
			infoString+='\n';
		infoString+="Image Height\n";
		infoString+=Integer.toString(View.picture.getHeight());
		for(int i=0;i<3;i++)
			infoString+='\n';
		infoString+="Image Width\n";
		infoString+=Integer.toString(View.picture.getWidth());
		
		text.setText(infoString);
	}
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

}