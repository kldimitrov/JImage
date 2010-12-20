package jimageviewer;

import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.ConvolveOp;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.Kernel;
import java.awt.image.LookupOp;
import java.awt.image.RescaleOp;
import java.awt.image.ShortLookupTable;
import java.awt.image.WritableRaster;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;

public class Converter {

	public static BufferedImage convertToAWT(ImageData data){
		ColorModel colorModel=null;
		PaletteData palette=data.palette;
		if(palette.isDirect){
			colorModel=new DirectColorModel(data.depth,palette.redMask,
					palette.greenMask,palette.blueMask);
			BufferedImage bufferedImage=new BufferedImage(colorModel,colorModel
					.createCompatibleWritableRaster(data.width, data.height),false,null);
			WritableRaster raster=bufferedImage.getRaster();
			int[] pixelArray=new int[3];
			for(int y=0;y<data.height;y++){
				for(int x=0;x<data.width;x++){
					int pixel=data.getPixel(x, y);
					RGB rgb=palette.getRGB(pixel);
					pixelArray[0]=rgb.red;
					pixelArray[1]=rgb.green;
					pixelArray[2]=rgb.blue;
					raster.setPixels(x, y, 1,1,pixelArray);
				}
			}
			return bufferedImage;
		}else{
			RGB[] rgbs=palette.getRGBs();
			byte[] red=new byte[rgbs.length];
			byte[] green=new byte[rgbs.length];
			byte[] blue=new byte[rgbs.length];
			for(int i=0;i<rgbs.length;i++){
				RGB rgb=rgbs[i];
				red[i]=(byte)rgb.red;
				green[i]=(byte)rgb.green;
				blue[i]=(byte)rgb.blue;
				}
			if(data.transparentPixel!=-1){
				colorModel=new IndexColorModel(data.depth,rgbs.length,red,green,
						blue,data.transparentPixel);
			}else{
				colorModel=new IndexColorModel(data.depth,rgbs.length,red,green,blue);
			}
			BufferedImage bufferedImage=new BufferedImage(colorModel,colorModel.createCompatibleWritableRaster(data.width, data.height),false,null);
			WritableRaster raster=bufferedImage.getRaster();
			int[] pixelArray=new int[1];
			for(int y=0; y<data.height;y++){
				for(int x=0; x<data.width;x++){
					int pixel=data.getPixel(x, y);
					pixelArray[0]=pixel;
					raster.setPixel(x, y, pixelArray);
				}
			}
			return bufferedImage;
		}
	}
	public static ImageData convertToSWT(BufferedImage bufferedImage){
		if(bufferedImage.getColorModel() instanceof DirectColorModel){
			DirectColorModel colorModel=(DirectColorModel) bufferedImage.getColorModel();
			PaletteData palette = new PaletteData(colorModel.getRedMask(), colorModel.getGreenMask(),
					colorModel.getBlueMask());
			ImageData data=new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(),
					colorModel.getPixelSize(),palette);
			WritableRaster raster=bufferedImage.getRaster();
			int[] pixelArray=new int[3];
			for(int y=0;y<data.height;y++){
				for(int x=0;x<data.width;x++){
					//ob1rni vnimanie
					raster.getPixel(x, y, pixelArray);
					int pixel=palette.getPixel(new RGB(pixelArray[0],pixelArray[1],pixelArray[2]));
					data.setPixel(x, y, pixel);
				}
			}
			return data;
		}else if(bufferedImage.getColorModel() instanceof IndexColorModel){
			IndexColorModel colorModel=(IndexColorModel)bufferedImage.getColorModel();
			int size=colorModel.getMapSize();
			byte[] reds=new byte[size];
			byte[] greens=new byte[size];
			byte[] blues=new byte[size];
			colorModel.getReds(reds);
			colorModel.getGreens(greens);
			colorModel.getBlues(blues);
			RGB[] rgbs=new RGB[size];
			for(int i=0; i<rgbs.length;i++){
				rgbs[i]=new RGB(reds[i]&0xFF, greens[i]&0xFF,blues[i]&0xFF);
			}
			PaletteData palette=new PaletteData(rgbs);
			ImageData data=new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(),
					colorModel.getPixelSize(),palette);
			data.transparentPixel=colorModel.getTransparentPixel();
			WritableRaster raster=bufferedImage.getRaster();
			int[] pixelArray=new int[1];
			for(int y=0;y<data.height;y++){
				for(int x=0;x<data.width;x++){
					raster.getPixel(x, y, pixelArray);
					data.setPixel(x, y, pixelArray[0]);
				}
			}
			return data;
		}
		return null;
	}
	public static BufferedImage blur(BufferedImage bufferedImage){
		Kernel kernel=new Kernel(3,3,
				new float[]{
				1f/9f, 1f/9f, 1f/9f,
				1f/9f, 1f/9f, 1f/9f,
				1f/9f, 1f/9f, 1f/9f,
		});
		BufferedImageOp op=new ConvolveOp(kernel);
		bufferedImage=op.filter(bufferedImage, null);
		return bufferedImage;
	}
	public static BufferedImage sharpen(BufferedImage bufferedImage){
		Kernel kernel=new Kernel(3,3,
				new float[]{
				-1.0f,-1.0f,-1.0f,
				-1.0f, 9.0f, -1.0f,
				-1.0f, -1.0f, -1.0f});
		BufferedImageOp op=new ConvolveOp(kernel);
		bufferedImage=op.filter(bufferedImage, null);
		return bufferedImage;
	}
	public static BufferedImage edgeDetect(BufferedImage bufferedImage	){
		Kernel kernel=new Kernel(3,3,
				new float[]{
				-1.0f,-1.0f,-1.0f,
				-1.0f,8.0f,-1.0f,
				-1.0f,-1.0f,-1.0f});
		ConvolveOp op=new ConvolveOp(kernel);
		bufferedImage=op.filter(bufferedImage, null);
		return bufferedImage;
		}
	static BufferedImage grayScale(BufferedImage bufferedImage){
		ColorConvertOp colorConvert=new ColorConvertOp(ColorSpace
				.getInstance(ColorSpace.CS_GRAY),null);
		colorConvert.filter(bufferedImage, bufferedImage);
		return bufferedImage;
	}
	static BufferedImage brightness(BufferedImage bufferedImage, float range){
		RescaleOp colorConvert=new RescaleOp(range,0,null);
		colorConvert.filter(bufferedImage, bufferedImage);
		return bufferedImage;
	}
	public static BufferedImage emboss(BufferedImage bufferedImage	){
		Kernel kernel=new Kernel(3,3,
				new float[]{
				-2f,0f,0f,
				0f,1f,0f,
				0f,0f,2f});
		ConvolveOp op=new ConvolveOp(kernel);
		bufferedImage=op.filter(bufferedImage, null);
		return bufferedImage;
		}
	static BufferedImage highPass(BufferedImage bufferedImage	){
		Kernel kernel=new Kernel(3,3,
				new float[]{
				-1f,-1f,-1f,
				-1f,8f,-1f,
				-1f,-1f,-1f});
		ConvolveOp op=new ConvolveOp(kernel);
		bufferedImage=op.filter(bufferedImage, null);
		return bufferedImage;
		}

	static BufferedImage motion(BufferedImage bufferedImage	){
		Kernel kernel=new Kernel(3,3,
				new float[]{
				0f,0f,0f,
				0f,1f,0f,
				0f,0f,-1f});
		ConvolveOp op=new ConvolveOp(kernel);
		bufferedImage=op.filter(bufferedImage, null);
		return bufferedImage;
		}

	static BufferedImage Prewitt(BufferedImage bufferedImage	){
		Kernel kernel=new Kernel(3,3,
				new float[]{
				-1f,-1f,-1f,
				0f,0f,0f,
				1f,1f,1f});
		ConvolveOp op=new ConvolveOp(kernel);
		bufferedImage=op.filter(bufferedImage, null);
		return bufferedImage;
		}

	static BufferedImage negative(BufferedImage bufferedImage){
		short[] invert=new short[256];
	
		for(int i=0;i<256;i++)
			invert[i]=(short)(255-i);
		BufferedImageOp invertOp=new LookupOp(
				new ShortLookupTable(0,invert),null);
		bufferedImage=invertOp.filter(bufferedImage, null);
		return bufferedImage;
	}

}

