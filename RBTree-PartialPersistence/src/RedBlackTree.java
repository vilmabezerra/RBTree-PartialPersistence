import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//import TreePrinter.PrintableNode;

/*RedBlack Tree code was taken from CodeByte website, which code I tested and works properly
 * 
 * link: http://www.codebytes.in/2014/10/red-black-tree-java-implementation.html
 * 
 * My intention here is to apply partial persistence to this data structure
 * 
 * */
public class RedBlackTree{

    private final int RED = 0;
    private final static int BLACK = 1;

    /* 
     * Tree Node class
     * */
    public static class RedBlackNode {
        
	    	int key = -1, color = BLACK;
	    RedBlackNode left = nil, right = nil, parent = nil;
	        
	
	    	RedBlackNode(int key) {
	        this.key = key;
	    } 
    }

    private final static RedBlackNode nil = new RedBlackNode(-1); 
    private RedBlackNode root = nil;
	private Scanner scan;
    

    /**
     * Binary tree printer
     * 
     * * @param root
     *            tree root node
     * 
     * @author MightyPork
     */
    public void printTree(RedBlackNode root) {

     	List<List<String>> lines = new ArrayList<List<String>>();

        List<RedBlackNode> level = new ArrayList<RedBlackNode>();
        List<RedBlackNode> next = new ArrayList<RedBlackNode>();

        level.add(root);
        int nn = 1;

        int widest = 0;

        while (nn != 0) {
            List<String> line = new ArrayList<String>();

            nn = 0;

            for (RedBlackNode n : level) {
                if (n == null) {
                    line.add(null);

                    next.add(null);
                    next.add(null);
                } else {
                    String aa = nodeKeyString(n.key) + ":"+ nodeColorString(n.color);
                    line.add(aa);
                    if (aa.length() > widest) widest = aa.length();

                    next.add(n.left);
                    next.add(n.right);

                    if (n.left != nil) nn++;
                    if (n.right != nil) nn++;
                }
            }

            if (widest % 2 == 1) widest++;

            lines.add(line);

            List<RedBlackNode> tmp = level;
            level = next;
            next = tmp;
            next.clear();
        }

        int perpiece = lines.get(lines.size() - 1).size() * 4;
        for (int i = 0; i < lines.size(); i++) {
            List<String> line = lines.get(i);
            int hpw = (int) Math.floor(perpiece / 2f) - 1;

            if (i > 0) {
                for (int j = 0; j < line.size(); j++) {

                    // split node
                    char c = ' ';
                    if (j % 2 == 1) {
                        if (line.get(j - 1) != null) {
                            c = (line.get(j) != null) ? '┴' : '┘';
                        } else {
                            if (j < line.size() && line.get(j) != null) c = '└';
                        }
                    }
                    System.out.print(c);

                    // lines and spaces
                    if (line.get(j) == null) {
                        for (int k = 0; k < perpiece - 1; k++) {
                            System.out.print(" ");
                        }
                    } else {

                        for (int k = 0; k < hpw; k++) {
                            System.out.print(j % 2 == 0 ? " " : "─");
                        }
                        System.out.print(j % 2 == 0 ? "┌" : "┐");
                        for (int k = 0; k < hpw; k++) {
                            System.out.print(j % 2 == 0 ? "─" : " ");
                        }
                    }
                }
                System.out.println();
            }

            // print line of numbers
            for (int j = 0; j < line.size(); j++) {

                String f = line.get(j);
                if (f == null) f = "";
                int gap1 = (int) Math.ceil(perpiece / 2f - f.length() / 2f);
                int gap2 = (int) Math.floor(perpiece / 2f - f.length() / 2f);

                // a number
                for (int k = 0; k < gap1; k++) {
                    System.out.print(" ");
                }
                System.out.print(f);
                for (int k = 0; k < gap2; k++) {
                    System.out.print(" ");
                }
            }
            System.out.println();

            perpiece /= 2;
          }
    }

    private String nodeColorString(int color) {
		String col = null;
		if (color == 0) {
			col = "\u001b[1;31mR\u001b[0m";
		}else {
			if (color == 1) {
				col = "B";
			}
		}
		return col;
	}
    
