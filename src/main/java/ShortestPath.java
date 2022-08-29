import java.util.*;

public class ShortestPath {


    public static Stack<Integer> aStar(Node[][] nodes, Node start, Node goal) {
        start.gCost = 0;
        getDistanceToGoal(start, goal);


        Set<Node> visited = new HashSet<>();
        Set<Node> checked = new HashSet<>();

        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(Node::getfCost).thenComparing(Node::getgCost));
        queue.add(start);
        Node current = null;
        while (!queue.isEmpty()) {
            current = queue.poll();

            if (current.col == goal.col && current.row == goal.row) {
                break;
            }
            if (visited.contains(current) || current.obstacle == 1) {
                continue;
            }
            visited.add(current);

            List<Node> neighbours = new ArrayList<>();
            addNeighbours(current, neighbours, nodes);

            for (Node node : neighbours) {
                if (visited.contains(node)) {
                    continue;
                }
                if (!checked.contains(node)) {
                    node.setParent(current);
                    node.gCost = current.gCost + 10;
                    getDistanceToGoal(node, goal);
                    queue.add(node);
                    checked.add(node);
                    continue;
                }
                int newDistanceToStart = current.gCost + 10;

                if (node.gCost > newDistanceToStart) {
                    node.setParent(current);
                    node.gCost = newDistanceToStart;
                    getDistanceToGoal(node, goal);
                }
            }
        }
        Stack<Integer> list = new Stack<>();

        if (current.col != goal.col || current.row != goal.row) {
            return list;
        }
        Node temp = current;
        while (temp != start) {
            if (temp == current) {
                list.push(temp.direction);
            }
            list.push(temp.direction);
            temp = temp.getPar();
        }

        return list;
    }


    public static void addNeighbours(Node node, List<Node> list, Node[][] nodes) {
        int col = node.col;
        int row = node.row;
        if (row > 1 && nodes[col][row - 1].obstacle == 0) {
            list.add(nodes[col][row - 1]);
            if (nodes[col][row - 1].direction == -1)
                    nodes[col][row - 1].direction = 1;
        }
        if (row < nodes[0].length - 1 && nodes[col][row + 1].obstacle == 0) {
            list.add(nodes[col][row + 1]);
            if (nodes[col][row + 1].direction == -1)
                nodes[col][row + 1].direction = 0;
        }
        if (col > 0 && nodes[col - 1][row].obstacle == 0 ){
            list.add(nodes[col - 1][row]);
            if (nodes[col - 1][row].direction == -1)
                nodes[col - 1][row].direction = 3;
        }
        if (col < nodes.length - 1 && nodes[col + 1][row].obstacle == 0) {
            list.add(nodes[col + 1][row]);
            if (nodes[col + 1][row].direction == -1)
                nodes[col + 1][row].direction = 2;
        }

    }

    public static void getDistanceToGoal(Node node, Node goal) {
        int colDistance = Math.abs(node.col - goal.col);
        int rowDistance = Math.abs(node.row - goal.row);
        node.hCost = Math.abs(colDistance - rowDistance) * 10 + Math.min(colDistance, rowDistance) * 14;
        node.fCost = node.hCost + node.gCost;
    }
}

class Node {
    int col;
    int row;
    int fCost = 1;
    int gCost;
    int hCost;
    int direction = -1;
    int obstacle;
    Node parent;

    public Node(int col, int row) {
        this.col = col;
        this.row = row;
    }

    public int getfCost() {
        return fCost;
    }


    public int getgCost() {
        return gCost;
    }

    public Node getPar() {
        return parent;
    }
    public  void setParent(Node parent) {
        this.parent = parent;
    }

}
