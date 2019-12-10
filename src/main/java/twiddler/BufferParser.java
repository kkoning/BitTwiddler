package twiddler;

import java.nio.ByteBuffer;

public interface BufferParser {
  ByteBuffer parse(String input);
}
