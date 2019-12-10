package twiddler;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.math.BigInteger;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

public class Controller implements Initializable {

  private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
  private static BigInteger longMax = new BigInteger(String.valueOf(Long.MAX_VALUE));
  private static BigInteger unsignedLongMax = new BigInteger("FFFFFFFFFFFFFFFF", 16);
  private static final String overflowErrMsg = "overflow";

  private static BufferParser hexBufferParser = new HexBufferParser();
  private static BufferParser binaryBufferParser = new BinaryBufferParser();



  @FXML
  public TextArea input;

  @FXML
  public ComboBox inputType;

  @FXML
  public ComboBox asTextType;

  @FXML
  public ComboBox endian;

  @FXML
  public TextField asI8;
  @FXML
  public TextField asI16;
  @FXML
  public TextField asI32;
  @FXML
  public TextField asI64;

  @FXML
  public TextField asU8;
  @FXML
  public TextField asU16;
  @FXML
  public TextField asU32;
  @FXML
  public TextField asU64;

  @FXML
  public TextField asF32;
  @FXML
  public TextField asF64;

  @FXML
  public TextField asHexU8;
  @FXML
  public TextField asHexU16;
  @FXML
  public TextField asHexU32;
  @FXML
  public TextField asHexU64;


  @FXML
  public TextArea asText;
  @FXML
  public TextArea asHexDump;
  @FXML
  public TextArea asBinary;

  @FXML
  public void processInput(Event e){

    ByteBuffer buf = null;

    String inputMode = (String) inputType.getValue();
    switch (inputMode) {
      case "UTF-8 Text":
        buf = ByteBuffer.wrap(input.getText().getBytes(StandardCharsets.UTF_8));
        updateFromBuffer(buf);
        endian.setDisable(false);
        break;
      case "UTF-16 Text (LE)":
        buf = ByteBuffer.wrap(input.getText().getBytes(StandardCharsets.UTF_16LE));
        updateFromBuffer(buf);
        endian.setDisable(false);
        break;
      case "Hex Bytes":
        buf = hexBufferParser.parse(input.getText());
        updateFromBuffer(buf);
        endian.setDisable(false);
        break;
      case "Binary Bytes":
        buf = binaryBufferParser.parse(input.getText());
        updateFromBuffer(buf);
        endian.setDisable(false);
        break;
      case "Decimal Integer":
        updateNumber(input.getText(), 10);
        endian.setDisable(true);
        break;
      case "Hex Integer":
        updateNumber(input.getText(), 16);
        endian.setDisable(true);
        break;
      case "Binary Integer":
        updateNumber(input.getText(), 2);
        endian.setDisable(true);
        break;
    }
  }

