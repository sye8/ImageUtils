import java.io.IOException;


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
 * Command line tool for image format conversion, compression and resizing
 * 
 * @author yesifan
 *
 */
public class Main {

	/**
	 * Looks up argument
	 * 
	 * @param arr The argument array
	 * @param s The argument to look for
	 * @return The index of the argument or -1 if not found
	 */
    private static int argsLookup(String[] arr, String s){
		for(int i = 0; i < arr.length; i++){
			if(arr[i].equals(s)){
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Main method
	 * 
	 * Commands:
	 * 
	 * -supportedTypes: Show supported image types
	 * 
	 * -scale [inPath] [scale] [outPath] scale and convert and image
	 * 
	 * -width&height [inPath] [width] [height] [outPath] convert image and resize into required height & width
	 * 
	 * -maintainRatio [inPath] [width] [outPath] resize and convert image by entering width while aspect ratio is maintained
	 * 
	 * -compress [inPath] [size in bytes] [outPath] compress image to maximum size under the required size in bytes
	 * Note: Compress doesn't convert the image
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if(argsLookup(args, "-supportedTypes") != -1){
			ImageUtils.getSupoortedTypes();
			System.exit(0);
		}
		//Scale and convert
		int index = argsLookup(args, "-scale");
		if(index != -1){
			try {
				ImageUtils.resizeAndConvertImage(args[index+1], Double.parseDouble(args[index+2]), args[index+3]);
			} catch (NumberFormatException e) {
				System.out.println("Illegal scale");
			} catch (IllegalArgumentException e) {
				System.out.println("Output Type not supported");
			} catch (IOException e) {
				System.out.println("Input or output path not found");
			}finally{
				System.exit(0);
			}
		}
		//Change size and convert
		index = argsLookup(args, "-width&height");
		if(index != -1){
			try {
				ImageUtils.resizeAndConvertImage(args[index+1], Integer.parseInt(args[index+2]), Integer.parseInt(args[index+3]), args[index+4]);
			} catch (NumberFormatException e) {
				System.out.println("Illegal width or height");
			} catch (IllegalArgumentException e) {
				System.out.println("Output Type not supported");
			} catch (IOException e) {
				System.out.println("Input or output path not found");
			}finally{
				System.exit(0);
			}
		}
		//Maintain aspect ratio
		index = argsLookup(args, "-maintainRatio");
		if(index != -1){
			try {
				ImageUtils.resizeAndConvertImageMaintainAspectRatio(args[index+1], Integer.parseInt(args[index+2]), args[index+3]);
			} catch (NumberFormatException e) {
				System.out.println("Illegal width");
			} catch (IllegalArgumentException e) {
				System.out.println("Output Type not supported");
			} catch (IOException e) {
				System.out.println("Input or output path not found");
			}finally{
				System.exit(0);
			}
		}
		//Image Compression
		index = argsLookup(args, "-compress");
		if(index != -1){
			try {
				ImageUtils.imageCompression(args[index+1], Integer.parseInt(args[index+2]), args[index+3]);
			} catch (NumberFormatException e) {
				System.out.println("Illegal size");
			} catch (IllegalArgumentException e) {
				System.out.println("Output format must be the same as input format");
			} catch (IllegalStateException e) {
				System.out.println("Image writer for input image format not found");
			} catch (IOException e) {
				System.out.println("Input or output path not found");
			}finally{
				System.exit(0);
			}
		}
	}

}
