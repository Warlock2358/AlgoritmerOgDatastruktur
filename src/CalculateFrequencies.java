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
}
