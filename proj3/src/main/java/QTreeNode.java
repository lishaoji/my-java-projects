public class QTreeNode {
    public Node node;
    public int depth;
    public QTreeNode child1, child2, child3, child4;

    public QTreeNode(Node n, int d) {
        node = n;
        child1 = null;
        child2 = null;
        child3 = null;
        child4 = null;
        depth = d;
    }

//    public QTreeNode(Node n, QTreeNode c1, QTreeNode c2, QTreeNode c3, QTreeNode c4) {
//        node = n;
//        child1 = c1;
//        child2 = c2;
//        child3 = c3;
//        child4 = c4;
//        children_size = 4;
//        depth = 0
//    }

//    public void addChild(Node x) {
//        QTreeNode nodeToAdd = new QTreeNode(x);
//        if (children_size == 3) {
//            child4 = nodeToAdd;
//        } else if (children_size == 2) {
//            child3 = nodeToAdd;
//        } else if (children_size == 1) {
//            child2 = nodeToAdd;
//        } else if (children_size == 0) {
//            child1 = nodeToAdd;
//        }
//        children_size = Math.min(4, children_size + 1);
//    }
//
//    public LinkedList<QTreeNode> getChildren() {
//        LinkedList<QTreeNode> children = new LinkedList<>();
//        children.add(child1);
//        children.add(child2);
//        children.add(child3);
//        children.add(child4);
//        return children;
//    }
}
