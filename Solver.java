/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.MinPQ;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Solver {
    private final List<Board> solution = new LinkedList<>();
    private boolean isSolvable;
    private int moves;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        MinPQ<Node> minPQ = new MinPQ<Node>();
        MinPQ<Node> minPQTwin = new MinPQ<Node>();
        minPQ.insert(new Node(initial, null));
        minPQTwin.insert(new Node(initial.twin(), null));

        while (!minPQ.isEmpty()) {
            Node node = minPQ.delMin();
            Node nodeTwin = minPQTwin.delMin();
            if (nodeTwin.board.isGoal()) {
                isSolvable = false;
                break;
            }
            if (node.board.isGoal()) {
                moves = node.moves;
                isSolvable = true;
                for (Node n = node; n != null; n = n.prev) {
                    solution.add(n.board);
                }
                Collections.reverse(solution);
                break;
            }
            for (Board b : node.board.neighbors()) {
                if (node.prev == null || !node.prev.board.equals(b)) {
                    minPQ.insert(new Node(b, node));
                }
            }
            for (Board b : nodeTwin.board.neighbors()) {
                if (nodeTwin.prev == null || !nodeTwin.prev.board.equals(b)) {
                    minPQTwin.insert(new Node(b, nodeTwin));
                }
            }
        }
    }

    private static class Node implements Comparable<Node> {
        private final Board board;
        private final Node prev;
        private final int moves;
        private final int fun;

        Node(Board board, Node prev) {
            this.board = board;
            this.prev = prev;
            this.moves = prev == null ? 0 : prev.moves + 1;
            fun = moves + board.manhattan();
        }

        @Override
        public int compareTo(Node node) {
            return Integer.compare(fun(), node.fun());
        }

        private int fun() {
            return fun;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        return isSolvable ? moves : -1;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        return isSolvable ? solution : null;
    }
}
