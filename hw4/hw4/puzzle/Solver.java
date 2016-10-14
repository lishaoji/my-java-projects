package hw4.puzzle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;


public class Solver {
    private LinkedList<Board> boards;
    private int move;
    private boolean solvable;

    private class Node implements Comparable<Node> {
        private Board board;
        private int moves;
        private Node prev;
        private int priority;

        private Node(Board b, int m, Node p) {
            this.board = b;
            this.moves = m;
            this.prev = p;
            this.priority = m + b.manhattan();
        }

        private int getPriority() {
            return priority;
        }

        @Override
        public int compareTo(Node that) {
            if (this.getPriority() > that.getPriority()) {
                return 1;
            }
            if (this.getPriority() < that.getPriority()) {
                return -1;
            }
            return 0;
        }
    }

    public Solver(Board initial) {
        boards = new LinkedList<>();
        if (initial.isGoal()) {
            solvable = true;
            boards.addFirst(initial);
            return;
        }

        MinPQ<Node> minPQ = new MinPQ<>();
        move = 0;
        Board board = initial;
        Node node = new Node(board, move, null);
        minPQ.insert(node);
        while (move < 100) {
            node = minPQ.delMin();
            board = node.board;
            if (board.isGoal()) {
                solvable = true;
                this.boards.push(board);
                while (node.prev != null) {
                    node = node.prev;
                    this.boards.push(node.board);
                }
                return;
            }
            node.moves += 1;
            Iterable<Board> neighbors = BoardUtils.neighbors(board);
            for (Board neighbor : neighbors) {
                if (node.prev == null || !neighbor.equals(node.prev.board)) {
                    Node newNode = new Node(neighbor, node.moves, node);
                    minPQ.insert(newNode);
                }
            }
        }
    }
    public int moves() {
        if (solvable) {
            return boards.size() - 1;
        }
        return -1;
    }
    public Iterable<Board> solution() {
        if (solvable) {
            return boards;
        } else {
            return null;
        }
    }
    // DO NOT MODIFY MAIN METHOD
    // Uncomment this method once your Solver and Board classes are ready.
    public static void main(String[] args) {
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board initial = new Board(tiles);
        Solver solver = new Solver(initial);
        StdOut.println("Minimum number of moves = " + solver.moves());
        for (Board board : solver.solution()) {
            StdOut.println(board);
        }
    }

}