  void updateNumber(String input, int radix) {
    try {
      BigInteger num = new BigInteger(input, radix);

      // === Signed Integers ===
      try {
        byte val = num.byteValueExact();
        asI8.setText(Byte.toString(val));
      } catch (ArithmeticException ae) {
        asI8.setText(overflowErrMsg);
      }
      try {
        short val = num.shortValueExact();
        asI16.setText(Short.toString(val));
      } catch (ArithmeticException ae) {
        asI16.setText(overflowErrMsg);
      }
      try {
        int val = num.intValueExact();
        asI32.setText(Integer.toString(val));
      } catch (ArithmeticException ae) {
        asI32.setText(overflowErrMsg);
      }
      try {
        long val = num.longValueExact();
        asI64.setText(Long.toString(val));
      } catch (ArithmeticException ae) {
        asI64.setText(overflowErrMsg);
      }

      // === Unsigned Integers ===
      long longVal;
      try {
        longVal = num.longValueExact();
        if (longVal >= 0) {
          if (longVal <= 0xFF) {
            asU8.setText(Long.toUnsignedString(longVal));
          } else {
            asU8.setText(overflowErrMsg);
          }
          if (longVal <= 0xFFFF) {
            asU16.setText(Long.toUnsignedString(longVal));
          } else {
            asU16.setText(overflowErrMsg);
          }
          if (longVal <= 0xFFFF_FFFFL) {
            asU32.setText(Long.toUnsignedString(longVal));
          } else {
            asU32.setText(overflowErrMsg);
          }
          asU64.setText(num.toString(16));
        } else {
          asU8.setText("negative");
          asU16.setText("negative");
          asU32.setText("negative");
          asU64.setText("negative");
        }
      } catch (ArithmeticException ae) {
        asU8.setText(overflowErrMsg);
        asU16.setText(overflowErrMsg);
        asU32.setText(overflowErrMsg);
        if ((num.compareTo(unsignedLongMax) <= 0) & (num.compareTo(BigInteger.ZERO) >= 0))
          asU64.setText(num.toString(16));
        else
          asU64.setText(overflowErrMsg);
      }

      // === Hex ===
      try {
        longVal = num.longValueExact();
        if (longVal >= 0) {
          if (longVal <= 0xFF) {
            asHexU8.setText(String.format("%x", longVal));
          } else {
            asHexU8.setText(overflowErrMsg);
          }
          if (longVal <= 0xFFFF) {
            asHexU16.setText(String.format("%x", longVal));
          } else {
            asHexU16.setText(overflowErrMsg);
          }
          if (longVal <= 0xFFFF_FFFFL) {
            asHexU32.setText(String.format("%x", longVal));
          } else {
            asHexU32.setText(overflowErrMsg);
          }
        } else {
          if (longVal >= Byte.MIN_VALUE) {
            asHexU8.setText(String.format("%x", (byte) longVal));
          } else {
            asHexU8.setText(overflowErrMsg);
          }
          if (longVal >= Short.MIN_VALUE) {
            asHexU16.setText(String.format("%x", (short) longVal));
          } else {
            asHexU16.setText(overflowErrMsg);
          }
          if (longVal >= Integer.MIN_VALUE) {
            asHexU32.setText(String.format("%x", (int) longVal));
          } else {
            asHexU32.setText(overflowErrMsg);
          }
        }
      } catch (ArithmeticException ae) {
        asHexU8.setText(overflowErrMsg);
        asHexU16.setText(overflowErrMsg);
        asHexU32.setText(overflowErrMsg);
      }
      if ((num.compareTo(unsignedLongMax) <= 0) & (num.compareTo(BigInteger.ZERO) >= 0))
        asHexU64.setText(num.toString(16));
      else
        asHexU64.setText(overflowErrMsg);

      asBinary.setText(num.toString(2));
      asHexDump.setText(num.toString(16));
    } catch (NumberFormatException nfe) {
      setAll("Invalid Number");
    }

    asText.setText("N/A");
    asF32.setText("N/A");
    asF64.setText("N/A");
  }

