# ImageUtils

[![License](https://img.shields.io/badge/license-MIT%20License-blue.svg)](LICENSE)

Java class for image format conversion, compression and resizing, includes a command line tool

Only requires JRE (Java SE 1.8) System Library, no other dependencies

- Supported read formats: [jpg, bmp, gif, png, wbmp, jpeg]

- Supported write formats: [jpg, bmp, gif, png, wbmp, jpeg]

Note: Compress doesn't convert the image

Test Note: From tests so far, compression seems to support jpg only. Feedback welcomed

## Extended Support

For extended image format support, try [TwelveMonkeys ImageIO](https://github.com/haraldk/TwelveMonkeys)

With extended support, supported formats are:

- Supported read formats: [psd, cur, tiff, bmp, gif, rgbe, tga, tif, wmf, ico, sgi, pgm, dcx, wbmp, jpeg, pam, pict, jpg, pct, pcx, svg, png, iff, ppm, pnm, thumbs db, icns, targa, pfm, hdr, pbm, thumbs]

- Supported write formats: [tiff, bmp, gif, tif, pgm, wbmp, jpeg, pam, pict, jpg, pct, iff, png, ppm, pnm, pbm]


## Main class command line tool (jar includes extended support):

	 * Commands:
	 * 
	 * -supportedTypes: Show supported image types
	 * 
	 * -scale [inPath] [scale] [outPath] scale and convert and image
	 * 
	 * -resize [inPath] [width] [height] [maintain aspect ratio (true/false)] [outPath] resize and convert the image
	 * If user chooses to keep aspect ratio, then the image will be resized to best fill the space given, while keeping aspect ratio
	 * 
	 * -convert [inPath] [outPath] converts the image
	 * 
	 * -compress [inPath] [size in bytes] [outPath] compress image to maximum size under the required size in bytes
	 * 
