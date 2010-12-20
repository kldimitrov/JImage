/*******************************************************************************
* Copyright (c) 2004 Chengdong Li : cdli@ccs.uky.edu
* All rights reserved. This program and the accompanying materials 
* are made available under the terms of the Common Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/cpl-v10.html
*******************************************************************************/
package jimageviewer;




import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Vector;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchWindow;


public class SWTImageCanvas extends Canvas {
	final float ZOOMIN_RATE = 1.1f; /* zoomin rate */
	final float ZOOMOUT_RATE = 0.9f; /* zoomout rate */
	private Image sourceImage; /* original image */
	private Image screenImage; /* screen image */
	private AffineTransform transform = new AffineTransform();
	private int alphaFilter;
	public int bright=20;
	private String currentDir=""; /* remembering file open directory */
	static Scale scale;
	static Text value;
	private ImageLoader loader;
	private String filename;
	public static int bluePosition,greenPosition,redPosition;
	//
	public static ImageData imDataDialog=null;
	static boolean dirty=false;
	Vector<ImageData> undoList;
	int	iterator;
	ImageData firstData;
	private String saveName;
	public SWTImageCanvas(final Composite parent) {
		this(parent, SWT.NULL);
	}

	/**
	 * Constructor for ScrollableCanvas.
	 * @param parent the parent of this control.
	 * @param style the style of this control.
	 */
	public SWTImageCanvas(final Composite parent, int style) {
		super( parent, style|SWT.BORDER|SWT.V_SCROLL|SWT.H_SCROLL
				            | SWT.NO_BACKGROUND);
		addControlListener(new ControlAdapter() { /* resize listener. */
			public void controlResized(ControlEvent event) {
				syncScrollBars();
				
			}
		});
		addPaintListener(new PaintListener() { /* paint listener. */
			public void paintControl(final PaintEvent event) {
				paint(event.gc);
			}
		});
		initScrollBars();
		undoList=new Vector<ImageData>();
		saveName=null;
	}

	public void dispose() {
		if (sourceImage != null && !sourceImage.isDisposed()) {
			sourceImage.dispose();
		}
		if (screenImage != null && !screenImage.isDisposed()) {
			screenImage.dispose();
		}
	}
	private void paint(GC gc) {
		Rectangle clientRect = getClientArea(); /* Canvas' painting area */
		if (sourceImage != null) {
			Rectangle imageRect =
				SWT2Dutil.inverseTransformRect(transform, clientRect);
			int gap = 2; /* find a better start point to render */
			imageRect.x -= gap; imageRect.y -= gap;
			imageRect.width += 2 * gap; imageRect.height += 2 * gap;

			Rectangle imageBound = sourceImage.getBounds();
			imageRect = imageRect.intersection(imageBound);
			Rectangle destRect = SWT2Dutil.transformRect(transform, imageRect);

			if (screenImage != null)
				screenImage.dispose();
			screenImage =
				new Image(getDisplay(), clientRect.width, clientRect.height);
			GC newGC = new GC(screenImage);
			newGC.setClipping(clientRect);
			newGC.drawImage(
				sourceImage,
				imageRect.x,
				imageRect.y, 
				imageRect.width,
				imageRect.height,
				destRect.x,
				destRect.y,
				destRect.width,
				destRect.height);
			newGC.dispose();

			gc.drawImage(screenImage, 0, 0);
		} else {
			gc.setClipping(clientRect);
			gc.fillRectangle(clientRect);
			initScrollBars();
		}
	}

