# ImageUtils
Java class for image format conversion, compression and resizing, includes a command line tool

Only requires JRE System Library, no other dependencies

-Supported read formats: [jpg, bmp, gif, png, wbmp, jpeg]

-Supported write formats: [jpg, bmp, gif, png, wbmp, jpeg]

## Main class command line tool:

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
