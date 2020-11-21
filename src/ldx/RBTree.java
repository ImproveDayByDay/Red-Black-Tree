package ldx;

import java.util.HashMap;
import java.util.Map;

public class RBTree<Key extends Comparable<Key>> {
    private Node root;

    private static final boolean RED = false;
    private static final boolean BLACK = true;

    private Node TNULL ;
    public class Node{
        boolean color;
        Key key;
        Node left;
        Node right;
        Node parent;
        public Node() {

        }
        public Node(Key key,boolean color,  Node left, Node right, Node parent) {
            this.color = color;
            this.key = key;
            this.left = left;
            this.right = right;
            this.parent = parent;
        }
    }

    public RBTree() {
        TNULL = new Node();
        TNULL.color = BLACK;
        TNULL.left = null;
        TNULL.right = null;
        root = TNULL;
    }

    private void leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;
        if(y.left != TNULL)
            y.left.parent = x;
        y.parent = x.parent;
        if(x.parent != null) {
            if(x.parent.left == x)
                x.parent.left = y;
            else
                x.parent.right = y;
        } else {
            root = y;
        }
        y.left = x;
        x.parent = y;
    }

    public void insert(Key key) {
        Node node = new Node(key, BLACK, TNULL, TNULL, null);
        if(node != null) {
            insert(node);
        }
    }

    private void insert(Node node) {
        Node p = null;
        Node x = root;
        while (x != TNULL) {
            p = x;
            if(node.key.compareTo(x.key)< 0) {
                x = x.left;
            } else {
                x = x.right;
            }
        }
        node.parent = p;
        if(p == null)
            root = node;
        else {
            if(node.key.compareTo(p.key) < 0) {
                p.left = node;
            } else {
                p.right = node;
            }
        }

        node.color = RED;
        insertFixUp(node);
    }

    public boolean isRed(Node x) {
        if( x== TNULL)
            return false;
        return x.color == RED;
    }

    public void insertFixUp(Node z) {
        Node uncle;
        while (z.parent != null && isRed(z.parent)) {
            if(z.parent.parent == z.parent.parent.left) {
                uncle = z.parent.parent.right;
                if(isRed(uncle)) {
                    z.parent.color = BLACK;
                    uncle.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                    continue;
                }

                if(z.parent.right == z) {
                    z = z.parent;
                    leftRotate(z);
                }

                z.parent.parent.color = RED;
                z.parent.color = BLACK;
                rightRotate(z.parent.parent);
            } else {
                uncle = z.parent.parent.left;
                if(isRed(uncle)) {
                    z.parent.color = BLACK;
                    uncle.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                    continue;
                }

                if(z.parent.left == z) {
                    z = z.parent;
                    rightRotate(z);
                }

                z.parent.parent.color = RED;
                z.parent.color = BLACK;
                leftRotate(z.parent.parent);
            }
        }
        root.color = BLACK;
    }
    private void rightRotate(Node y) {
        Node x = y.left;
        y.left = x.right;
        if(x.right != TNULL) {
            x.right.parent = y;
        }
        x.parent = y.parent;
        if(y.parent != null){
            if(y == y.parent.left)
                y.parent.left = x;
            else
                y.parent.right = x;
        } else {
            root = x;
        }

        x.right = y;
        y.parent =  x;
    }

    public void print() {
        print(root);
    }

    public Node minimum(Node node) {
        while (node.left != TNULL) {
            node = node.left;
        }
        return node;
    }

    private void deleteNodeHelper(Node node, Key key) {
        Node z = null;
        Node x, y;
        while (node != TNULL) {
            int cmp = node.key.compareTo(key);
            if(cmp < 0) {
                node = node.right;
            } else if(cmp == 0) {
                z = node;
                break;
            } else {
                node = node.left;
            }
        }
        y = z;
        boolean yOriginalColor = y.color;
        if(z.left == TNULL) {
            x = z.right;
            rbTransplant(z, z.right);
        } else if(z.right == TNULL) {
            x = z.left;
            rbTransplant(z, z.left);
        } else {
            y = minimum(z.right);
            yOriginalColor = y.color;
            x = y.right;
            if(y.parent == z) {
                x.parent = y;
            } else {
                rbTransplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            rbTransplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }
        if(yOriginalColor == BLACK){
            fixDelete(x);
        }
    }

    private void fixDelete(Node node) {
        while (true) {
            if(node == root) {
                root.color = BLACK;
                break;
            }
            if(node == node.parent.left) {
                if(isRed(node.parent)) {
                    node.parent.color = BLACK;
                    node.parent.right.color = RED;
                    node.color = BLACK;
                    break;
                }
                if(!isRed(node.parent.right) && isRed(node.parent.right.right)) {
                    leftRotate(node.parent);
                    node.parent.parent.color = node.parent.color;
                    node.parent.parent.right.color = BLACK;
                    node.parent.color = BLACK;
                    node.color = BLACK;
                    break;
                }
                if(!isRed(node.parent.right) && !isRed(node.parent.right.right) && !isRed(node.parent.right.left)) {
                    node.parent.right.color = RED;
                    node = node.parent;
                    continue;
                }
                if(isRed(node.parent.right)) {
                    leftRotate(node.parent);
                    node.parent.color = RED;
                    node.parent.parent.color = BLACK;
                    continue;
                }
                 if(!isRed(node.parent.right) && isRed(node.parent.right.left) && isRed(node.parent.right.right)) {
                     rightRotate(node.parent.right);
                     node.parent.right.color = BLACK;
                     node.parent.right.right.color = RED;
                     continue;
                 }
            } else {
                if(isRed(node.parent)) {
                    node.parent.color = BLACK;
                    node.parent.left.color = RED;
                    node.color = BLACK;
                    break;
                }
                if(!isRed(node.parent.left) && isRed(node.parent.left.left)) {
                    rightRotate(node.parent);
                    node.parent.parent.color = node.parent.color;
                    node.parent.parent.left.color = BLACK;
                    node.parent.color = BLACK;
                    node.color = BLACK;
                    break;
                }
                if(!isRed(node.parent.left) && !isRed(node.parent.left.left) && !isRed(node.parent.left.right)) {
                    node.parent.left.color = RED;
                    node = node.parent;
                    continue;
                }
                if(isRed(node.parent.left)) {
                    rightRotate(node.parent);
                    node.parent.color = RED;
                    node.parent.parent.color = BLACK;
                    continue;
                }
                if(!isRed(node.parent.left) && isRed(node.parent.left.right) && isRed(node.parent.left.left)) {
                    leftRotate(node.parent.left);
                    node.parent.left.color = BLACK;
                    node.parent.left.left.color = RED;
                    continue;
                }
            }
        }
    }

    public void rbTransplant(Node u, Node v) {
        if(u.parent == null) {
            root = v;
        } else if(u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        v.parent = u.parent;
    }
    public void delete(Key key) {
        deleteNodeHelper(root, key);
    }
    private void print(Node node) {
        if(node.left != TNULL)
            print(node.left);
        System.out.println(node.key +"=====>"+ node.color);
        if(node.right != TNULL)
            print(node.right);
    }
    public static void main(String[] args) {
        /*RBTree<Integer> rbTree = new RBTree<>();
        for(int i=1; i<=10; i++) {
            rbTree.insert(i);
        }
        rbTree.delete(4);
        rbTree.delete(7);
        rbTree.delete(8);
        rbTree.print();*/
        Map<String, Integer> map = new HashMap<>(22);
        map.put(null,2);
        map.put("a", 1);
        map.put("a", 2);
        System.out.println(map.get(null));
    }
}