	private void initScrollBars() {
		ScrollBar horizontal = getHorizontalBar();
		horizontal.setEnabled(false);
		horizontal.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				scrollHorizontally((ScrollBar) event.widget);
			}
		});
		ScrollBar vertical = getVerticalBar();
		vertical.setEnabled(false);
		vertical.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				scrollVertically((ScrollBar) event.widget);
			}
		});
	}

	private void scrollHorizontally(ScrollBar scrollBar) {
		if (sourceImage == null)
			return;

		AffineTransform af = transform;
		double tx = af.getTranslateX();
		double select = -scrollBar.getSelection();
		af.preConcatenate(AffineTransform.getTranslateInstance(select - tx, 0));
		transform = af;
		syncScrollBars();
	}

	private void scrollVertically(ScrollBar scrollBar) {
		if (sourceImage == null)
			return;

		AffineTransform af = transform;
		double ty = af.getTranslateY();
		double select = -scrollBar.getSelection();
		af.preConcatenate(AffineTransform.getTranslateInstance(0, select - ty));
		transform = af;
		syncScrollBars();
	}

	public Image getSourceImage() {
		return sourceImage;
	}

	public void syncScrollBars() {
		if (sourceImage == null) {
			redraw();
			return;
		}

		AffineTransform af = transform;
		double sx = af.getScaleX(), sy = af.getScaleY();
		double tx = af.getTranslateX(), ty = af.getTranslateY();
		if (tx > 0) tx = 0;
		if (ty > 0) ty = 0;

		ScrollBar horizontal = getHorizontalBar();
		horizontal.setIncrement((int) (getClientArea().width / 100));
		horizontal.setPageIncrement(getClientArea().width);
		Rectangle imageBound = sourceImage.getBounds();
		int cw = getClientArea().width, ch = getClientArea().height;
		if (imageBound.width * sx > cw) { /* image is wider than client area */
			horizontal.setMaximum((int) (imageBound.width * sx));
			horizontal.setEnabled(true);
			if (((int) - tx) > horizontal.getMaximum() - cw)
				tx = -horizontal.getMaximum() + cw;
		} else { /* image is narrower than client area */
			horizontal.setEnabled(false);
			tx = (cw - imageBound.width * sx) / 2; //center if too small.
		}
		horizontal.setSelection((int) (-tx));
		horizontal.setThumb((int) (getClientArea().width));

		ScrollBar vertical = getVerticalBar();
		vertical.setIncrement((int) (getClientArea().height / 100));
		vertical.setPageIncrement((int) (getClientArea().height));
		if (imageBound.height * sy > ch) { /* image is higher than client area */
			vertical.setMaximum((int) (imageBound.height * sy));
			vertical.setEnabled(true);
			if (((int) - ty) > vertical.getMaximum() - ch)
				ty = -vertical.getMaximum() + ch;
		} else { /* image is less higher than client area */
			vertical.setEnabled(false);
			ty = (ch - imageBound.height * sy) / 2; //center if too small.
		}
		vertical.setSelection((int) (-ty));
		vertical.setThumb((int) (getClientArea().height));

		/* update transform. */
		af = AffineTransform.getScaleInstance(sx, sy);
		af.preConcatenate(AffineTransform.getTranslateInstance(tx, ty));
		transform = af;;
		redraw();
	}
	
	public void onFileSave(){
		FileDialog dialog=new FileDialog(getShell(),SWT.SAVE);
		dialog.setFilterExtensions(
				new String[] { "*.gif; *.jpg; *.png; *.ico; *.bmp" });
		dialog.setFilterNames(
				new String[] { "SWT image" + " (gif, jpeg, png, ico, bmp)" });
		dialog.setFilterPath(currentDir);
		saveName=dialog.getFileName();
		System.out.println(saveName);
		saveImage(dialog.open());
		
	}
	public void saveImage(String name){
		if(name==null)
			return;
		try{
			if(saveName==null){
				name=filename;
			}
		loader=new ImageLoader();
		ImageData imData=getImageData();
		loader.data=new ImageData[]{imData};
		loader.save(name,SWT.IMAGE_JPEG);
		}catch(Exception e){}
	}

	public Image loadImage(String filename) {
		if (sourceImage != null && !sourceImage.isDisposed()) {
			sourceImage.dispose();
			sourceImage = null;
			undoList.removeAllElements();
			iterator=0;
			
		}
		sourceImage = new Image(getDisplay(), filename);
		undoList.add(getImageData());
		iterator=0;
		firstData=getImageData();
		alphaFilter=sourceImage.getImageData().alpha;
		NavigationView.fillView();
		showOriginal();
		return sourceImage;
	}
	public void menuSave() {
	}
	
	public void onFileOpen() {
		FileDialog fileChooser = new FileDialog(getShell(), SWT.OPEN);
		fileChooser.setText("Open image file");
		fileChooser.setFilterPath(currentDir);
		fileChooser.setFilterExtensions(
			new String[] { "*.gif; *.jpg; *.png; *.ico; *.bmp" });
		fileChooser.setFilterNames(
			new String[] { "SWT image" + " (gif, jpeg, png, ico, bmp)" });
		filename = fileChooser.open();
		if (filename != null){
			loadImage(filename);
			currentDir = fileChooser.getFilterPath();
		}
	}

	public ImageData getImageData() {
		imDataDialog=sourceImage.getImageData();
		return sourceImage.getImageData();
	}
	public int getWidth(){
		return sourceImage.getBounds().width;
	}
	public int getHeight(){
		return sourceImage.getBounds().height;
	}
	public void setImageData(ImageData data) {
		if (sourceImage != null)
			sourceImage.dispose();
		if (data != null)
			sourceImage = new Image(getDisplay(), data);
		syncScrollBars();
		undoList.add(data);
		iterator=iterator+1;
		dirty=true;
	}

	/**
	 * Fit the image onto the canvas
	 */
	public void fitCanvas() {
		if (sourceImage == null)
			return;
		Rectangle imageBound = sourceImage.getBounds();
		Rectangle destRect = getClientArea();
		double sx = (double) destRect.width / (double) imageBound.width;
		double sy = (double) destRect.height / (double) imageBound.height;
		double s = Math.min(sx, sy);
		double dx = 0.5 * destRect.width;
		double dy = 0.5 * destRect.height;
		centerZoom(dx, dy, s, new AffineTransform());
	}

	void setAlpha(int alpha){
		sourceImage.getImageData().alpha=alpha;
	}
	public void showOriginal() {
		if (sourceImage == null)
			return;
		transform = new AffineTransform();
		syncScrollBars();
	}
	public void centerZoom(
		double dx,
		double dy,
		double scale,
		AffineTransform af) {
		af.preConcatenate(AffineTransform.getTranslateInstance(dx, dy));
		af.preConcatenate(AffineTransform.getScaleInstance(scale, scale));
		af.preConcatenate(AffineTransform.getTranslateInstance(-dx, -dy));
		transform = af;
		syncScrollBars();
	}
	public void moveRG(){
		if (sourceImage == null)
			return;
		ImageData imData=getImageData();
		int redMask=imData.palette.redMask;
		int greenMask=imData.palette.greenMask;
		imData.palette.greenMask=redMask;
		imData.palette.redMask=greenMask;
		setImageData(imData);

	}
	
	public void moveRB(){
		if (sourceImage == null)
			return;
		ImageData imData=getImageData();
		int redMask=imData.palette.redMask;
		int blueMask=imData.palette.blueMask;
		imData.palette.blueMask=redMask;
		imData.palette.redMask=blueMask;
		setImageData(imData);

	}
	public void moveBG(){
		if (sourceImage == null)
			return;
		ImageData imData=getImageData();
		int blueMask=imData.palette.blueMask;
		int greenMask=imData.palette.greenMask;
		imData.palette.greenMask=blueMask;
		imData.palette.blueMask=greenMask;
		setImageData(imData);

	}
	
	public PaletteData getPalette(){
		return getImageData().palette;
	}
	public void setPalette(PaletteData palette){
		ImageData imData=getImageData();
		imData.palette=palette;
		setImageData(imData);
	}
	
	public void zoomIn(){
		if (sourceImage == null)
			return;
		
		setImageData(getImageData().scaledTo((int)(getWidth()*ZOOMIN_RATE), (int)(getHeight()*ZOOMIN_RATE)));

	}
	public void zoomOut(){
		if (sourceImage == null)
			return;
		
		setImageData(getImageData().scaledTo((int)(getWidth()*ZOOMOUT_RATE), (int)(getHeight()*ZOOMOUT_RATE)));

	}
	public void deleteRed(){
		if (sourceImage == null)
			return;
		ImageData imData=getImageData();
		imData.palette.redMask=0;
		setImageData(imData);

	}
	public void rotate(int direction){
		if (sourceImage == null)
			return;
		ImageData srcData = getImageData();
		int bytesPerPixel=srcData.bytesPerLine/srcData.width;
		int destBytesPerLine=(direction==SWT.DOWN)?srcData.width*bytesPerPixel:srcData.height
				*bytesPerPixel;
		byte[] newData=new byte[(direction==SWT.DOWN)?srcData.height*destBytesPerLine:
			srcData.width*destBytesPerLine];
		int width=0, height=0;
		for(int srcY=0;srcY<srcData.height;srcY++){
			for(int srcX=0;srcX<srcData.width;srcX++){
				int destX=0, destY=0,destIndex=0,srcIndex=0;
				switch(direction){
							case SWT.LEFT:
								destX=srcY;
								destY=srcData.width-srcX-1;
								height=srcData.width;
								width=srcData.height;
								break;
							case SWT.RIGHT:
								destX=srcData.height-srcY-1;
								destY=srcX;
								width=srcData.height;
								height=srcData.width;
								break;
							case SWT.DOWN:
								destX=srcData.width-srcX-1;
								destY=srcData.height-srcY-1;
								width=srcData.width;
								height=srcData.height;
								break;
				}
				destIndex=(destY*destBytesPerLine)+(destX*bytesPerPixel);
				srcIndex=(srcY*srcData.bytesPerLine)+(srcX*bytesPerPixel);
				System.arraycopy(srcData.data, srcIndex, newData, destIndex, bytesPerPixel);
			}
		}
		setImageData(new ImageData(width,height,srcData.depth,srcData.palette,destBytesPerLine,newData));		
	}

		public void flip(boolean vertical){
		if (sourceImage == null)
			return;
		ImageData srcData=getImageData();
		int bytesPerPixel=srcData.bytesPerLine/srcData.width;
		int destBytesPerLine=srcData.width*bytesPerPixel;
		byte[] newData=new byte[srcData.data.length];
		for(int srcY=0;srcY<srcData.height;srcY++){
			for(int srcX=0;srcX<srcData.width;srcX++){
				int destX=0,destY=0,destIndex=0,srcIndex=0;
				if(vertical){
					destX=srcX;
					destY=srcData.height-srcY-1;
				}else{
					destX=srcData.width-srcX-1;
					destY=srcY;
				}
				destIndex=(destY*destBytesPerLine)+(destX*bytesPerPixel);
				srcIndex=(srcY*srcData.bytesPerLine)+(srcX*bytesPerPixel);
				System.arraycopy(srcData.data, srcIndex, newData, destIndex, bytesPerPixel);
				
			}
		}
			setImageData(new ImageData(srcData.width,srcData.height,srcData.depth,srcData.palette,
				destBytesPerLine,newData));
		
	}
