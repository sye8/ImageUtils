import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
 * Static methods for loading, converting and scaling images. 
 * With JRE System Library, supported formats are below: 
 * Supported read formats: [jpg, bmp, gif, png, wbmp, jpeg]. 
 * Supported write formats: [jpg, bmp, gif, png, wbmp, jpeg]. 
 * 
 * For extended support: https://github.com/haraldk/TwelveMonkeys 
 * 
 * For camera raw image conversion to JPG, use dcraw. 
 * https://www.cybercom.net/~dcoffin/dcraw/ 
 * dcraw comes in C source code. 
 * Compile with "gcc -o dcraw -O4 dcraw.c -lm -ljasper -ljpeg -llcms2" or "gcc -o dcraw -O4 dcraw.c -lm -DNODEPS". 
 * Please make sure the complied file has at least execute permission. 
 * If not, please give permission to execute in terminal with chmod 111 [compiled unit name]. 
 * If you are using this java source code, please place complied dcraw in the same directory as the complied class. 
 * If you are using eclipse, please place complied dcraw under project directory. 
 * If you are using the jar through terminal, please place complied dcraw in the working directory of your shell.
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
	 * @throws IOException 
	 */
	public static BufferedImage loadImage(String filename) throws IOException{
		return ImageIO.read(new File(filename));
	}
	
	/**
	 * Resize and convert an image based on scale needed and output path
	 * 
	 * @param inPath The original image path
	 * @param scale Desired scale
	 * @param outPath The output path. Image format will be extracted from this
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public static void scaleAndConvertImage(String inPath, double scale, String outPath) throws IOException, InterruptedException{
		//Check if supported
		int typeSupport = checkIfReadTypeIsSupported(inPath.substring(inPath.indexOf('.')+1).toLowerCase());
		switch(typeSupport){
			case 0:
				break;
			case 1:
				//TryDCRaw
				tryDCRaw(inPath, 1);
				return;
			case 2:
				//TryDCRaw
				tryDCRaw(inPath, 2);
				return;
		}
		//Getting the input image
		BufferedImage input = loadImage(inPath);	
		//If file not found
		if(input == null){
			System.out.println("Failed to load image");
			return;
		}		
		//Calculate width and height
		int outWidth = (int)(input.getWidth() * Math.sqrt(scale));
		int outHeight = (int)(input.getHeight() * Math.sqrt(scale));
		
		//Creating the output image
		resizeAndConvertImage(input, outWidth, outHeight, outPath);
	}
	
	/**
	 * Resizes and convert image based on width, height and specification to keep aspect ratio or not. 
	 * If aspect ratio is to be kept, then the image will be resized to best fill the space given, while keeping aspect ratio
	 * 
	 * @param inPath The original image path
	 * @param width Output width
	 * @param height Output height
	 * @param maintainAspectRatio If the user want the output to keep the input aspect ratio
	 * @param outPath The output path. Image format will be extracted from this
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public static void resizeAndConvertImage(String inPath, int width, int height, boolean maintainAspectRatio, String outPath) throws IOException, InterruptedException{
		//Check if supported
		int typeSupport = checkIfReadTypeIsSupported(inPath.substring(inPath.indexOf('.')+1).toLowerCase());
		switch(typeSupport){
			case 0:
				break;
			case 1:
				//TryDCRaw
				tryDCRaw(inPath, 1);
				return;
			case 2:
				//TryDCRaw
				tryDCRaw(inPath, 2);
				return;
		}
		//Getting the input image
		BufferedImage input = loadImage(inPath);		
		//If file not found
		if(input == null){
			System.out.println("Failed to load image");
			return;
		}
		
		//Calculate height and width if user ask to maintain aspect ratio
		if(maintainAspectRatio){
			int inputWidth = input.getWidth();
			int inputHeight = input.getHeight();
			double inAspectRatio = (double)(inputWidth)/(double)(inputHeight);
			if(inAspectRatio != (double)(width)/(double)(height)){
				if(inputWidth > inputHeight){
					height = (int)(width / inAspectRatio);
				}else if(inputWidth < inputHeight){
					width = (int)(height * inAspectRatio);
				}else{
					if(width > height){
						width = height;
					}else if(width < height){
						height  = width;
					}
				}
			}
		}

		//Creating the output image
		resizeAndConvertImage(input, width, height, outPath);
	}
	
	/**
	 * Method that creates an output image with an input image and specified height & width
	 * 
	 * @param input The input image
	 * @param height The output image height
	 * @param width The output image width
	 * @param outPath The output path
	 * @throws IOException 
	 */
	private static void resizeAndConvertImage(BufferedImage input, int width, int height, String outPath) throws IOException{
		BufferedImage output;
		if(input.getType() == 0 || outPath.substring(outPath.indexOf('.')+1).toLowerCase().equals("jpg")){
			output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		}else{
			output = new BufferedImage(width, height, input.getType());
		}
		Graphics2D g2d = output.createGraphics();
		g2d.drawImage(input, 0, 0, width, height, null);
	    g2d.dispose();
	    
	    //Writing the output file
	    if(ImageIO.write(output, outPath.substring(outPath.indexOf('.')+1).toLowerCase(), new File(outPath))){
	    	System.out.println("Conversion Complete!");
	    }else{
	    	System.out.println("Output type not supported");
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
		if(!(inPath.substring(inPath.indexOf('.')+1).toLowerCase().equals(outPath.substring(outPath.indexOf('.')+1).toLowerCase()))){
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
		Iterator<ImageWriter> iterator = ImageIO.getImageWritersByFormatName(outPath.substring(outPath.indexOf('.')+1).toLowerCase());
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
	 * Tries to use DCRaw (a raw decoder written in C, complied into terminal tool) to convert to JPG
	 * Saves results at the same directory with name.thumb.jpg
	 * 
	 * @param inPath The path of the input image
	 * @param systemType The type of the system, requires for command line execution
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	private static void tryDCRaw(String inPath, int systemType) throws InterruptedException, IOException{
		System.out.println("Trying DCRaw conversion to JPG");
		System.out.println("Note: The ouput image will not be resized.");
		List<String> commands = new ArrayList<String>();
		File file;
		if(systemType == 1){
			file = new File("dcraw.exe");
		}else{
			file = new File("dcraw");
		}
		commands.add(file.getAbsolutePath());
		commands.add("-e");
		commands.add(inPath);
		ProcessBuilder pb = new ProcessBuilder(commands);
	    pb.redirectErrorStream(true);
	    Process process;
		try {
			process = pb.start();
		} catch (IOException e) {
			System.out.println("DCRaw not found or cannot execute");
			e.printStackTrace();
			return;
		}
	    
	    //Read output
        StringBuilder out = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = null, previous = null;
        while ((line = br.readLine()) != null)
            if (!line.equals(previous)) {
                previous = line;
                out.append(line).append('\n');
                System.out.println(line);
            }

        //Check result
        if (process.waitFor() == 0) {
            System.out.println("Conversion Success");
            System.out.println("Result saved at: " + inPath.substring(0, inPath.indexOf('.')) + ".thumb.jpg");
            return;
        }

        //Abnormal termination: Log command parameters and output and throw ExecutionException
        System.out.println("Error occured");
        System.err.println(commands);
        System.err.println(out.toString());
        return;
	}
	
	/**
	 * 
	 * @return Set of all informal format names understood by the current set of registered readers
	 */
	private static Set<String> readSupport(){
		Set<String> set = new HashSet<String>();

		String[] formatNames = ImageIO.getReaderFormatNames();

		for (int i = 0; i < formatNames.length; i++) {
			set.add(formatNames[i].toLowerCase());
		}
		
		return set;
	}
	
	/**
	 * 
	 * @return Set of all informal format names understood by the current set of registered writers
	 */
	private static Set<String> writeSupport(){
		Set<String> set = new HashSet<String>();

		String[] formatNames = ImageIO.getWriterFormatNames();

		for (int i = 0; i < formatNames.length; i++) {
			set.add(formatNames[i].toLowerCase());
		}
		
		return set;
	}
	
	/**
	 * Checks if system is Windows
	 * 
	 * @return True if system is Windows
	 */
	private static boolean osCheck(){
		String os = System.getProperty("os.name");
		System.out.println("Your OS is: " + os);
		if(os.toLowerCase().startsWith("windows")){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Check if read type is supported.
	 * 
	 * @param type The type of the image
	 * @return 0 if current readers support the format; 1 if readers doesn't support and system is windows; 2 if readers doesn't support and system is not Windows. Can try dcraw.
	 */
	private static int checkIfReadTypeIsSupported(String type){
		if(readSupport().contains(type)){
			return 0;
		}else{
			if(osCheck()){
				return 1;
			}else{		
				return 2;
			}
		}		
	}
	
	/**
	 * Prints supported file types for ImageIO readers and writers and system info
	 * 
	 */
	public static void getSupoortedTypes(){
		
		//Read type support
		Set<String> set = readSupport();
		System.out.println("Supported read formats: " + set);
		set.clear();

		// Write Type Support
		set = writeSupport();
		System.out.println("Supported write formats: " + set);
		set.clear();
		
		osCheck();
	}
	
}
