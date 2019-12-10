# BitTwiddler

This is just a simple [bit-twiddler](https://en.wikipedia.org/wiki/Bit_twiddler)
that I wrote to familiarize myself with JavaFX.  At least _slightly_ more useful
than your average "Hello World" application, this tiny app just does a few types
of type conversions and bit manipulations.

In short, the program allows you to input a byte buffer (or a number) and see
what those bytes would mean converted into a number of different primitive 
formats.  The following are allowed as input:

- A string, in UTF-8 format.
- A string, in UTF-16 (little-endian) format.
- Directly entered hexadecimal bytes (i.e., pasted from a hex dump)
- Directly entered binary bytes.

The following will be displayed, updated in real time as the input changes:

- A (vector of) n-bit integers (signed and unsigned), where n is 8, 16, 32, or 64.
- A (vector of) 32 and 64-bit floating point values.
- For each of the above, you can select whether the source bytes are interpreted 
  as big- or little-endian.
- As a string (parsed as UTF-8, UTF-16 LE, and ASCII).
- As hex/binary data.


## License:

Permission to use, copy, modify, and/or distribute this software for any purpose 
with or without fee is hereby granted.