public void transparence(){
	if(sourceImage==null)
		return;
	ImageData imData=getImageData();
	//RGB white=new RGB(255,200,255);
	ColorDialog colors=new ColorDialog(getShell());
	RGB color=colors.open();
	if(color==null)
		return;
	for(int i=0;i<imData.width;i++){
		for(int j=0;j<imData.height;j++){
			RGB rgb=imData.palette.getRGB(imData.getPixel(i, j));
			int treshold=200;
			if(rgb.red>treshold&&rgb.green>treshold&&rgb.blue>treshold){
				imData.setPixel(i, j, imData.palette.getPixel(color));
			}
		}
	}
	imData.transparentPixel=imData.palette.getPixel(new RGB(255,255,255));
	setImageData(imData);

}

public void oilFilter(){
	if(sourceImage==null)
		return;
	ImageData imData=getImageData();
	RGB white=new RGB(255,255,255);
	for(int i=0;i<imData.width;i++){
		for(int j=0;j<imData.height;j++){
			double x=i,y=j;
			RGB rgb=imData.palette.getRGB(imData.getPixel(i, j));
			double radius=Math.sqrt(i*i+j*j);
			double angle=Math.atan(i/j);
			x=radius*imData.width/6.2832;
			y=angle*imData.height/6.2832;
			imData.setPixel((int)x, (int)y, imData.palette.getPixel(rgb));
		}
	}
	setImageData(imData);
}
public void blurFilter(){
	if(sourceImage==null){
		MessageDialog.openInformation(getShell(), "Open Image", "You should load an image first!");
		return;}
	BufferedImage buff=Converter.convertToAWT(getImageData());
	buff=Converter.blur(buff);
	setImageData(Converter.convertToSWT(buff));
 }
