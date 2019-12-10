package twiddler;

import java.nio.ByteBuffer;

public class BinaryBufferParser implements BufferParser {
  @Override
  public ByteBuffer parse(String input) {
    // Need at least 8 characters per output byte.
    ByteBuffer buf = ByteBuffer.wrap(new byte[input.length() / 8]);
    char[] chars = input.toCharArray();
    byte current_byte = 0;
    int bit = 0;
    for (int i = 0; i < chars.length; i++) {
      char c = chars[i];
      switch(c) {
        // === Hex Digits ===
        case '0':
          current_byte |= staggerBits((byte) 0,bit);
          break;
        case '1':
          current_byte |= staggerBits((byte) 1,bit);
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

      // We just completed a byte.
      if ((bit % 8) == 7) {
        buf.put(current_byte);
        current_byte = 0;
      }
      bit++;
    }

    buf.limit(buf.position());
    buf.rewind();
    return buf;
  }

  byte staggerBits(byte value, int bit) {
    int shiftBits = 7 - (bit % 8);
    return (byte) (value << shiftBits);

  }
}
