package edu.ser222.m03_02;

/**
 * A binary search tree based implementation of a symbol table.
 * 
 * Completion time: 6 hours
 *
 * @author Borys Banaszkiewicz, Sedgewick, Acuna
 * @version 1.0
 */

import java.util.Collections;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

public class CompletedBST<Key extends Comparable<Key>, Value> implements BST<Key, Value> {
    private Node<Key, Value> root;

    @Override
    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null)
            return 0;
        else
            return x.N;
    }

    @Override
    public Value get(Key key) {
        Node<Key, Value> iter = root;

        while(iter != null) {
            int cmp = key.compareTo(iter.key);

            if (cmp < 0)
                iter = iter.left;
            else if (cmp > 0)
                iter = iter.right;
            else
                return iter.val;
        }

        return null;
    }

    private Value get(Node<Key, Value> x, Key key) {
        // Return value associated with key in the subtree rooted at x;
        // return null if key not present in subtree rooted at x.
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) return get(x.left, key);
        else if (cmp > 0) return get(x.right, key);
        else return x.val;
    }

    @Override
    public void put(Key key, Value val) {
        root = put(root, key, val);
    }

    private Node put(Node<Key, Value> x, Key key, Value val) {
        if (x == null)
            return new Node(key, val, 1);

        int cmp = key.compareTo(x.key);
        if (cmp < 0)
            x.left = put(x.left, key, val);
        else if (cmp > 0)
            x.right = put(x.right, key, val);
        else
            x.val = val;
        x.N = size(x.left) + size(x.right) + 1;

        return x;
    }

    @Override
    public Key min() {
        if(root == null)
            throw new NoSuchElementException();
        return min(root).key;
    }

    private Node<Key, Value> min(Node x) {
        if (x.left == null)
            return x;
        return min(x.left);
    }

    @Override
    public Key max() {
        if(root == null)
            throw new NoSuchElementException();
        return max(root).key;
    }

    private Node<Key, Value> max(Node x) {
    if (x.right == null) return x;
        return max(x.right);
    }

    @Override
    public Key floor(Key key) {
        if(root == null)
            throw new NoSuchElementException();

        Node<Key, Value> x = floor(root, key);
        if (x == null)
            return null;
        return x.key;
    }

    private Node<Key, Value> floor(Node<Key, Value> x, Key key) {
        if (x == null)
            return null;
        int cmp = key.compareTo(x.key);
        if (cmp == 0) return x;
        if (cmp < 0) return floor(x.left, key);
        Node<Key, Value> t = floor(x.right, key);
        if (t != null) return t;
        else return x;
    }

    @Override
    public Key select(int k) {
        return select(root, k).key;
    }

    private Node<Key, Value> select(Node x, int k) {
        if (x == null) return null;
        int t = size(x.left);
        if (t > k) return select(x.left, k);
        else if (t < k) return select(x.right, k-t-1);
        else return x;
    }

    @Override
    public int rank(Key key) {
        return rank(key, root);
    }

    private int rank(Key key, Node<Key, Value> x) {
        // Return number of keys less than x.key in the subtree rooted at x.
        if (x == null) return 0;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) return rank(key, x.left);
        else if (cmp > 0) return 1 + size(x.left) + rank(key, x.right);
        else return size(x.left);
    }

    @Override
    public void deleteMin() {
        if(root == null)
            throw new NoSuchElementException();
        root = deleteMin(root);
    }

    private Node<Key, Value> deleteMin(Node x) {
        if (x.left == null) return x.right;
        x.left = deleteMin(x.left);
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }

    @Override
    public void delete(Key key) {
        root = delete(root, key);
    }

    private Node<Key, Value> delete(Node<Key, Value> x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) x.left = delete(x.left, key);
        else if (cmp > 0) x.right = delete(x.right, key);
        else
        {
            if (x.right == null) return x.left;
            if (x.left == null) return x.right;
            Node t = x;
            x = min(t.right);
            x.right = deleteMin(t.right);
            x.left = t.left;
        }
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }

    @Override
    public Iterable<Key> keys() {
        if (root == null)
            return new LinkedList<>();
        else
            return keys(min(), max());
    }

    @Override
    public Iterable<Key> keys(Key lo, Key hi)
    {
        Queue<Key> queue = new LinkedList<>();
        keys(root, queue, lo, hi);
        return queue;
    }

    private void keys(Node<Key, Value> x, Queue<Key> queue, Key lo, Key hi)
    {
        if (x == null) return;
        int cmplo = lo.compareTo(x.key);
        int cmphi = hi.compareTo(x.key);
        if (cmplo < 0) keys(x.left, queue, lo, hi);
        if (cmplo <= 0 && cmphi >= 0) queue.add(x.key);
        if (cmphi > 0) keys(x.right, queue, lo, hi);
    }

    @Override
    public Key ceiling(Key key) {
        //SKIP, UNNEEDED
        return null;
    }
    
    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public boolean contains(Key key) {
        return get(key) != null;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public void deleteMax()  {
        if (root == null) throw new NoSuchElementException();
        root = deleteMax(root);
    }
    
    private Node<Key, Value> deleteMax(Node x) {
        if (x.right == null) return x.left;
        x.right = deleteMax(x.right);
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }

    @Override
    public int size(Key lo, Key hi) {
        Iterable<Key> keys = keys();
        int count = 0;
        
        for (Key key : keys) {
            int cmp_low = key.compareTo(lo);
            int cmp_high = key.compareTo(hi);
            
            if (cmp_low >=0 && cmp_high <= 0) count++;
        }
        
        return count;
    }

    @Override
    public void putFast(Key key, Value val) {
        Node<Key,Value> curr = root;
        Node<Key,Value> temp = curr;
        
        if (curr == null) {
            root = new Node(key, val, 1);
            return;
        }
        
        while (curr != null) {
            int cmp = key.compareTo(curr.key);
            temp = curr;
            
            if (cmp < 0) {
                curr = curr.left;
            }
            else if (cmp > 0) {
                curr = curr.right;
            }
            else {
                curr.val = val;
                curr.N = size(curr.left) + size(curr.right) + 1;
                return;
            }
        }
        
        if (key.compareTo(temp.key) < 0) {
            temp.left = new Node(key, val, 1);
        }
        else {
            temp.right = new Node(key, val, 1);
        }
    }

    @Override
    public Value getFast(Key key) {
        Node<Key,Value> curr = root;
        
        while (curr != null) {
            int cmp = key.compareTo(curr.key);
            
            if (cmp < 0) {
                curr = curr.left;
            }
            else if (cmp > 0) {
                curr = curr.right;
            }
            else {
                return curr.val;
            }
        }
        return null;
    }

    @Override
    public void balance() {
        LinkedList<Node<Key,Value>> inOrderTraversedList = new LinkedList<>();
        
        inOrder(root, inOrderTraversedList);
        
        Node<Key,Value>[] nodes_arr= new Node[inOrderTraversedList.size()];
        int index = 0;

        for (Node<Key,Value> nodes : inOrderTraversedList) {
            nodes_arr[index++] = nodes;
        }

        root = balance(nodes_arr, 0, inOrderTraversedList.size() - 1);
    }
    
    private void inOrder(Node<Key, Value> node, LinkedList<Node<Key,Value>> list) {
        if (node == null) return;
        inOrder(node.left, list);
        list.add(node);
        inOrder(node.right, list);
    }
    
    private Node<Key, Value> balance(Node<Key, Value>[] nodes, int start, int end) {
        if (start > end) return null;
        
        int mid = start + (end - start) / 2;
        
        Node<Key, Value> new_root = nodes[mid];

        new_root.left = balance(nodes, start, mid - 1);
        new_root.right = balance(nodes, mid + 1, end);
        
        new_root.N = size(new_root.left) + size(new_root.right) + 1;
        
        return new_root;
    }

    @Override
    public String displayLevel(Key key) {
        Queue<Node<Key,Value>> queue = new LinkedList<>();
        StringBuilder result = new StringBuilder();
        
        queue.add(findNode(key));
        
        if (queue.isEmpty() || queue.peek() == null) {
            return "empty";
        }
        
        while (!queue.isEmpty()) {
            int size = queue.size();
            
            for (int i = 0; i < size; i++) {
                Node<Key, Value> curr = queue.poll();
                
                if (curr.left != null) {
                    queue.add(curr.left);
                }
                if (curr.right != null) {
                    queue.add(curr.right);
                }
                
                result.append(curr.val).append(" ");
            }
        }

        return result.toString().trim();
    }

    public Node<Key, Value> findNode(Key key) {
        if (root == null) return null;
        
        Queue<Node<Key,Value>> queue_root = new LinkedList<>();
        
        queue_root.add(root);
        
        while (!queue_root.isEmpty()) {
            int size = queue_root.size();
            
            for (int i = 0; i < size; i++) {
                Node<Key, Value> curr = queue_root.poll();
                
                if (curr.key.compareTo(key) == 0) {
                    return curr;
                }
                
                if (curr.left != null) {
                    queue_root.add(curr.left);
                }
                if (curr.right != null) {
                    queue_root.add(curr.right);
                }
            }
        }
        
        return null;
    }
    /**
     * entry point for testing.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BST<Integer, String> bst = new CompletedBST();
        
        bst.put(10, "TEN");
        bst.put(3, "THREE");
        bst.put(1, "ONE");
        bst.put(5, "FIVE");
        bst.put(2, "TWO");
        bst.put(7, "SEVEN");
        
        System.out.println("Before balance:");
        System.out.println(bst.displayLevel(10)); //root
        
        System.out.println("After balance:");
        bst.balance();
        System.out.println(bst.displayLevel(3)); //root

    }
}