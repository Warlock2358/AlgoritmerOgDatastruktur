import java.io.File;

public class MainKludge7 {
  public static void main(String[] args) {
    if (args.length < 2) {
      System.out.println("Usage: java Main <input file> <compress/decompress>");
      return;
    }

    String fileName;

    // Checks if given input for args[0] is the file name or the path to the file
    File file = new File(args[0]);
    if (file.exists()) {
      if (file.isFile()) {
        fileName = file.getName();
      } else if (file.isDirectory()) {
        fileName = file.getName();
      } else {
        System.out.println("Usage: java Main <input file/path> <compress/decompress>");
        return;
      }
    } else {
      System.out.println("The file does not exist.");
      return;
    }

    String mode = args[1];

//    String fileName = "diverse.txt";
//    String mode = "compress";

    if (mode.equalsIgnoreCase("compress")) {
      Compress c = new Compress();
      String compressedFileName = c.writeCompressedFileName(fileName);
      c.compress(fileName, compressedFileName);
    } else if (mode.equalsIgnoreCase("decompress")) {
      Decompress d = new Decompress();
      String decompressedFileName = d.writeDecompressedFileName(fileName);
      d.decompress(fileName, decompressedFileName);
    } else {
      System.out.println("Usage: java Main <input file> <compress/decompress>");
    }
  }
}