  /**
   * Updates the interface based on new input in the form of a ByteBuffer.
   *
   * @param buf
   */
  public void updateFromBuffer(ByteBuffer buf) {
    // === Handle the buf==null error condition first.
    if (buf == null) {
      setAll("error");
      return;
    }

    // === Set up Buffer & Determine Endianness ===
    buf.rewind();
    if (endian.getValue().equals("Little Endian"))
      buf.order(ByteOrder.LITTLE_ENDIAN);

    // === Signed Integers ===
    // I8
    StringBuffer str = new StringBuffer();
    while (buf.remaining() >= Byte.BYTES) {
      byte value = buf.get();
      str.append(value);
      if (buf.remaining() >= Byte.BYTES)
        str.append(", ");
    }
    asI8.setText(str.toString());

    // I16
    buf.rewind();
    str = new StringBuffer();
    while (buf.remaining() >= Short.BYTES) {
      short value = buf.getShort();
      str.append(value);
      if (buf.remaining() >= Short.BYTES)
        str.append(", ");
    }
    asI16.setText(str.toString());

    // I32
    buf.rewind();
    str = new StringBuffer();
    while (buf.remaining() >= Integer.BYTES) {
      int value = buf.getInt();
      str.append(value);
      if (buf.remaining() >= Integer.BYTES)
        str.append(", ");
    }
    asI32.setText(str.toString());

    // I64
    buf.rewind();
    str = new StringBuffer();
    while (buf.remaining() >= Long.BYTES) {
      long value = buf.getLong();
      str.append(value);
      if (buf.remaining() >= Long.BYTES)
        str.append(", ");
    }
    asI64.setText(str.toString());


    // === Unigned Integers ===
    // U8
    buf.rewind();
    str = new StringBuffer();
    while (buf.remaining() >= Byte.BYTES) {
      var value = Byte.toUnsignedLong(buf.get());
      str.append(value);
      if (buf.remaining() >= Byte.BYTES)
        str.append(", ");
    }
    asU8.setText(str.toString());

    // U16
    buf.rewind();
    str = new StringBuffer();
    while (buf.remaining() >= Short.BYTES) {
      var value = Short.toUnsignedLong(buf.getShort());
      str.append(value);
      if (buf.remaining() >= Short.BYTES)
        str.append(", ");
    }
    asU16.setText(str.toString());

    // U32
    buf.rewind();
    str = new StringBuffer();
    while (buf.remaining() >= Integer.BYTES) {
      var value = Integer.toUnsignedLong(buf.getInt());
      str.append(value);
      if (buf.remaining() >= Integer.BYTES)
        str.append(", ");
    }
    asU32.setText(str.toString());

    // U64
    buf.rewind();
    str = new StringBuffer();
    while (buf.remaining() >= Long.BYTES) {
      var value = Long.toUnsignedString(buf.getLong());
      str.append(value);
      if (buf.remaining() >= Long.BYTES)
        str.append(", ");
    }
    asU64.setText(str.toString());


    // === Floating Point Values ===

    // F32
    buf.rewind();
    str = new StringBuffer();
    while (buf.remaining() >= Float.BYTES) {
      var value = buf.getFloat();
      str.append(value);
      if (buf.remaining() >= Float.BYTES)
        str.append(", ");
    }
    asF32.setText(str.toString());

    // 64
    buf.rewind();
    str = new StringBuffer();
    while (buf.remaining() >= Double.BYTES) {
      var value = buf.getDouble();
      str.append(value);
      if (buf.remaining() >= Double.BYTES)
        str.append(", ");
    }
    asF64.setText(str.toString());


    // === Hex Values ===
    // === Unsigned Integers ===
    // I8
    buf.rewind();
    str = new StringBuffer();
    while (buf.remaining() >= Byte.BYTES) {
      byte value = buf.get();
      str.append(String.format("%x", value));
      if (buf.remaining() >= Byte.BYTES)
        str.append(", ");
    }
    asHexU8.setText(str.toString());

    // I16
    buf.rewind();
    str = new StringBuffer();
    while (buf.remaining() >= Short.BYTES) {
      short value = buf.getShort();
      str.append(String.format("%x", value));
      if (buf.remaining() >= Short.BYTES)
        str.append(", ");
    }
    asHexU16.setText(str.toString());

    // I32
    buf.rewind();
    str = new StringBuffer();
    while (buf.remaining() >= Integer.BYTES) {
      int value = buf.getInt();
      str.append(String.format("%x", value));
      if (buf.remaining() >= Integer.BYTES)
        str.append(", ");
    }
    asHexU32.setText(str.toString());

    // I64
    buf.rewind();
    str = new StringBuffer();
    while (buf.remaining() >= Long.BYTES) {
      long value = buf.getLong();
      str.append(String.format("%x", value));
      if (buf.remaining() >= Long.BYTES)
        str.append(", ");
    }
    asHexU64.setText(str.toString());




    // === Lower Fields ===
    updateTextOutput(buf);
    updateHexOutput(buf);
    updateBinaryOutput(buf);

  }

