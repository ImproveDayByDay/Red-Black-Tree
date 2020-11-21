package ldx;

public class RedBlackBST<Key extends Comparable<Key>, Value> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private Node root;

    private class Node {
        private Key key;
        private Value value;
        private Node left, right;
        private boolean color;
        public Node(Key key, Value value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    public RedBlackBST() {

    }

    private Node balance(Node h) {
        if(isRed(h.right) && !isRed(h.left))
            h=rotateLeft(h);
        if(isRed(h.left) && isRed(h.left.left))
            h = rotateRight(h);
        if(isRed(h.left) && isRed(h.right))
            flipColors(h);
        return h;
    }

    private Node moveRedLeft(Node h) {
        flipColors(h);
        if(isRed(h.right.left)) {
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
            flipColors(h);
        }
        return h;
    }

    public void deleteMin() {
        if(!isRed(root.left) && !isRed(root.right)) {
            root.color = RED;
        }
        root = deleteMin(root);
        if(root != null)
            root.color = BLACK;
    }

    private Node deleteMin(Node h) {
        if(h.left == null)
            return null;
        if(!isRed(h.left) && !isRed(h.left.left))
            h = moveRedLeft(h);
        h.left = deleteMin(h.left);
        return balance(h);
    }
    private Node moveRight(Node h) {
        flipColors(h);
        if(isRed(h.left.left)) {
            h=rotateRight(h);
            flipColors(h);
        }
        return h;
    }

    private boolean idRed(Node x) {
        if(x == null)
            return false;
        return x.color == RED;
    }

    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = x.right.color;
        x.right.color = RED;
        return x;
    }

    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = x.left.color;
        x.left.color = RED;
        return x;
    }

    private void flipColors(Node h) {
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    public void put(Key key, Value value) {
        root = put(root, key, value);
        root.color = BLACK;
    }

    private boolean isRed(Node x) {
        if(x == null)
            return false;
        return x.color == RED;
    }

    private Node put(Node h, Key key, Value value) {
        if(h == null)
            return new Node(key, value, RED);
        int cmp = key.compareTo(h.key);
        if(cmp < 0)
            h.left = put(h.left, key, value);
        else if(cmp > 0)
            h.right = put(h.right, key, value);
        else h.value = value;

        if(isRed(h.right) && !isRed(h.left))
            h = rotateLeft(h);
        if(isRed(h.left) && isRed(h.left.left))
            h=rotateRight(h);
        if(isRed(h.left) && isRed(h.right))
            flipColors(h);

        return h;
    }

    public void delete(Key key) {
        if(!isRed(root.left) && !isRed(root.right)) {
            root.color = RED;
        }
        root = delete(root, key);
    }

    public Key min() {
        return min(root).key;
    }

    private Node min(Node x) {
        if(x.left == null)
            return x;
        else
            return min(x.left);
    }

    private Node delete(Node h, Key key) {
        if(key.compareTo(h.key) < 0) {
            if(!isRed(h.left) && !isRed(h.left.left)) {
                h = moveRedLeft(h);
            }
            h.left = delete(h.left, key);
        } else {
            if(isRed(h.left))
                h = rotateRight(h);
            if(key.compareTo(h.key) == 0 && (h.right == null))
                return null;
            if(!isRed(h.right) && !isRed(h.right.left))
                h = moveRight(h);
            if(key.compareTo(h.key) == 0) {
                Node x = min(h.right);
                h.key = x.key;
                h.value = x.value;
                h.right = deleteMin(h.right);
            }
            else h.right = delete(h.right, key);
        }
        return balance(h);
    }
}
