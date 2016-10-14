package hw4.puzzle;

public class Board {
    private final int[][] t;
    private final int n;

    public Board(int[][] tiles) {
        n = tiles.length;
        t = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                t[i][j] = tiles[i][j];
            }
        }
    }
    public int tileAt(int i, int j) {
        if (i >= size() || j >= size()) {
            throw  new java.lang.IndexOutOfBoundsException("Index greater than size!");
        }
        return t[i][j];
    }
    public int size() {
        return n;
    }
    public int hamming() {
        int sum = 0;
        for (int i = 0; i < n; i += 1) {
            for (int j = 0; j < n; j += 1) {
                if (t[i][j] != i * n + j + 1 && t[i][j] != 0) {
                    sum += 1;
                }
            }
        }
        return sum;
    }
    public int manhattan() {
        int sum = 0;
        for (int i = 0; i < n; i += 1) {
            for (int j = 0; j < n; j += 1) {
                int value = t[i][j];
                if (t[i][j] != i * n + j + 1 && t[i][j] != 0) {
                    int x = (value - 1) / n;
                    int y = value % n - 1;
                    int ds = Math.abs(i - x) + Math.abs(j - y);
                    sum += ds;
                }
            }
        }
        return sum;
    }
    public boolean isGoal() {
//        for (int i = 0; i < size(); i += 1) {
//            for (int j = 0; j < size(); j += 1) {
//                if (t[i][j] != i * N + j + 1) {
//                    return false;
//                }
//            }
//        }
//        return true;
        return manhattan() == 0;
    }

    public int hashCode() {
        return 1;
    }
    
    public boolean equals(Object y) {
        if (!y.getClass().equals(t.getClass())) {
            return false;
        }
        Board b = (Board) y;
        for (int i = 0; i < size(); i += 1) {
            for (int j = 0; j < size(); j += 1) {
                if (t[i][j] != b.t[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /** Returns the string representation of the board. 
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
