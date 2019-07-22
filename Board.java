/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Board {
    private final int[][] tiles;
    private int manhattan = -1;
    private int hamming = -1;
    private String string;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = new int[tiles.length][tiles.length];
        for (int r = 0; r < tiles.length; r++) {
            for (int c = 0; c < tiles.length; c++) {
                this.tiles[r][c] = tiles[r][c];
            }
        }
    }

    // string representation of this board
    public String toString() {
        if (string != null) {
            return string;
        }
        StringBuilder s = new StringBuilder();
        int n = dimension();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        string = s.toString();
        return string;
    }

    // board dimension n
    public int dimension() {
        return tiles.length;
    }

    // number of tiles out of place
    public int hamming() {
        if (hamming == -1) {
            hamming = hamming0();
        }
        return hamming;
    }

    private int hamming0() {
        int i = 1;
        int result = 0;
        for (int[] tile : tiles) {
            for (int t : tile) {
                if (t != i && t != 0) {
                    result++;
                }
                i++;
            }
        }
        return result;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        if (manhattan == -1) {
            manhattan = manhattan0();
        }
        return manhattan;
    }

    private int manhattan0() {
        int result = 0;
        for (int r = 0; r < tiles.length; r++) {
            for (int c = 0; c < tiles[r].length; c++) {
                int t = tiles[r][c];
                if (t == 0) {
                    continue;
                }
                int rg = (t - 1) / dimension();
                int cg = (t - 1) - rg * dimension();
                result += Math.abs(rg - r) + Math.abs(cg - c);
            }
        }
        return result;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Board board = (Board) obj;
        return Arrays.deepEquals(tiles, board.tiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int[] zeroC = findZero();
        int zr = zeroC[0];
        int zc = zeroC[1];
        List<Board> result = new LinkedList<>();

        if (zr - 1 >= 0) {
            Board board = new Board(tiles);
            board.swap(zr, zc, zr - 1, zc);
            result.add(board);
        }
        if (zr + 1 <= dimension() - 1) {
            Board board = new Board(tiles);
            board.swap(zr, zc, zr + 1, zc);
            result.add(board);
        }
        if (zc - 1 >= 0) {
            Board board = new Board(tiles);
            board.swap(zr, zc, zr, zc - 1);
            result.add(board);
        }
        if (zc + 1 <= dimension() - 1) {
            Board board = new Board(tiles);
            board.swap(zr, zc, zr, zc + 1);
            result.add(board);
        }

        return result;
    }

    private void swap(int r1, int c1, int r2, int c2) {
        int old = tiles[r1][c1];
        tiles[r1][c1] = tiles[r2][c2];
        tiles[r2][c2] = old;
    }

    private int[] findZero() {
        for (int r = 0; r < tiles.length; r++) {
            for (int c = 0; c < tiles[r].length; c++) {
                if (tiles[r][c] == 0) {
                    return new int[] { r, c };
                }
            }
        }
        throw new IllegalArgumentException("no zero");
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board board = new Board(tiles);
        int r1, c1, r2, c2;
        r1 = 0;
        c1 = 0;
        if (tiles[r1][c1] == 0) {
            c1 = 1;
        }
        r2 = 1;
        c2 = 1;

        if (tiles[r2][c2] == 0) {
            c2 = 0;
        }

        board.swap(r1, c1, r2, c2);
        return board;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        In in = new In("puzzle06.txt");
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board board = new Board(tiles);
        System.out.println("board = " + board);
        System.out.println("board.isGoal() = " + board.isGoal());
        System.out.println("board.manhattan() = " + board.manhattan());
        System.out.println("board.hamming() = " + board.hamming());
        System.out.println("board.twin() = " + board.twin());
    }
}