    private String nodeKeyString(int key) {
    		String k = null;
    		if(key == -1) {
    			k = "LEAF";
    		}else {
    			k = Integer.toString(key);
    		}
    		return k;
    }

	private RedBlackNode findNode(RedBlackNode findNode, RedBlackNode node) {
        if (root == nil) {
            return null;
        }

        if (findNode.key < node.key) {
            if (node.left != nil) {
                return findNode(findNode, node.left);
            }
        } else if (findNode.key > node.key) {
            if (node.right != nil) {
                return findNode(findNode, node.right);
            }
        } else if (findNode.key == node.key) {
            return node;
        }
        return null;
    }

    private void insert(RedBlackNode node) {
    		RedBlackNode temp = root;
        if (root == nil) {
            root = node;
            node.color = BLACK;
            node.parent = nil;
        } else {
            node.color = RED;
            while (true) {
                if (node.key < temp.key) {
                    if (temp.left == nil) {
                        temp.left = node;
                        node.parent = temp;
                        break;
                    } else {
                        temp = temp.left;
                    }
                } else if (node.key >= temp.key) {
                    if (temp.right == nil) {
                        temp.right = node;
                        node.parent = temp;
                        break;
                    } else {
                        temp = temp.right;
                    }
                }
            }
            fixTree(node);
        }
    }

    //Takes as argument the newly inserted node
    private void fixTree(RedBlackNode node) {
        while (node.parent.color == RED) {
        		RedBlackNode uncle = nil;
            if (node.parent == node.parent.parent.left) {
                uncle = node.parent.parent.right;

                if (uncle != nil && uncle.color == RED) {
                    node.parent.color = BLACK;
                    uncle.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;
                    continue;
                } 
                if (node == node.parent.right) {
                    //Double rotation needed
                    node = node.parent;
                    rotateLeft(node);
                } 
                node.parent.color = BLACK;
                node.parent.parent.color = RED;
                //if the "else if" code hasn't executed, this
                //is a case where we only need a single rotation 
                rotateRight(node.parent.parent);
            } else {
                uncle = node.parent.parent.left;
                 if (uncle != nil && uncle.color == RED) {
                    node.parent.color = BLACK;
                    uncle.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;
                    continue;
                }
                if (node == node.parent.left) {
                    //Double rotation needed
                    node = node.parent;
                    rotateRight(node);
                }
                node.parent.color = BLACK;
                node.parent.parent.color = RED;
                //if the "else if" code hasn't executed, this
                //is a case where we only need a single rotation
                rotateLeft(node.parent.parent);
            }
        }
        root.color = BLACK;
    }

    void rotateLeft(RedBlackNode node) {
        if (node.parent != nil) {
            if (node == node.parent.left) {
                node.parent.left = node.right;
            } else {
                node.parent.right = node.right;
            }
            node.right.parent = node.parent;
            node.parent = node.right;
            if (node.right.left != nil) {
                node.right.left.parent = node;
            }
            node.right = node.right.left;
            node.parent.left = node;
        } else {//Need to rotate root
        		RedBlackNode right = root.right;
            root.right = right.left;
            right.left.parent = root;
            root.parent = right;
            right.left = root;
            right.parent = nil;
            root = right;
        }
    }

    void rotateRight(RedBlackNode node) {
        if (node.parent != nil) {
            if (node == node.parent.left) {
                node.parent.left = node.left;
            } else {
                node.parent.right = node.left;
            }

            node.left.parent = node.parent;
            node.parent = node.left;
            if (node.left.right != nil) {
                node.left.right.parent = node;
            }
            node.left = node.left.right;
            node.parent.right = node;
        } else {//Need to rotate root
        		RedBlackNode left = root.left;
            root.left = root.left.right;
            left.right.parent = root;
            root.parent = left;
            left.right = root;
            left.parent = nil;
            root = left;
        }
    }

    //Deletes whole tree
    void deleteTree(){
        root = nil;
    }
    
    //Deletion Code .
    
    //This operation doesn't care about the new Node's connections
    //with previous node's left and right. The caller has to take care
    //of that.
    void transplant(RedBlackNode target, RedBlackNode with){ 
          if(target.parent == nil){
              root = with;
          }else if(target == target.parent.left){
              target.parent.left = with;
          }else
              target.parent.right = with;
          with.parent = target.parent;
    }
    
