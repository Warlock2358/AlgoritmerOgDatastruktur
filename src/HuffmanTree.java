import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.PriorityQueue;

public class HuffmanTree {

  public HuffmanNode root = null;
  public ArrayList<HuffmanNode> nodes;
  public StringBuilder encodedString = new StringBuilder();
  public StringBuilder decodedString = new StringBuilder();

  public HuffmanTree(ArrayList<HuffmanNode> nodes) {
    this.nodes = nodes;
  }

//  public void buildTree() {
//    while (nodes.size() > 1) {
//      HuffmanNode minNode1 = nodes.stream().min(Comparator.comparingInt(HuffmanNode::getFrequency)).get();
//      nodes.remove(minNode1);
//      HuffmanNode minNode2 = nodes.stream().min(Comparator.comparingInt(HuffmanNode::getFrequency)).orElse(null);
//      nodes.remove(minNode2);
//
//      HuffmanNode f = new HuffmanNode();
//
//      if (minNode2 != null) {
//        f.frequency = minNode1.frequency + minNode2.frequency;
//      }
//
//      f.left = minNode1;
//
//      f.right = minNode2;
//
//      root = f;
//
//      nodes.add(f);
//    }
//  }

  public HuffmanNode generateTree(PriorityQueue<HuffmanNode> pq) {
    while (pq.size() > 1) {
      HuffmanNode left = pq.poll();
      HuffmanNode right = pq.poll();

      HuffmanNode node = new HuffmanNode(left, right);

      if (right != null) {
        node.frequency = left.frequency + right.frequency;
      }

      node.left = left;

      node.right = right;

      root = node;

      pq.add(node);
    }
    return pq.poll();
  }


  public void printTree(HuffmanNode node, String s, ArrayList<HuffmanCode> hc) {
    if (node.left == null && node.right == null && Character.isDefined(node.c)) {
      //System.out.println(node.c + ":" + s);
      HuffmanCode huffmanCode;
      huffmanCode = new HuffmanCode();
      huffmanCode.c = node.c;
      huffmanCode.code = s;
      node.code = s;
      hc.add(huffmanCode);
      return;
    }
    if (!Character.isDefined(node.c)){
      System.out.println(node.c);
    }
    assert node.left != null;
    printTree(node.left, s + "0", hc);
    printTree(node.right, s + "1", hc);
  }

  public void encodeString(String s, ArrayList<HuffmanCode> hc) {
    for (char c : s.toCharArray()) {
      encodedString.append(Objects.requireNonNull(hc.stream().filter(huffmanCode -> huffmanCode.c == c).findFirst().orElse(null)).code);
    }
  }

  public String getEncodedString() {
    return encodedString.toString();
  }

  public void decodeString(String encodedString, HuffmanNode root) {
    HuffmanNode node = root;
    for (char c : encodedString.toCharArray()) {
      if (c == '0') {
        node = node.left;
      } else {
        node = node.right;
      }

      assert node != null;
      if (node.left == null && node.right == null) {
        //System.out.print(node.c);
        decodedString.append(node.c);
        node = root;
      }
    }
  }

  public String getDecodedString() {
    return decodedString.toString();
  }
}
