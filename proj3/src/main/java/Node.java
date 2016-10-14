import java.util.Comparator;

public class Node implements Comparable<Node> {
    private String filename;
    private double xTop;
    private double yTop;
    private double xBottom;
    private double yBottom;

    public Node(String fn, double ullon, double ullat, double lrlon, double lrlat) {
        filename = fn;
        xTop = ullon;
        yTop = ullat;
        xBottom = lrlon;
        yBottom = lrlat;
    }

    public double getxTop() {
        return xTop;
    }

    public double getyTop() {
        return yTop;
    }

    public double getxBottom() {
        return xBottom;
    }

    public double getyBottom() {
        return yBottom;
    }

    public String getTheFileName() {
        return filename;
    }

    @Override
    public int compareTo(Node n) {
        if (this.getyTop() > n.getyTop()) {
            return -1;
        } else if (this.getyTop() < n.getyTop()) {
            return 1;
        } else if (this.getxTop() < n.getxTop()) {
            return -1;
        } else if (this.getxTop() > n.getxTop()) {
            return 1;
        } else {
            return 0;
        }
    }
}
