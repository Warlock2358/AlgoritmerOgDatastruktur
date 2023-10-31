import java.io.*;

public class Compress {

  public String writeCompressedFileName(String file) {
    String extensionlessFile = file.substring(0, file.lastIndexOf('.'));
    String extension = file.substring(file.lastIndexOf('.') + 1);
    return extensionlessFile + "Compressed." + extension;
  }

  private void writeCompressedFile(String uncompressedFile, String compressedFile, int[] frequencies, String[] huffmanCodes) {
    try {
      BufferedInputStream reader = new BufferedInputStream(new FileInputStream(uncompressedFile));
      DataOutputStream writer = new DataOutputStream(new FileOutputStream(compressedFile));

      for (int frequency : frequencies) {
        writer.writeInt(frequency);
      }

      int bitBuffer = 0;
      int bitCount = 0;
      int character;

      while ((character = reader.read()) != -1) {
        String huffmanCode = huffmanCodes[character];

        for (char bit : huffmanCode.toCharArray()) {
          bitBuffer = (bitBuffer << 1) | (bit - '0');
          bitCount++;

          if (bitCount == 8) {
            writer.write(bitBuffer);
            bitBuffer = 0;
            bitCount = 0;
          }
        }
      }
      if (bitCount > 0) {
        writer.write(bitBuffer << (8 - bitCount));
      }

      reader.close();
      writer.close();
    } catch (Exception e) {
      System.out.println("An error occurred with reading the file.");
    }
  }

  public void compress(String file, String compressedFile) {
    int[] frequencies = new CalculateFrequencies().countFrequencies(file);
    HuffmanTree hf = new HuffmanTree();
    HuffmanNode root = hf.buildHuffmanTree(frequencies);
    String[] huffmanCodes = hf.getHuffmanCodes(root);
    writeCompressedFile(file, compressedFile, frequencies, huffmanCodes);
    System.out.println("File compressed successfully.");
  }
}