public void sharpenFilter(){
	if(sourceImage==null){
		MessageDialog.openInformation(getShell(), "Open Image", "You should load an image first!");
		return;}
	BufferedImage buff=Converter.convertToAWT(getImageData());
	buff=Converter.sharpen(buff);
	setImageData(Converter.convertToSWT(buff));
}
public void edgeDetectFilter(){
	if(sourceImage==null){
		MessageDialog.openInformation(getShell(), "Open Image", "You should load an image first!");
		return;}
	BufferedImage buff=Converter.convertToAWT(getImageData());
	buff=Converter.edgeDetect(buff);
	setImageData(Converter.convertToSWT(buff));
}
public void grayScaleFilter(){
	if(sourceImage==null){
		MessageDialog.openInformation(getShell(), "Open Image", "You should load an image first!");
		return;}
	BufferedImage buff=Converter.convertToAWT(getImageData());
	buff=Converter.grayScale(buff);
	setImageData(Converter.convertToSWT(buff));
	
}
public void brightnessFilter(float range){
	if(sourceImage==null){
		MessageDialog.openInformation(getShell(), "Open Image", "You should load an image first!");
		return;}
	BufferedImage buff=Converter.convertToAWT(getImageData());
	buff=Converter.brightness(buff,range);
	setImageData(Converter.convertToSWT(buff));
	
}
public void embossFilter(){
	if(sourceImage==null){
		MessageDialog.openInformation(getShell(), "Open Image", "You should load an image first!");
		return;}
	BufferedImage buff=Converter.convertToAWT(getImageData());
	buff=Converter.emboss(buff);
	setImageData(Converter.convertToSWT(buff));
	
}

