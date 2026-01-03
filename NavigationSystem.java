import java.util.*;

class Node {
  private final String name;
  private final List<Edge> edges = new ArrayList<>();

  public Node(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public List<Edge> getEdges() {
    return edges;
  }

  public void addEdge(Edge edge) {
    edges.add(edge);
  }

  @Override
  public String toString() {
    return name;
  }
}

class Edge {
  private final Node target;
  private final int weight;

  public Edge(Node target, int weight) {
    this.target = target;
    this.weight = weight;
  }

  public Node getTarget() {
    return target;
  }

  public int getWeight() {
    return weight;
  }
}

class Graph {
  private final Map<String, Node> nodes = new HashMap<>();

  public Node addNode(String name) {
    Node node = new Node(name);
    nodes.put(name, node);
    return node;
  }

  public void addEdge(String from, String to, int weight) {
    Node fromNode = nodes.get(from);
    Node toNode = nodes.get(to);
    if (fromNode != null && toNode != null) {
      fromNode.addEdge(new Edge(toNode, weight));
    }
  }

  public Node getNode(String name) {
    return nodes.get(name);
  }

  public Collection<Node> getAllNodes() {
    return nodes.values();
  }
}

class Dijkstra {

  static class NodeDistance {
    Node node;
    int distance;

    NodeDistance(Node node, int distance) {
      this.node = node;
      this.distance = distance;
    }
  }

  public static Map<Node, Integer> computeShortestDistances(Node source, Map<Node, Node> previousNodes) {
    Map<Node, Integer> distances = new HashMap<>();
    PriorityQueue<NodeDistance> pq = new PriorityQueue<>(Comparator.comparingInt(nd -> nd.distance));

    for (Node node : previousNodes.keySet()) {
      distances.put(node, Integer.MAX_VALUE);
    }
    distances.put(source, 0);
    pq.add(new NodeDistance(source, 0));

    while (!pq.isEmpty()) {
      NodeDistance current = pq.poll();
      Node currentNode = current.node;

      for (Edge edge : currentNode.getEdges()) {
        int newDist = distances.get(currentNode) + edge.getWeight();
        if (newDist < distances.getOrDefault(edge.getTarget(), Integer.MAX_VALUE)) {
          distances.put(edge.getTarget(), newDist);
          previousNodes.put(edge.getTarget(), currentNode);
          pq.add(new NodeDistance(edge.getTarget(), newDist));
        }
      }
    }
    return distances;
  }

  public static List<Node> getShortestPath(Node target, Map<Node, Node> previousNodes) {
    List<Node> path = new LinkedList<>();
    Node step = target;
    if (!previousNodes.containsKey(step)) {
      return path;
    }
    while (step != null) {
      path.add(0, step);
      step = previousNodes.get(step);
    }
    return path;
  }
}

public class NavigationSystem {
  public static void main(String[] args) {
    Graph graph = new Graph();

    graph.addNode("A");
    graph.addNode("B");
    graph.addNode("C");
    graph.addNode("D");
    graph.addNode("E");

    graph.addEdge("A", "B", 4);
    graph.addEdge("A", "C", 2);
    graph.addEdge("B", "C", 1);
    graph.addEdge("B", "D", 5);
    graph.addEdge("C", "D", 8);
    graph.addEdge("C", "E", 10);
    graph.addEdge("D", "E", 2);

    Node source = graph.getNode("A");
    Node target = graph.getNode("E");

    Map<Node, Node> previousNodes = new HashMap<>();
    for (Node node : graph.getAllNodes()) {
      previousNodes.put(node, null);
    }

    Map<Node, Integer> distances = Dijkstra.computeShortestDistances(source, previousNodes);
    List<Node> path = Dijkstra.getShortestPath(target, previousNodes);

    System.out.println("Shortest distances from node " + source + ":");
    for (Map.Entry<Node, Integer> entry : distances.entrySet()) {
      System.out.println("To " + entry.getKey() + " = " + entry.getValue());
    }

    System.out.println("\nShortest path from " + source + " to " + target + ": " + path);
  }
}
