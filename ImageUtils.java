import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

/*
 * MIT License
 * 
 * Copyright (c) 2017 Sifan YE
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

/**
 * Static methods for loading, converting and scaling images
 * Only requires JRE System Library, no other dependencies.
 * Supported read formats: [jpg, bmp, gif, png, wbmp, jpeg]
 * Supported write formats: [jpg, bmp, gif, png, wbmp, jpeg]
 * 
 * @author Sifan Ye
 *
 */

public class ImageUtils {
	
	/**
	 * Load and return a BufferedImage object with filename/path
	 * Includes try/catch
	 * 
	 * @param Filename
	 * @return A loaded BufferedImage
	 */
	public static BufferedImage loadImage(String filename){
		try{
			return ImageIO.read(new File(filename));
		}catch (IOException e) {
			System.out.println("Image Not Found");
			return null;
		}
	}
	
	/**
	 * Resize and convert an image based on scale needed and output path
	 * 
	 * @param inPath The original image path
	 * @param scale Desired scale
	 * @param outPath The output path. Image format will be extracted from this
	 * @throws IOException
	 */
	public static void resizeAndConvertImage(String inPath, double scale, String outPath) throws IOException, IllegalArgumentException{
		//Getting the input image
		BufferedImage input = loadImage(inPath);		
		//If file not found
		if(input == null){
			throw new IOException("Input image not found");
		}		
		int outWidth = (int)(input.getWidth() * Math.sqrt(scale));
		int outHeight = (int)(input.getHeight() * Math.sqrt(scale));
		
		//Creating the output image
		BufferedImage output = new BufferedImage(outWidth, outHeight, input.getType());
		Graphics2D g2d = output.createGraphics();
		g2d.drawImage(input, 0, 0, outWidth, outHeight, null);
	    g2d.dispose();
	    
	    //Writing the output file
	    if(ImageIO.write(output, outPath.substring(outPath.indexOf('.')+1), new File(outPath))){
	    	System.out.println("Conversion Complete!");
	    }else{
	    	throw new IllegalArgumentException("Output type not supported");
	    }    
	}
	
	/**
	 * Resize and convert an image based on width and height needed and output path
	 * 
	 * @param inPath The original image path
	 * @param width Output width
	 * @param height Output height
	 * @param outPath The output path. Image format will be extracted from this
	 * @throws IOException
	 */
	public static void resizeAndConvertImage(String inPath, int width, int height, String outPath) throws IOException, IllegalArgumentException{
		//Getting the input image
		BufferedImage input = loadImage(inPath);		
		//If file not found
		if(input == null){
			throw new IOException("Input image not found");
		}		
		
		//Creating the output image
		BufferedImage output = new BufferedImage(width, height, input.getType());
		Graphics2D g2d = output.createGraphics();
		g2d.drawImage(input, 0, 0, width, height, null);
	    g2d.dispose();
	    
	    //Writing the output file
	    if(ImageIO.write(output, outPath.substring(outPath.indexOf('.')+1), new File(outPath))){
	    	System.out.println("Conversion Complete!");
	    }else{
	    	throw new IllegalArgumentException("Output type not supported");
	    }    
	}
	
	/**
	 * Resize and convert an image based on width needed, original aspect ratio and output path
	 * 
	 * @param inPath The original image path
	 * @param width Output width
	 * @param outPath The output path. Image format will be extracted from this
	 * @throws IOException
	 */
	public static void resizeAndConvertImageMaintainAspectRatio(String inPath, int width, String outPath) throws IOException{
		//Getting the input image
		BufferedImage input = loadImage(inPath);		
		//If file not found
		if(input == null){
			throw new IOException("Input image not found");
		}		
		double aspectRatio = (double)input.getWidth()/(double)input.getHeight();
		int outHeight = (int)(width/aspectRatio);
		
		//Creating the output image
		BufferedImage output = new BufferedImage(width, outHeight, input.getType());
		Graphics2D g2d = output.createGraphics();
		g2d.drawImage(input, 0, 0, width, outHeight, null);
	    g2d.dispose();
	    
	    //Writing the output file
	    if(ImageIO.write(output, outPath.substring(outPath.indexOf('.')+1), new File(outPath))){
	    	System.out.println("Conversion Complete!");
	    }else{
	    	throw new IllegalArgumentException("Output type not supported");
	    }    
	}
	
	/**
	 * Compresses the image to the biggest size lower than the specified limit
	 * 
	 * @param inPath The original image path
	 * @param size The desired size, in bytes
	 * @param outPath The output path. Note: Only same type compression is supported
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static void imageCompression(String inPath, int size, String outPath) throws IllegalStateException, IOException{
		//If the output format is not the same as input format
		if(!(inPath.substring(inPath.indexOf('.')+1).equals(outPath.substring(outPath.indexOf('.')+1)))){
			System.out.println("Output format must be the same as input format");
			return;
		}
		//Check if required compression size if smaller than original
		File in = new File(inPath);
		if(in.length() < size){
			System.out.println("Compression will not be performed since original file size is smaller than specified compression size");
			return;
		}
		//Load input image
		BufferedImage input = ImageIO.read(in);
		
		//Get suitable Image Writer
		Iterator<ImageWriter> iterator = ImageIO.getImageWritersByFormatName(outPath.substring(outPath.indexOf('.')+1));
		if(!iterator.hasNext()){
			throw new IllegalStateException("Writers Not Found");
		}
		ImageWriter imageWriter = iterator.next();
		
		float quality = 0.975f;
		byte[] byteData;
		ImageOutputStream outputStream;
		
		//Try compression
		do{
			//Set parameters
			ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();
			imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			imageWriteParam.setCompressionQuality(quality);
			
			//Output
			ByteArrayOutputStream compressed = new ByteArrayOutputStream();
			outputStream = ImageIO.createImageOutputStream(compressed);
			imageWriter.setOutput(outputStream);
			imageWriter.write(null, new IIOImage(input, null, null), imageWriteParam);
			byteData = compressed.toByteArray();
			quality -= 0.025f;
			if(quality < 0){
				System.out.println("Sorry, cannot compress image under the size specified. Please choose a bigger size");
				 outputStream.close();
				 imageWriter.dispose();
				return;
			}
		}while(byteData.length > size);
		
		//Generate output file
		OutputStream out = new BufferedOutputStream(new FileOutputStream(outPath));
		out.write(byteData);
		
	    //Closing everything
	    outputStream.close();
	    imageWriter.dispose();
	    out.close();
	    System.out.println("Compression Complete");
	}
	
	/**
	 * Prints supported file types for ImageIO readers and writers
	 * 
	 */
	public static void getSupoortedTypes(){
		Set<String> set = new HashSet<String>();

		// Get list of all informal format names understood by the current set of registered readers
		String[] formatNames = ImageIO.getReaderFormatNames();

		for (int i = 0; i < formatNames.length; i++) {
			set.add(formatNames[i].toLowerCase());
		}
		System.out.println("Supported read formats: " + set);

		set.clear();

		// Get list of all informal format names understood by the current set of registered writers
		formatNames = ImageIO.getWriterFormatNames();

		for (int i = 0; i < formatNames.length; i++) {
			set.add(formatNames[i].toLowerCase());
		}
		System.out.println("Supported write formats: " + set);

		set.clear();
	}
	

	
}