    boolean delete(RedBlackNode z){
        if((z = findNode(z, root))==null)return false;
        RedBlackNode x;
        RedBlackNode y = z; // temporary reference y
        int y_original_color = y.color;
        
        if(z.left == nil){
            x = z.right;  
            transplant(z, z.right);  
        }else if(z.right == nil){
            x = z.left;
            transplant(z, z.left); 
        }else{
            y = treeMinimum(z.right);
            y_original_color = y.color;
            x = y.right;
            if(y.parent == z)
                x.parent = y;
            else{
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color; 
        }
        if(y_original_color==BLACK)
            deleteFixup(x);  
        return true;
    }
    
    void deleteFixup(RedBlackNode x){
        while(x!=root && x.color == BLACK){ 
            if(x == x.parent.left){
            		RedBlackNode w = x.parent.right;
                if(w.color == RED){
                    w.color = BLACK;
                    x.parent.color = RED;
                    rotateLeft(x.parent);
                    w = x.parent.right;
                }
                if(w.left.color == BLACK && w.right.color == BLACK){
                    w.color = RED;
                    x = x.parent;
                    continue;
                }
                else if(w.right.color == BLACK){
                    w.left.color = BLACK;
                    w.color = RED;
                    rotateRight(w);
                    w = x.parent.right;
                }
                if(w.right.color == RED){
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.right.color = BLACK;
                    rotateLeft(x.parent);
                    x = root;
                }
            }else{
            		RedBlackNode w = x.parent.left;
                if(w.color == RED){
                    w.color = BLACK;
                    x.parent.color = RED;
                    rotateRight(x.parent);
                    w = x.parent.left;
                }
                if(w.right.color == BLACK && w.left.color == BLACK){
                    w.color = RED;
                    x = x.parent;
                    continue;
                }
                else if(w.left.color == BLACK){
                    w.right.color = BLACK;
                    w.color = RED;
                    rotateLeft(w);
                    w = x.parent.left;
                }
                if(w.left.color == RED){
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.left.color = BLACK;
                    rotateRight(x.parent);
                    x = root;
                }
            }
        }
        x.color = BLACK; 
    }
    
    RedBlackNode treeMinimum(RedBlackNode subTreeRoot){
        while(subTreeRoot.left!=nil){
            subTreeRoot = subTreeRoot.left;
        }
        return subTreeRoot;
    }
    
    public void consoleUI() {
        scan = new Scanner(System.in);
        while (true) {
            System.out.println("\n1. Add 2r items\n"
                    + "2. Delete r items\n"
                    + "3. Add items\n"
                    + "4. Delete items\n"
                    + "5. Check items\n"
                    + "6. Print tree\n"
                    + "7. Delete tree\n"
                    + "\u001b[1;31m-999 to Exit a Command\u001b[0m");
            int choice = scan.nextInt();

            int item;
            	RedBlackNode node;
            switch (choice) {
                case 3:
                    item = scan.nextInt();
                    while (item != -999) {
                        node = new RedBlackNode(item);
                        insert(node);
                        item = scan.nextInt();
                    }
                    printTree(root);
                    break;
                case 4:
                    item = scan.nextInt();
                    while (item != -999) {
                        node = new RedBlackNode(item);
                        System.out.print("\nDeleting item " + item);
                        if (delete(node)) {
                            System.out.print(": deleted!");
                        } else {
                            System.out.print(": does not exist!");
                        }
                        item = scan.nextInt();
                    }
                    System.out.println();
                    printTree(root);
                    break;
                case 5:
                    item = scan.nextInt();
                    while (item != -999) {
                        node = new RedBlackNode(item);
                        System.out.println((findNode(node, root) != null) ? "found" : "not found");
                        item = scan.nextInt();
                    }
                    break;
                case 6:
                    printTree(root);
                    break;
                case 7:
                    deleteTree();
                    System.out.println("Tree deleted!");
                    break;
            }
        }
    }
    public static void main(String[] args) {
        RedBlackTree rbt = new RedBlackTree();
        rbt.consoleUI();
    }
}

