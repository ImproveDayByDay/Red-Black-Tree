package ldx;

public class MyRBTree <Key extends Comparable<Key>>{
    Node root;
    Node NULL;
    private final static boolean RED = true;
    private final static boolean BLACK = false;

    public class Node {
        private Node left,right,parent;
        private Key key;
        boolean color;
        public Node (Key key, Node left, Node right, Node parent, boolean color) {
            this.key = key;
            this.left = left;
            this.right = right;
            this.color = color;
            this.parent = parent;
        }
    }

    public MyRBTree() {
        NULL = new Node(null, null, null, null, BLACK);
        root = NULL;
    }

    private void leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.parent = x.parent;
        if(y.left != NULL) {
            y.left.parent = x;
        }
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

    private void rightRotate(Node y) {
        Node x = y.left;
        y.left = x.right;
        if(x.right != NULL)
            x.right.parent = y;
        x.parent = y.parent;
        if(y.parent != null) {
            if(y.parent.left == y)
                y.parent.left = x;
            else
                y.parent.right = x;
        } else {
            root = x;
        }

        x.right = y;
        y.parent = x;
    }

    public void insert(Key key) {
        Node node = new Node(key, NULL, NULL, null, RED);
        root = insert(node);
    }

    private Node insert( Node node) {
        Node p = null;
        Node z = root;
        while (z != NULL) {
            p = z;
            int cmp = node.key.compareTo(z.key);
            if(cmp < 0) {
                z = z.left;
            } else if(cmp > 0) {
                z = z.right;
            }
        }

        if(p == null) {
            root = node;
        } else {
            int cmp = node.key.compareTo(p.key);
            if(cmp < 0) {
                p.left = node;
                node.parent = p;
            } else {
                p.right = node;
                node.parent = p;
            }
        }

        fixInsert(node);
        return root;
    }

    private boolean isRed(Node node) {
        if(node == NULL)
            return false;
        return node.color == RED;
    }

    private void fixInsert(Node node) {
        Node z = node;
        while (z.parent != null && isRed(z.parent)) {
            if(z.parent == z.parent.parent.left) {
                if(isRed(z.parent.parent.right)) {
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    z.parent.parent.right.color = BLACK;
                    z = z.parent.parent;
                    continue;
                }

                if(z == z.parent.right) {
                    z = z.parent;
                    leftRotate(z);
                }
                rightRotate(z.parent.parent);
                z.parent.color = BLACK;
                z.parent.right.color = RED;
            } else {
                if(isRed(z.parent.parent.left)) {
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    z.parent.parent.left.color = BLACK;
                    z = z.parent.parent;
                    continue;
                }

                if(z == z.parent.left) {
                    z = z.parent;
                    rightRotate(z);
                }
                leftRotate(z.parent.parent);
                z.parent.color = BLACK;
                z.parent.left.color = RED;
            }
        }
        root.color = BLACK;
    }
    private void print(Node node) {
        if(node.left != NULL)
            print(node.left);
        int k = node.color ? 1 : 0;
        System.out.println(node.key +"=====>"+ k);
        if(node.right != NULL)
            print(node.right);
    }
    public void delete(Key key) {
         delete(root, key);
    }
    private void rbPlant(Node x, Node y) {
        if(x.parent == null)
            root = y;
        else if(x.parent.left == x)
            x.parent.left = y;
        else
            x.parent.right = y;
        y.parent = x.parent;
    }
    private Node minimum(Node node) {
        while (node.left != NULL) {
            node = node.left;
        }
        return node;
    }
    public void delete(Node node, Key key) {
        Node z = node;
        while (true) {
            int cmp = key.compareTo(z.key);
            if(cmp < 0) {
                z = z.left;
            } else if(cmp == 0){
                break;
            } else {
                z = z.right;
            }
        }
        Node x;
        boolean color =z.color;

        if(z.right == NULL) {
            x = z;
            rbPlant(z, z.left);
        } else if(z.left == NULL) {
            x = z;
            rbPlant(z, z.right);
        } else {
            Node min = minimum(z.right);
            color = min.color;
            x = min.right;
            if(min.parent != z){
                rbPlant(min, min.right);
                min.right = z.right;
                min.right.parent = min;
            }
            rbPlant(z, min);
            min.color = z.color;
            min.left = z.left;
            min.left.parent = min;
        }
        if(color == BLACK) {
            fixDelete(x);
        }

    }
    public void fixDelete(Node node) {
        while (true) {
            if(node == root) {
                node.color = BLACK;
                break;
            }
            if(node.parent.left == node) {
                if(!isRed(node.parent.right) && isRed(node.parent.right.right)) {
                    leftRotate(node.parent);
                    node.parent.parent.color = node.parent.color;
                    node.parent.color = BLACK;
                    node.parent.parent.right.color = BLACK;
                    node.color = BLACK;
                    break;
                }
                if(isRed(node.parent) && !isRed(node.parent.right) && !isRed(node.parent.right.right) && !isRed(node.parent.right.left)) {
                    node.color = BLACK;
                    node.parent.color = BLACK;
                    node.parent.right.color = RED;
                    break;
                }
                if(isRed(node.parent) && isRed(node.parent.right) && isRed(node.parent.right.right) && isRed(node.parent.right.left)) {
                    node.parent.right.color = RED;
                    node.color = BLACK;
                    node = node.parent;
                    continue;
                }
                if(isRed(node.parent.right)) {
                    leftRotate(node.parent);
                    node.parent.color = RED;
                    node.parent.parent.color = BLACK;
                    continue;
                }
                if(!isRed(node.parent.right) && isRed(node.parent.right.left)) {
                    rightRotate(node.parent.right);
                    node.parent.right.color = BLACK;
                    node.parent.right.right.color = RED;
                    continue;
                }
            } else {
                if(!isRed(node.parent.left) && isRed(node.parent.left.left)) {
                    leftRotate(node.parent);
                    node.parent.parent.color = node.parent.color;
                    node.parent.color = BLACK;
                    node.parent.parent.left.color = BLACK;
                    node.color = BLACK;
                    break;
                }
                if(isRed(node.parent) && !isRed(node.parent.left) && !isRed(node.parent.left.left) && !isRed(node.parent.left.right)) {
                    node.color = BLACK;
                    node.parent.color = BLACK;
                    node.parent.left.color = RED;
                    break;
                }
                if(isRed(node.parent) && isRed(node.parent.left) && isRed(node.parent.left.left) && isRed(node.parent.left.right)) {
                    node.parent.left.color = RED;
                    node.color = BLACK;
                    node = node.parent;
                    continue;
                }
                if(isRed(node.parent.left)) {
                    rightRotate(node.parent);
                    node.parent.color = RED;
                    node.parent.parent.color = BLACK;
                    continue;
                }
                if(!isRed(node.parent.left) && isRed(node.parent.left.right)) {
                    leftRotate(node.parent.left);
                    node.parent.left.color = BLACK;
                    node.parent.left.left.color = RED;
                    continue;
                }
            }
        }
    }

    public static void main(String[] args) {
        MyRBTree<Integer> myRBTree = new MyRBTree<>();
        myRBTree.insert(5);
        myRBTree.insert(7);
        myRBTree.insert(9);
        myRBTree.insert(11);
        myRBTree.insert(3);
        myRBTree.insert(4);
        myRBTree.insert(6);
        myRBTree.insert(15);
        myRBTree.delete(7);
        myRBTree.print(myRBTree.root);
        int kk = 1;
    }


}