public void motionFilter(){
	if(sourceImage==null){
		MessageDialog.openInformation(getShell(), "Open Image", "You should load an image first!");
		return;}
	BufferedImage buff=Converter.convertToAWT(getImageData());
	buff=Converter.motion(buff);
	setImageData(Converter.convertToSWT(buff));
}

public void undo(){
	if((iterator==0)||(sourceImage==null))
		return;
	iterator=iterator-1;
	System.out.println("First"+iterator);
	ImageData imData=undoList.get(iterator--);
	setImageData(imData);
	System.out.println(iterator);
}
public void negativeFilter(){
	if(sourceImage==null){
		MessageDialog.openInformation(getShell(), "Open Image", "You should load an image first!");
		return;}
	BufferedImage buff=Converter.convertToAWT(getImageData());
	buff=Converter.negative(buff);
	setImageData(Converter.convertToSWT(buff));

}
public void highPass(){
	if(sourceImage==null){
		MessageDialog.openInformation(getShell(), "Open Image", "You should load an image first!");
		return;}
	BufferedImage buff=Converter.convertToAWT(getImageData());
	buff=Converter.highPass(buff);
	setImageData(Converter.convertToSWT(buff));

	
}
public void prewitt(){
	if(sourceImage==null){
		MessageDialog.openInformation(getShell(), "Open Image", "You should load an image first!");
		return;}
	BufferedImage buff=Converter.convertToAWT(getImageData());
	buff=Converter.Prewitt(buff);
	setImageData(Converter.convertToSWT(buff));


}
public static boolean isDirty(){
	return dirty;
}
public static void setDirty(){
	dirty=false;
}
public static ImageData getImDataDialog(){
	return imDataDialog;
}

}