public class HuffmanNode implements Comparable<HuffmanNode> {
  int frequency;
  char c;
  String code = null;

  HuffmanNode left;
  HuffmanNode right;

  public HuffmanNode(char c, int freq) {
    this.c = c;
    this.frequency = freq;
    this.left = this.right = null;
  }

  public HuffmanNode(char c, String code) {
    this.c = c;
    this.code = code;
    this.frequency = -1;
    this.left = this.right = null;
  }

  public HuffmanNode(HuffmanNode left, HuffmanNode right) {
    this.c = '\u0000';
    this.frequency = -1;
    this.left = left;
    this.right = right;
  }

  public int getFrequency() {
    return frequency;
  }

  @Override
  public int compareTo(HuffmanNode other) {
    return Integer.compare(this.frequency, other.frequency);
  }
}
