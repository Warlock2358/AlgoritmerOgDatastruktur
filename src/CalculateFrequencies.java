import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class CalculateFrequencies {

  ArrayList<HuffmanNode> nodes = new ArrayList<>();

  public void calcFreq(String s){
    for (char c : s.toCharArray()) {
      boolean found = false;
      for (HuffmanNode node : nodes) {
        if (node.c == c) {
          node.frequency++;
          found = true;
          break;
        }
      }
      if (!found) {
        HuffmanNode node = new HuffmanNode(c, 1);
        nodes.add(node);
      }
    }
  }

  public ArrayList<HuffmanNode> getFreq(int[] frequency) {
    int[] temp = Arrays.copyOf(frequency, frequency.length);
    ArrayList<HuffmanNode> list = new ArrayList<>();

    for (int i = 0; i < temp.length; i++) {
      if (temp[i] != 0) {
        list.add(new HuffmanNode((char) i, temp[i]));
      }
    }
    return list;
  }

  public int[] countFrequencies(String file) {
    int[] frequncies = new int[256];
    try {
      BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
      int character;
      while ((character = reader.read()) != -1) {
        frequncies[character]++;
      }
    } catch (Exception e) {
      System.out.println("An error occurred with reading the file.");
    }
    return frequncies;
  }
}
