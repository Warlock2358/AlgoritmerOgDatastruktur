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

  public HuffmanTree() {
  }

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
  public void encodeString(String s, ArrayList<HuffmanCode> hc) {
    for (char c : s.toCharArray()) {
      encodedString.append(Objects.requireNonNull(hc.stream().filter(huffmanCode -> huffmanCode.c == c).findFirst().orElse(null)).code);
    }
  }
  public void createHuffmanCodes(HuffmanNode root, String code, ArrayList<HuffmanCode> hc) {
    if (root == null) {
      return;
    }

    if (root.isLeaf()) {
      hc.add(new HuffmanCode(root.c, code));
      root.code = code;
    }

    createHuffmanCodes(root.left, code + "0", hc);
    createHuffmanCodes(root.right, code + "1", hc);
  }


  public HuffmanNode buildHuffmanTree(int[] frequencies) {
    PriorityQueue<HuffmanNode> pq = buildPriorityQueue(frequencies) ;

    while (pq.size() > 1) {
      HuffmanNode left = pq.poll();
      HuffmanNode right = pq.poll();

      HuffmanNode node = new HuffmanNode(left, right);
      assert right != null;
      node.frequency = left.frequency + right.frequency;
      node.left = left;
      node.right = right;

      pq.add(node);
    }

    return pq.poll();
  }

  public PriorityQueue<HuffmanNode> buildPriorityQueue(int[] frequencies) {
    PriorityQueue<HuffmanNode> pq = new PriorityQueue<>();

    for (int i = 0; i < frequencies.length; i++) {
      if (frequencies[i] > 0) {
        char c = (char) i;
        int frequency = frequencies[i];
        HuffmanNode node = new HuffmanNode(c, frequency);
        pq.add(node);
      }
    }
    return pq;
  }

  public void generateHuffmanCodes(HuffmanNode root, String code, String[] huffmanCode) {
    if (root == null) {
      return;
    }

    if (root.isLeaf()) {
      huffmanCode[root.c] = code;
    }

    generateHuffmanCodes(root.left, code + "0", huffmanCode);
    generateHuffmanCodes(root.right, code + "1", huffmanCode);
  }

  public String[] getHuffmanCodes(HuffmanNode root) {
    String[] huffmanCodes = new String[256];
    generateHuffmanCodes(root, "", huffmanCodes);
    return huffmanCodes;
  }
}
