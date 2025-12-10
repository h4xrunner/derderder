import java.util.*;

public class Lab12 {

    public static void main(String[] args) {
        // You may test your graph here manually.
        // Official testing will be done with JUnit.
        
    }
}

interface IUndirectedNode<T> {
    void addNeighbor(Node<T> neighbor);
    void removeNeighbor(Node<T> neighbor);
}


class Node<T> implements IUndirectedNode<T> {
    T data;
    List<Node<T>> neighbors;

    // BFS helper fields for easier implementation for shortest path method
    boolean visited;
    Node<T> parent;
    
    public Node(T data) {
        this.data = data;
        this.neighbors = new ArrayList<>();
    }
    
    @Override
    public void addNeighbor(Node<T> neighbor) {
        neighbors.add(neighbor);
    }
    
    @Override
    public void removeNeighbor(Node<T> neighbor) {
        neighbors.remove(neighbor);
    }

    
    @Override
    public String toString() {
        return data.toString();
    }
}


interface AdjacencyList<T> {
    void addNode(T node);
    void addEdge(T data1, T data2);
    void removeNode(T data);
    void removeEdge(T data1, T data2);
    List<T> bfs(T startData);
    List<T> dfs(T startData);
    List<T> getShortestPath(T startData, T endData);
}

class Graph<T> implements AdjacencyList<T> {
    private Map<T, Node<T>> nodes;
    
    public Graph() {
        this.nodes = new HashMap<>();
    }

    
    @Override
    public void addNode(T data) {
        /*
         * TODO:
         * Add a new node if it does not already exist.
         * Store it inside nodes.
         */
        if (!nodes.containsKey(data)) {
            Node<T> newNode = new Node<>(data);
            nodes.put(data, newNode);
        }
    }
    
    @Override
    public void addEdge(T data1, T data2) {
        /*
         * TODO:
         * Ensure both nodes exist (create if needed).
         * Add each node as a neighbor to the other (undirected graph).
         */
        if (!nodes.containsKey(data1)) {
            addNode(data1);
        }
        if (!nodes.containsKey(data2)) {
            addNode(data2);
        }
        
        Node<T> node1 = nodes.get(data1);
        Node<T> node2 = nodes.get(data2);
        
        node1.addNeighbor(node2);
        node2.addNeighbor(node1);
    }
    
    @Override
    public void removeEdge(T data1, T data2) {
        /*
         * TODO:
         * If both nodes exist:
         * - Remove data2 from data1's neighbor list
         * - Remove data1 from data2's neighbor list
         */
        Node<T> node1 = nodes.get(data1);
        Node<T> node2 = nodes.get(data2);
        
        if (node1 != null && node2 != null) {
            node1.removeNeighbor(node2);
            node2.removeNeighbor(node1);
        }
    }
    
    @Override
    public void removeNode(T data) {
        /*
         * TODO:
         * If node exists:
         * - Remove this node from all neighbor lists
         * - Remove the node from nodes
         */
        Node<T> nodeToRemove = nodes.get(data);
        
        if (nodeToRemove != null) {
            for (Node<T> neighbor : nodeToRemove.neighbors) {
                neighbor.neighbors.remove(nodeToRemove);
            }
            nodes.remove(data);
        }
    }
    
    @Override
    public List<T> bfs(T startData) {
        /*
         * TODO:
         * Perform Breadth-First Search starting from startData.
         *
         * Steps:
         * 1. Get starting Node
         * 2. Use a Queue for BFS
         * 3. Maintain a visited Set
         * 4. Add nodes to result in order visited
         */
        List<T> result = new ArrayList<>();
        Node<T> startNode = nodes.get(startData);
        
        if (startNode == null) {
            return result;
        }
        
        Queue<Node<T>> queue = new LinkedList<>();
        Set<Node<T>> visited = new HashSet<>();
        
        queue.add(startNode);
        visited.add(startNode);
        
        while (!queue.isEmpty()) {
            Node<T> currentNode = queue.poll();
            result.add(currentNode.data);
            
            for (Node<T> neighbor : currentNode.neighbors) {
                if (!visited.contains(neighbor)) {
                    queue.add(neighbor);
                    visited.add(neighbor);
                }
            }
        }
        
        return result;
    }
    
    @Override
    public List<T> dfs(T startData) {
        /*
         * TODO:
         * Perform Depth-First Search (recursive or stack version).
         *
         * Steps:
         * 1. Use a Set<Node> to track visited nodes
         * 2. Visit node, then recursively visit neighbors
         */
        List<T> result = new ArrayList<>();
        Node<T> startNode = nodes.get(startData);
        
        if (startNode == null) {
            return result;
        }
        
        Set<Node<T>> visited = new HashSet<>();
        dfsHelper(startNode, visited, result);
        
        return result;
    }
    
    private void dfsHelper(Node<T> current, Set<Node<T>> visited, List<T> result) {
        /*
         * TODO:
         * Recursive DFS helper.
         */
        visited.add(current);
        result.add(current.data);
        
        for (Node<T> neighbor : current.neighbors) {
            if (!visited.contains(neighbor)) {
                dfsHelper(neighbor, visited, result);
            }
        }
    }
    
    @Override
    public List<T> getShortestPath(T startData, T endData) {
        /*
         * TODO:
         * Use BFS to compute shortest path in an unweighted graph.
         *
         * Steps:
         * 1. BFS until reaching end node
         * 2. Reconstruct path by following parent pointers
         * 3. Reverse and return the path
         */
        List<T> path = new ArrayList<>();
        Node<T> startNode = nodes.get(startData);
        Node<T> endNode = nodes.get(endData);
        
        if (startNode == null || endNode == null) {
            return path;
        }
        
        for (T key : nodes.keySet()) {
            Node<T> n = nodes.get(key);
            n.visited = false;
            n.parent = null;
        }
        
        Queue<Node<T>> queue = new LinkedList<>();
        queue.add(startNode);
        startNode.visited = true;
        
        while (!queue.isEmpty()) {
            Node<T> currentNode = queue.poll();
            
            if (currentNode.data.equals(endData)) {
                Node<T> temp = currentNode;
                while (temp != null) {
                    path.add(temp.data);
                    temp = temp.parent;
                }
                Collections.reverse(path);
                return path;
            }
            
            for (Node<T> neighbor : currentNode.neighbors) {
                if (!neighbor.visited) {
                    neighbor.visited = true;
                    neighbor.parent = currentNode;
                    queue.add(neighbor);
                }
            }
        }
        
        return path;
    }
    
    public void printGraph() {
        for (T key : nodes.keySet()) {
            System.out.print(key + " -> ");
            Node<T> node = nodes.get(key);
            for (Node<T> neighbor : node.neighbors) {
                System.out.print(neighbor.data + " ");
            }
            System.out.println();
        }
    }
}
Graph<String> graph = new Graph<>();
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addNode("D");
        graph.addNode("E");
        
        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        graph.addEdge("B", "D");
        graph.addEdge("C", "D");
        graph.addEdge("D", "E");
        
        graph.printGraph();
