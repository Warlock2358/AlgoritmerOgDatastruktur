import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {

  public static String testString = "aaaaabbbbcccdde";
  public static StringBuilder inputString = new StringBuilder();
  public static StringBuilder encodedString = new StringBuilder();
  public static BitSet huffmanCodeBit;

  public static void main(String[] args) throws IOException {
    String fileName1 = "testFile.txt";
    String fileName2 = "opg8-kompr.lyx";
    String fileName3 = "diverse.txt";
    String fileName4 = "diverse.lyx";
    String fileName5 = "enwik8.txt";

    String fileName = fileName5;

    String zipFileName1 = "testFile7zip.txt";
    String zipFileName2 = "opg8-kompr7zip.lyx";
    String zipFileName3 = "diverseT7zip.txt";
    String zipFileName4 = "diverseL7zip.lyx";
    String zipFileName5 = "enwik87zip.txt";

    String zipFileName = zipFileName5;

    /*
    Scanner scanner = new Scanner(System.in);
    System.out.println("Enter the path to the file you want to encode:");
    String path = scanner.nextLine();
    System.out.println("-----");
     */


    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
      String line;
      while ((line = reader.readLine()) != null) {
        inputString.append(line);
      }

      try {
        File testFile = new File("encodedTest.txt");
        if (testFile.createNewFile()) {
          System.out.println("File created: " + testFile.getName());
        } else {
          System.out.println("File already exists.");
        }
      } catch (IOException e) {
        System.out.println("An error occurred with creating the file.");
      }

      System.out.println("-----");
      CalculateFrequencies cf = new CalculateFrequencies();
      cf.calcFreq(inputString.toString());

//    System.out.println("Character frequencies:");
//    for (HuffmanNode node : cf.nodes) {
//      System.out.println(node.c + " " + node.frequency);
//    }

      System.out.println("-----");
      System.out.println("Encoded string for characters:");
      HuffmanTree ht = new HuffmanTree(cf.nodes);
      ArrayList<HuffmanCode> huffmanCodes = new ArrayList<>();
      //ht.buildTree();
      PriorityQueue<HuffmanNode> pq = new PriorityQueue<>();

      pq.addAll(cf.nodes);
      ht.root = ht.generateTree(pq);
      assert ht.root != null;
      ht.printTree(ht.root, "", huffmanCodes);
      //System.out.println("-----");
      ht.encodeString(inputString.toString(), huffmanCodes);
      //System.out.println(ht.getEncodedString());

      int[] freq = new int[256];

      int character;
      while ((character = reader.read()) != -1) {
        char c = (char) character;
        if (c < 256) {
          freq[c]++;
        }
      }
      ArrayList<HuffmanNode> nodes = cf.getFreq(freq);
      ArrayList<String> word = new ArrayList<>();
      byte[] bytes = Files.readAllBytes(Path.of(fileName));
      StringBuilder str = new StringBuilder();

      for (byte aByte : bytes) {
        for (HuffmanNode n1 : ht.nodes) {
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
      OutputStream oos = new FileOutputStream(zipFileName);
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


      //System.out.println("-----");
      //writeToFile(ht, "encodedTest.txt");

      /*
      System.out.println("-----");
      System.out.println("Read from file:");
      readFromFile("encodedTest.txt");


      System.out.println("-----");
      System.out.println("Decoded string:");
      ht.decodeString(String.valueOf(encodedString), ht.root);
      //System.out.println(ht.getDecodedString());

      System.out.println("-----");
      if (ht.getDecodedString().contentEquals(inputString)) {
        System.out.println("Decoded string is equal to original string");
      } else {
        System.out.println("Decoded string is not equal to original string");
      }
      reader.close();
       */
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void writeToFile(HuffmanTree ht, String fileName) {
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
      writer.write(ht.getEncodedString());
      writer.close();
      System.out.println("Successfully wrote to the file.");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void readFromFile(String fileName) {
    try (BufferedReader encodeReader = new BufferedReader(new InputStreamReader(Files.newInputStream(Path.of(fileName)), StandardCharsets.UTF_8))) {
      String encodeLine;
      while ((encodeLine = encodeReader.readLine()) != null) {
        encodedString.append(encodeLine);
      }
      encodeReader.close();
      System.out.println("Successfully read from the file.");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}