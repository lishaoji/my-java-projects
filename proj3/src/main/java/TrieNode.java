
public class TrieNode implements Comparable<TrieNode>{
    public String lon;
    public String lat;
    public String ID;
    public boolean isConnected;
    public boolean isHighWay;
    public double priority;
    public double distance;
    TrieNode prev;

    public TrieNode(String longitude, String lattitude, String id,
                    boolean c, boolean h, TrieNode p) {
        lon = longitude;
        lat = lattitude;
        ID = id;
        isConnected = c;
        isHighWay = h;
        prev = p;
        priority = Double.POSITIVE_INFINITY;
        distance = Double.POSITIVE_INFINITY;
    }

    @Override
    public int compareTo(TrieNode tn) {
        if (this.priority > tn.priority) {
            return 1;
        } else if (this.priority < tn.priority) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (this.getClass() != other.getClass()) {
            return false;
        }
        TrieNode tn = (TrieNode) other;
        return (this.lon == tn.lon && this.lat == tn.lat
                && this.ID.equals(tn.ID)
                && this.priority == tn.priority
                && this.distance == tn.distance);
    }
}
