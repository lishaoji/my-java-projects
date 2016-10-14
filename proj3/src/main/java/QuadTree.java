import java.util.ArrayList;

public class QuadTree {
    public QTreeNode root;
    public int height;
    public ArrayList<Node> traversal;

    public QuadTree(double ROOT_ULLON, double ROOT_ULLAT, double ROOT_LRLON, double ROOT_LRLAT, int h) {
        QTreeNode root_node = new QTreeNode(new Node("", ROOT_ULLON, ROOT_ULLAT, ROOT_LRLON, ROOT_LRLAT), 0);
        root = buildTree(root_node, h);
        height = h;
    }

    public QTreeNode buildTree(QTreeNode r, int height) {
        if (height == 0) {
            return r;
        } else {
            QTreeNode c1 = buildTree(makeChild1(r), height - 1);
            QTreeNode c2 = buildTree(makeChild2(r), height - 1);
            QTreeNode c3 = buildTree(makeChild3(r), height - 1);
            QTreeNode c4 = buildTree(makeChild4(r), height - 1);
            r.child1 = c1;
            r.child2 = c2;
            r.child3 = c3;
            r.child4 = c4;
            return r;
        }
    }

    public QTreeNode makeChild1(QTreeNode parent) {
        Node p = parent.node;
        String name = p.getTheFileName() + 1;
        double ullon = p.getxTop();
        double ullat = p.getyTop();
        double lrlon = (p.getxTop() + p.getxBottom()) / 2;
        double lrlat = (p.getyTop() + p.getyBottom()) / 2;
        int depth = parent.depth;
        QTreeNode toReturn = new QTreeNode(new Node(name, ullon, ullat, lrlon, lrlat), depth + 1);
        return toReturn;
    }

    public QTreeNode makeChild2(QTreeNode parent) {
        Node p = parent.node;
        String name = p.getTheFileName() + 2;
        double ullon = (p.getxTop() + p.getxBottom()) / 2;
        double ullat = p.getyTop();
        double lrlon = p.getxBottom();
        double lrlat = (p.getyTop() + p.getyBottom()) / 2;
        int depth = parent.depth;
        QTreeNode toReturn = new QTreeNode(new Node(name, ullon, ullat, lrlon, lrlat), depth + 1);
        return toReturn;
    }

    public QTreeNode makeChild3(QTreeNode parent) {
        Node p = parent.node;
        String name = p.getTheFileName() + 3;
        double ullon = p.getxTop();
        double ullat = (p.getyTop() + p.getyBottom()) / 2;
        double lrlon = (p.getxTop() + p.getxBottom()) / 2;
        double lrlat = p.getyBottom();
        int depth = parent.depth;
        QTreeNode toReturn = new QTreeNode(new Node(name, ullon, ullat, lrlon, lrlat), depth + 1);
        return toReturn;
    }

    public QTreeNode makeChild4(QTreeNode parent) {
        Node p = parent.node;
        String name = p.getTheFileName() + 4;
        double ullon = (p.getxTop() + p.getxBottom()) / 2;
        double ullat = (p.getyTop() + p.getyBottom()) / 2;
        double lrlon = p.getxBottom();
        double lrlat = p.getyBottom();
        int depth = parent.depth;
        QTreeNode toReturn = new QTreeNode(new Node(name, ullon, ullat, lrlon, lrlat), depth + 1);
        return toReturn;
    }

    public ArrayList<Node> traversal(int depth, double ullon, double ullat, double lrlon, double lrlat) {
        traversal = new ArrayList<>();
        traversalHelper(root, depth, ullon, ullat, lrlon, lrlat);
        return traversal;
    }

    private void traversalHelper(QTreeNode r, int depth, double ullon, double ullat, double lrlon, double lrlat) {
        if (r.depth == depth && intersects(r.node, ullon, ullat, lrlon, lrlat)) {
            traversal.add(r.node);
        } else {
            if (intersects(r.child1.node, ullon, ullat, lrlon, lrlat)) {
                traversalHelper(r.child1, depth, ullon, ullat, lrlon, lrlat);
            }
            if (intersects(r.child2.node, ullon, ullat, lrlon, lrlat)) {
                traversalHelper(r.child2, depth, ullon, ullat, lrlon, lrlat);
            }
            if (intersects(r.child3.node, ullon, ullat, lrlon, lrlat)) {
                traversalHelper(r.child3, depth, ullon, ullat, lrlon, lrlat);
            }
            if (intersects(r.child4.node, ullon, ullat, lrlon, lrlat)) {
                traversalHelper(r.child4, depth, ullon, ullat, lrlon, lrlat);
            }
        }
    }

    private static boolean intersects(Node n, double ullon, double ullat, double lrlon, double lrlat) {
        double xTop = n.getxTop();
        double yTop = n.getyTop();
        double xBottom = n.getxBottom();
        double yBottom = n.getyBottom();
        return (xTop < lrlon && xBottom > ullon && yBottom < ullat && yTop > lrlat);
    }
}
