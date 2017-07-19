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
	 * 
