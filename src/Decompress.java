import java.io.*;
import java.nio.Buffer;


public class Decompress {
  public String writeDecompressedFileName(String file) {
    String extensionlessFile = file.substring(0, file.lastIndexOf('.'));
    String extension = file.substring(file.lastIndexOf('.') + 1);

    if (extensionlessFile.endsWith("Compressed")) {
      extensionlessFile = extensionlessFile.substring(0, extensionlessFile.lastIndexOf("Compressed"));
    }

    return extensionlessFile + "Decompressed." + extension;
  }

  public int[] readFrequencies(DataInputStream reader) throws IOException {
    int[] frequencies = new int[256];
    for (int i = 0; i < frequencies.length; i++) {
      frequencies[i] = reader.readInt();
    }
    return frequencies;
  }

  public void decompress(String compressedFile, String decompressedFile) {
    try {
      DataInputStream reader = new DataInputStream(new FileInputStream(compressedFile));
      BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(decompressedFile));

      int[] frequencies = readFrequencies(reader);

      HuffmanTree hf = new HuffmanTree();
      HuffmanNode root = hf.buildHuffmanTree(frequencies);

      decodeFile(root, reader, writer);

      System.out.println("File decompressed successfully.");
      reader.close();
      writer.close();
    } catch (Exception e) {
      System.out.println("An error occurred with reading the file during decompression.");
    }
  }

  public void decodeFile(HuffmanNode root, DataInputStream reader, BufferedOutputStream writer) throws IOException {
    HuffmanNode node = root;
    int bit;

    while ((bit = reader.read()) != -1) {
      for (int i = 7; i >= 0; i--) {
        int bitValue = (bit >>> i) & 1;
        if (bitValue == 0) {
          node = node.left;
        } else {
          node = node.right;
        }
        assert node != null;
        if (node.isLeaf()) {
          writer.write(node.c);
          node = root;
        }
      }
    }
  }
}
