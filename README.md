# ImageUtils

[![License](https://img.shields.io/badge/license-MIT%20License-blue.svg)](LICENSE)

Java class for image format conversion, compression and resizing, includes a command line tool

Only requires JRE (Java SE 1.8) System Library, provides support for formats below

- Supported read formats: [jpg, bmp, gif, png, wbmp, jpeg]

- Supported write formats: [jpg, bmp, gif, png, wbmp, jpeg]

Note: Compress doesn't convert the image

Test Note: From tests so far, compression seems to support jpg only. Feedback welcomed

## Extended Support

For extended image format support, try [TwelveMonkeys ImageIO](https://github.com/haraldk/TwelveMonkeys)

With extended support, supported formats are:

- Supported read formats: [psd, cur, tiff, bmp, gif, rgbe, tga, tif, wmf, ico, sgi, pgm, dcx, wbmp, jpeg, pam, pict, jpg, pct, pcx, svg, png, iff, ppm, pnm, thumbs db, icns, targa, pfm, hdr, pbm, thumbs]

- Supported write formats: [tiff, bmp, gif, tif, pgm, wbmp, jpeg, pam, pict, jpg, pct, iff, png, ppm, pnm, pbm]

## Raw Support

For camera raw image conversion to JPG, use [dcraw](https://www.cybercom.net/~dcoffin/dcraw/). 

So far only UNIX support is included in this java project. 

dcraw comes in C source code. 

Compile with `gcc -o dcraw -O4 dcraw.c -lm -ljasper -ljpeg -llcms2` or `gcc -o dcraw -O4 dcraw.c -lm -DNODEPS`. 

Please make sure the complied file has at least execute permission. 

If not, please give permission to execute in terminal with `chmod 111 [compiled unit name]`. 

If you are using this java source code, please place complied dcraw in the same directory as the complied class. 

If you are using eclipse, please place complied dcraw under project directory. 

If you are using the jar through terminal, please place complied dcraw in the working directory of your shell.

## Main class command line tool (jar includes extended support) commands:

* `-supportedTypes`: Show supported image types

* `-scale [inPath] [scale] [outPath]` scale and convert and image

* `-resize [inPath] [width] [height] [maintain aspect ratio (true/false)] [outPath]` resize and convert the image

  If user chooses to keep aspect ratio, then the image will be resized to best fill the space given, while keeping aspect ratio

* `-convert [inPath] [outPath]` converts the image

* `-compress [inPath] [size in bytes] [outPath]` compress image to maximum size under the required size in bytes
