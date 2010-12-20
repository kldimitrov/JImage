package jimageviewer;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public class SWT2Dutil {
	public static Rectangle transformRect(AffineTransform af, Rectangle src){
		Rectangle dest=new Rectangle(0,0,0,0);
		src=absRect(src);
		Point p1=new Point(src.x,src.y);
		p1=transformPoint(af,p1);
		dest.x=p1.x; dest.y=p1.y;
		dest.width=(int)(src.width/af.getScaleX());
		dest.height=(int)(src.height/af.getScaleY());
		return dest;
	}
	public static Rectangle inverseTransformRect(AffineTransform af, Rectangle src){
		Rectangle dest=new Rectangle(0,0,0,0);
		src=absRect(src);
		Point p1=new Point(src.x,src.y);
		p1=inverseTransformPoint(af, p1);
		dest.x=p1.x;dest.y=p1.y;
		dest.width=(int)(src.width/af.getScaleX());
		dest.height=(int)(src.height/af.getScaleY());
		return dest;
	}
	public static Point transformPoint(AffineTransform af,Point pt){
		Point2D src=new Point2D.Float(pt.x,pt.y);
		Point2D dest=af.transform(src,null);
		Point point=new Point((int)Math.floor(dest.getX()),(int)Math.floor(dest.getY()));
		return point;
	}
	public static Point inverseTransformPoint(AffineTransform af, Point pt){
		Point2D src=new Point2D.Float(pt.x,pt.y);
		try{
			Point2D dest=af.inverseTransform(src, null);
			return new Point((int)Math.floor(dest.getX()), (int)Math.floor(dest.getY()));
		}catch(Exception e){
			e.printStackTrace();
			return new Point(0,0);
		}
	}
	public static Rectangle absRect(Rectangle src){
		Rectangle dest=new Rectangle(0,0,0,0);
		if(src.width<0){dest.x=src.x+src.width+1;dest.width=-src.width;}
		else{dest.x=src.x; dest.width=src.width;}
		if(src.height<0){dest.y=src.y+src.height+1; dest.height=-src.height;}
		else{dest.y=src.y; dest.height=src.height;}
		return dest;
		}
	
}
