package twiddler;

import java.nio.ByteBuffer;

public class HexBufferParser implements BufferParser {
  @Override
  public ByteBuffer parse(String input) {
    ByteBuffer buf = ByteBuffer.wrap(new byte[input.length() / 2]);
    char[] chars = input.toCharArray();
    byte current_byte = 0;
    int nibble = 0;
    for (int i = 0; i < chars.length; i++) {
      char c = chars[i];
      switch(c) {
        // === Hex Digits ===
        case '0':
          current_byte |= staggerNibbles((byte) 0,nibble);
          break;
        case '1':
          current_byte |= staggerNibbles((byte) 1,nibble);
          break;
        case '2':
          current_byte |= staggerNibbles((byte) 2,nibble);
          break;
        case '3':
          current_byte |= staggerNibbles((byte) 3,nibble);
          break;
        case '4':
          current_byte |= staggerNibbles((byte) 4,nibble);
          break;
        case '5':
          current_byte |= staggerNibbles((byte) 5,nibble);
          break;
        case '6':
          current_byte |= staggerNibbles((byte) 6,nibble);
          break;
        case '7':
          current_byte |= staggerNibbles((byte) 7,nibble);
          break;
        case '8':
          current_byte |= staggerNibbles((byte) 8,nibble);
          break;
        case '9':
          current_byte |= staggerNibbles((byte) 9,nibble);
          break;
        case 'a':
        case 'A':
          current_byte |= staggerNibbles((byte) 0xA,nibble);
          break;
        case 'b':
        case 'B':
          current_byte |= staggerNibbles((byte) 0xB,nibble);
          break;
        case 'c':
        case 'C':
          current_byte |= staggerNibbles((byte) 0xC,nibble);
          break;
        case 'd':
        case 'D':
          current_byte |= staggerNibbles((byte) 0xD,nibble);
          break;
        case 'e':
        case 'E':
          current_byte |= staggerNibbles((byte) 0xE,nibble);
          break;
        case 'f':
        case 'F':
          current_byte |= staggerNibbles((byte) 0xF,nibble);
          break;

        // === Space and Ignored Characters ===
        case ' ':
        case '\t':
        case '\n':
        case '_':
        case ',':
          continue;
        default:
          return null;
      }

      if ((nibble % 2) == 1) {
        buf.put(current_byte);
        current_byte = 0;
      }
      nibble++;
    }

    buf.limit(buf.position());
    buf.rewind();
    return buf;
  }

  byte staggerNibbles(byte value, int nibble) {
    if ((nibble % 2) == 0)
      return (byte) (value << 4);
    else
      return (byte) (value & 0x0F);
  }

}