  void updateTextOutput(ByteBuffer buf) {
    buf.rewind();
    String asTextType = (String) this.asTextType.getValue();
    Charset cs = StandardCharsets.UTF_8;
    if (asTextType.equals("UTF-16 LE"))
      cs = StandardCharsets.UTF_16LE;
    else if (asTextType.equals("ASCII"))
      cs = StandardCharsets.US_ASCII;

    String str = new String(buf.array(), cs);


    asText.setText(new String(buf.array(),cs));
  }

  public void updateHexOutput(ByteBuffer buf) {
    buf.rewind();
    StringBuffer sb = new StringBuffer();
    while (buf.remaining() > 0) {
      if ((buf.position() % 32) == 0) {
        if (buf.position() != 0)
          sb.append('\n');
        sb.append(String.format("%3x: ", buf.position()));
      } else {

        if ((buf.position() % 2) == 0)
          sb.append(' ');

        if ((buf.position() % 8) == 0)
          sb.append(' ');

        if ((buf.position() % 16) == 0)
          sb.append(' ');
      }

      byte b = buf.get();
      int pos = Byte.toUnsignedInt(b);
      sb.append(HEX_ARRAY[pos>>>4]);
      sb.append(HEX_ARRAY[pos & 0x0F]);
    }
    sb.append('\n');
    asHexDump.setText(sb.toString());
  }

  void updateBinaryOutput(ByteBuffer buf) {
    buf.rewind();

    StringBuffer sb = new StringBuffer();
    while (buf.remaining() > 0) {
      if ((buf.position() % 8) == 0) {
        if (buf.position() != 0)
          sb.append('\n');
        sb.append(String.format("%3x: ", buf.position()));
      } else {

        if ((buf.position() % 2) == 0)
          sb.append(' ');

        if ((buf.position() % 4) == 0)
          sb.append(' ');
      }

      byte b = buf.get();
      printBinaryByte(b,sb);
      sb.append(' ');
    }
    sb.append('\n');
    asBinary.setText(sb.toString());
  }

  void printBinaryByte(byte value, StringBuffer buf) {
    if ((value & 0b1000_0000) == 0)
      buf.append('0');
    else
      buf.append('1');
    if ((value & 0b0100_0000) == 0)
      buf.append('0');
    else
      buf.append('1');
    if ((value & 0b0010_0000) == 0)
      buf.append('0');
    else
      buf.append('1');
    if ((value & 0b0001_0000) == 0)
      buf.append('0');
    else
      buf.append('1');
    if ((value & 0b0000_1000) == 0)
      buf.append('0');
    else
      buf.append('1');
    if ((value & 0b0000_0100) == 0)
      buf.append('0');
    else
      buf.append('1');
    if ((value & 0b0000_0010) == 0)
      buf.append('0');
    else
      buf.append('1');
    if ((value & 0b0000_0001) == 0)
      buf.append('0');
    else
      buf.append('1');
  }

  /**
   * Sets all the output fields to the same string.
   *
   * Useful for clearing fields when switching between input modes and displaying error conditions.
   *
   * @param value
   */
  void setAll(String value) {
    asI8.setText(value);
    asI16.setText(value);
    asI32.setText(value);
    asI64.setText(value);
    asU8.setText(value);
    asU16.setText(value);
    asU32.setText(value);
    asU64.setText(value);
    asF32.setText(value);
    asF64.setText(value);
    asHexU8.setText(value);
    asHexU16.setText(value);
    asHexU32.setText(value);
    asHexU64.setText(value);
    asText.setText(value);
    asHexDump.setText(value);
    asBinary.setText(value);
  }


  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    // Set up the initial values, since this doesn't work straight from FXML?
    inputType.setValue("UTF-8 Text");
    asTextType.setValue("UTF-8");
    endian.setValue("Little Endian");
  }
}
