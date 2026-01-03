import java.util.HashMap;
import java.util.Map;

class LRUCache {

  private class Node {
    int chatId;
    String message;
    Node prev, next;

    Node(int chatId, String message) {
      this.chatId = chatId;
      this.message = message;
    }
  }

  private final int capacity;
  private final Map<Integer, Node> cache;
  private final Node head, tail;

  public LRUCache(int capacity) {
    this.capacity = capacity;
    this.cache = new HashMap<>();

    head = new Node(-1, "");
    tail = new Node(-1, "");
    head.next = tail;
    tail.prev = head;
  }

  private void addToFront(Node node) {
    node.next = head.next;
    node.prev = head;
    head.next.prev = node;
    head.next = node;
  }

  private void removeNode(Node node) {
    node.prev.next = node.next;
    node.next.prev = node.prev;
  }

  private void moveToFront(Node node) {
    removeNode(node);
    addToFront(node);
  }

  private Node removeLRU() {
    Node lru = tail.prev;
    removeNode(lru);
    return lru;
  }

  public String get(int chatId) {
    if (!cache.containsKey(chatId)) {
      return null;
    }
    Node node = cache.get(chatId);
    moveToFront(node);
    return node.message;
  }

  public void put(int chatId, String message) {
    if (cache.containsKey(chatId)) {
      Node node = cache.get(chatId);
      node.message = message;
      moveToFront(node);
    } else {
      if (cache.size() == capacity) {
        Node lru = removeLRU();
        cache.remove(lru.chatId);
      }
      Node newNode = new Node(chatId, message);
      cache.put(chatId, newNode);
      addToFront(newNode);
    }
  }

  public void displayActiveChats() {
    Node curr = head.next;
    System.out.print("Active Chats (Most → Least Recent): ");
    while (curr != tail) {
      System.out.print(curr.chatId + " ");
      curr = curr.next;
    }
    System.out.println();
  }
}

public class ChatActivityMonitor {

  public static void main(String[] args) {
    LRUCache chatCache = new LRUCache(3);

    chatCache.put(101, "Hello!");
    chatCache.put(102, "Hi there");
    chatCache.put(103, "Good morning");

    chatCache.displayActiveChats();

    chatCache.get(101);
    chatCache.displayActiveChats();

    chatCache.put(104, "New chat started");
    chatCache.displayActiveChats();
  }
}