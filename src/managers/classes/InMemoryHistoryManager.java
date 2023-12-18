package managers.classes;

import managers.interfaces.HistoryManager;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList history = new CustomLinkedList();

    @Override
    public void add(Task task) {
        history.linkLast(task);
    }

    @Override
    public void remove(int id) {
        history.removeNode(id);
    }

    @Override
    public List<Task> getHistory() {
        return history.getTasks();
    }

    private static class CustomLinkedList {
        private static class Node {
            Task task;
            Node prev;
            Node next;

            Node(Task task) {
                this.task = task;
            }
        }
        private Node head;
        private Node tail;
        final private Map<Integer, Node> idToNode = new HashMap<>();

        public void linkLast(Task task) {
            if (idToNode.containsKey(task.getID())) {
                removeNode(task.getID());
            }

            Node newNode = new Node(task);
            if (head == null) {
                head = newNode;
            } else {
                tail.next = newNode;
                newNode.prev = tail;
            }
            tail = newNode;
            idToNode.put(task.getID(), newNode);
        }

        void removeNode(int id) {
            Node nodeToRemove = idToNode.get(id);
            if (nodeToRemove == null) {
                return;
            }

            if (nodeToRemove.prev != null) {
                nodeToRemove.prev.next = nodeToRemove.next;
            } else {
                head = nodeToRemove.next;
            }

            if (nodeToRemove.next != null) {
                nodeToRemove.next.prev = nodeToRemove.prev;
            } else {
                tail = nodeToRemove.prev;
            }

            idToNode.remove(id);
        }

        private List<Task> getTasks() {
            List<Task> tasks = new ArrayList<>();
            Node current = head;
            while (current != null) {
                tasks.add(current.task);
                current = current.next;
            }
            return  tasks;
        }
    }
}
