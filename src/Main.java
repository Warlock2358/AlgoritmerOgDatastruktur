import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.PriorityQueue;

public class Main {
  public static StringBuilder inputString = new StringBuilder();
  public static StringBuilder encodedString = new StringBuilder();

  public static void main(String[] args) throws IOException {

    // File names for original files
    String fileName1 = "opg8-kompr.lyx";
    String fileName2 = "diverse.txt";
    String fileName3 = "diverse.lyx";
    String fileName4 = "enwik8.txt";

    // File names for compressed files
    String zipFileName1 = "opg8-komprCompressed.lyx";
    String zipFileName2 = "diverseCompressed.txt";
    String zipFileName3 = "diverseCompressed.lyx";
    String zipFileName4 = "enwik8Compressed.txt";

    // File names for decompressed files
    String unzipFileName1 = "opg8-komprDecompressed.lyx";
    String unzipFileName2 = "diverseDecompressed.txt";
    String unzipFileName3 = "diverseDecompressed.lyx";
    String unzipFileName4 = "enwik8Decompressed.txt";


    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName1)));

      String line;
      while ((line = reader.readLine()) != null) {
        inputString.append(line);
      }
      reader.close();

     CalculateFrequencies cf = new CalculateFrequencies();
      cf.calcFreq(inputString.toString());

      HuffmanTree ht = new HuffmanTree(cf.nodes);
      ArrayList<HuffmanCode> huffmanCodes = new ArrayList<>();
      //ht.buildTree();
      PriorityQueue<HuffmanNode> pq = new PriorityQueue<>();

      pq.addAll(cf.nodes);
      ht.root = ht.generateTree(pq);
      ht.createHuffmanCodes(ht.root, "", huffmanCodes);
      ht.encodeString(inputString.toString(), huffmanCodes);

      // TODO Does not work
      BufferedReader reader2 = new BufferedReader(new InputStreamReader(new FileInputStream(fileName1)));
      int[] freq = new int[256];

      int character;
      while ((character = reader2.read()) != -1) {
        char c = (char) character;
        if (c < 256) {
          freq[c]++;
        }
      }

      ArrayList<HuffmanNode> nodes = cf.getFreq(freq);
      ArrayList<String> word = new ArrayList<>();
      byte[] bytes = Files.readAllBytes(Path.of(fileName1));
      StringBuilder str = new StringBuilder();

      for (byte aByte : bytes) {
        for (HuffmanNode n1 : nodes) {
          if (aByte == n1.c) {
            str.append(n1.code);
            if (str.length() % 8 == 0) {
              word.add(str.toString());
              str = new StringBuilder();
            }
          }
        }
      }
      word.add(str.toString());
      OutputStream oos = new FileOutputStream(zipFileName1);
      BufferedOutputStream bos = new BufferedOutputStream(oos);
      DataOutputStream os = new DataOutputStream(bos);
      StringBuilder frequencies = new StringBuilder();
      for (HuffmanNode node : nodes) {
        frequencies.append(node.c).append(String.valueOf(node.frequency).length()).append(node.frequency);
      }
      int z = 0;
      for (String s : word) {
        z += s.length();
      }
      for (int i = 0; i < 8 - z % 8; i++) {
        word.set(word.size() - 1, word.get(word.size() - 1) + "0");
      }

      int freqLength = frequencies.length();
      int antZero = 8 - z % 8;
      int startLength = String.valueOf(freqLength).length();
      int endLength = String.valueOf(antZero).length();
      os.write(String.valueOf(startLength).getBytes());
      os.write(String.valueOf(freqLength).getBytes());
      os.write(String.valueOf(endLength).getBytes());
      os.write(String.valueOf(antZero).getBytes());
      os.write(frequencies.toString().getBytes());
      StringBuilder output = new StringBuilder();
      for (String smi : word) {
        String[] words = smi.split("(?<=\\G........)");
        for (String sm : words) {
          // System.out.println(sm);
          int b = Integer.parseInt(sm, 2);
          output.append((char) b);
        }
      }

      os.write(output.toString().getBytes());
      os.close();
      System.out.println("Successfully wrote to the file.");


    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}