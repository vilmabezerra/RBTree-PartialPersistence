import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
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
    private static class RedBlackNode {
        
	    	private int key = -1, color = BLACK;
	    private RedBlackNode left = nil, right = nil, parent = nil;
	    private Map<Long, RedBlackNode> versions = new HashMap<Long, RedBlackNode>();
	        
	
	    	RedBlackNode(int key) {
	        this.key = key;
	        addNodeVersion();
	    } 
	    	/*
	    	 * Setters that shall set node attribute as well as 
	    	 * add a new node version for each time one of them is called
	    	 * 
	    	 * */
	    	
	    	public void setColor(int color) {
	    		this.color = color;
	    		addNodeVersion();
	    	}
	    	
	    public void setLeft(RedBlackNode node) {
	    		this.left = node;
	    		addNodeVersion();
	    	}
	    	
	    public void setRight(RedBlackNode node) {
	    		this.right = node;
	    		addNodeVersion();
	    	}
	    	
	    	
	    	/*
	    	 * Add new version of this node
	    	 * 
	    	 * */
	    private void	addNodeVersion() {
	    		Long msec = System.nanoTime();
	    		versions.put(msec, this);
	    		System.out.println("added " + this.key + " version msec:"+ msec + " Node:"+ versions.get(msec).key);
	    }
    }

    private final static RedBlackNode nil = new RedBlackNode(-1); 
    private RedBlackNode root = nil;
	private Scanner scan;
	private Map<Long, RedBlackNode> rbVersions = new HashMap<Long, RedBlackNode>();
    

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
			col = "R";
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
            node.setColor(BLACK);
            node.parent = nil;
        } else {
            node.setColor(RED);
            while (true) {
                if (node.key < temp.key) {
                    if (temp.left == nil) {
                        temp.setLeft(node);
                        node.parent = temp;
                        break;
                    } else {
                        temp = temp.left;
                    }
                } else if (node.key >= temp.key) {
                    if (temp.right == nil) {
                        temp.setRight(node);
                        node.parent = temp;
                        break;
                    } else {
                        temp = temp.right;
                    }
                }
            }
            fixTree(node);
        }
        
        addTreeVersion(root);
    }

    /*
     * Add new version of tree on Map. 
     * 
     * It is used in insert and remove items methods
     * */
    private void addTreeVersion(RedBlackNode root) {
    		Long msec = System.nanoTime();
    		
		rbVersions.put(msec, root);
		
		System.out.println("Tree msec:"+ msec + " Root:"+ rbVersions.get(msec).key);
	}

	//Takes as argument the newly inserted node
    private void fixTree(RedBlackNode node) {
        while (node.parent.color == RED) {
        		RedBlackNode uncle = nil;
            if (node.parent == node.parent.parent.left) {
            	
                uncle= node.parent.parent.right;

                if (uncle != nil && uncle.color == RED) {
                    node.parent.setColor(BLACK);
                    uncle.setColor(BLACK);
                    node.parent.parent.setColor(RED);
                    node = node.parent.parent;
                    continue;
                } 
                if (node == node.parent.right) {
                    //Double rotation needed
                    node = node.parent;
                    rotateLeft(node);
                } 
                node.parent.setColor(BLACK);
                node.parent.parent.setColor(RED);
                
                //if the "else if" code hasn't executed, this
                //is a case where we only need a single rotation 
                rotateRight(node.parent.parent);
            } else {
                uncle = node.parent.parent.left;
                 if (uncle != nil && uncle.color == RED) {
                    node.parent.setColor(BLACK);
                    uncle.setColor(BLACK);
                    node.parent.parent.setColor(RED);
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
                node.parent.setLeft(node.right);
            } else {
                node.parent.setRight(node.right);
            }
            node.right.parent = node.parent;
            node.parent = node.right;
            if (node.right.left != nil) {
                node.right.left.parent = node;
            }
            node.setRight(node.right.left);
            node.parent.setLeft(node);
        } else {//Need to rotate root
        		RedBlackNode right = root.right;
            root.setRight(right.left);
            right.left.parent = root;
            root.parent = right;
            right.setLeft(root);
            right.parent = nil;
            root = right;
        }
    }

    void rotateRight(RedBlackNode node) {
        if (node.parent != nil) {
            if (node == node.parent.left) {
                node.parent.setLeft(node.left);
            } else {
                node.parent.setRight(node.left);
            }

            node.left.parent = node.parent;
            node.parent = node.left;
            if (node.left.right != nil) {
                node.left.right.parent = node;
            }
            node.setLeft(node.left.right);
            node.parent.setRight(node);
        } else {//Need to rotate root
        		RedBlackNode left = root.left;
            root.setLeft(root.left.right);
            left.right.parent = root;
            root.parent = left;
            left.setRight(root);
            left.parent = nil;
            root = left;
        }
    }

    //Deletes whole tree
    void deleteTree(){
        root = nil;
        addTreeVersion(root);
    }
    
    //Deletion Code .
    
    //This operation doesn't care about the new Node's connections
    //with previous node's left and right. The caller has to take care
    //of that.
    void transplant(RedBlackNode target, RedBlackNode with){ 
          if(target.parent == nil){
              root = with;
          }else if(target == target.parent.left){
              target.parent.setLeft(with);
          }else
              target.parent.setRight(with);
          with.parent = target.parent;
    }
    
    /*
     * Delete a node. 
     * 
     * Returns true when successfully deleted and false when it does not exist
     * 
     * */
    boolean delete(RedBlackNode z){
        if((z = findNode(z, root)) == null) return false;
        
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
                y.setRight(z.right);
                y.right.parent = y;
            }
            transplant(z, y);
            y.setLeft(z.left);
            y.left.parent = y;
            y.setColor(z.color); 
        }
        
        if(y_original_color==BLACK)
            deleteFixup(x); 
        
        addTreeVersion(root);
        return true;
    }
    
    void deleteFixup(RedBlackNode x){
        while(x!=root && x.color == BLACK){ 
            if(x == x.parent.left){
            		RedBlackNode w = x.parent.right;
                if(w.color == RED){
                    w.setColor(BLACK);
                    x.parent.setColor(RED);
                    rotateLeft(x.parent);
                    w = x.parent.right;
                }
                if(w.left.color == BLACK && w.right.color == BLACK){
                    w.setColor(RED);
                    x = x.parent;
                    continue;
                }
                else if(w.right.color == BLACK){
                    w.left.setColor(BLACK);
                    w.setColor(RED);
                    rotateRight(w);
                    w = x.parent.right;
                }
                if(w.right.color == RED){
                    w.setColor(x.parent.color);
                    x.parent.setColor(BLACK);
                    w.right.setColor(BLACK);
                    rotateLeft(x.parent);
                    x = root;
                }
            }else{
            		RedBlackNode w = x.parent.left;
                if(w.color == RED){
                    w.setColor(BLACK);
                    x.parent.setColor(RED);
                    rotateRight(x.parent);
                    w = x.parent.left;
                }
                if(w.right.color == BLACK && w.left.color == BLACK){
                    w.setColor(RED);
                    x = x.parent;
                    continue;
                }
                else if(w.left.color == BLACK){
                    w.right.setColor(BLACK);
                    w.setColor(RED);
                    rotateLeft(w);
                    w = x.parent.left;
                }
                if(w.left.color == RED){
                    w.setColor(x.parent.color);
                    x.parent.setColor(BLACK);
                    w.left.setColor(BLACK);
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
    
    private void addNodesRandomly(int r) {
    		Random random = new Random();
    		int max = 1000, min = -998, value = 0;
    		RedBlackNode node;
    		
		while(r > 0) {
			value = random.nextInt(max + 1 -min) + min; 
			node =  new RedBlackNode(value);
			insert(node);
			r--;
		}
	}
    
    
    private void deleteNodesRandomly(int half) {
    		int max = 0, value = 0;
    		Random random = new Random();
    		RedBlackNode node;
    		
    		while(half > 0) {
    			max = countNodes(root);
    			value = 0;
    			
    			List<RedBlackNode> nodes = new ArrayList<RedBlackNode>();
    			nodes = getNodes(root, nodes);
    			
    			
    			value = random.nextInt(max) + 1; 
    			node =  nodes.get(value);
    			System.out.println("Deleting "+ node.key);
    			delete(node);
    			half--;
    		}
    		
	}
    
    private int countNodes(RedBlackNode node) {
        int total = 0;
        if (node == null) {
            return 0;
        }
        total += countNodes(node.left);
        total += countNodes(node.right);

        if(node != nil){
            total++;
        }
        return total;
    }
    
    private List<RedBlackNode> getNodes(RedBlackNode node, List<RedBlackNode> nodes) {      
        if (node == nil) {
            return nodes;
        }
        getNodes(node.left, nodes);
        getNodes(node.right, nodes);

        if(node != nil){
            nodes.add(node);
        }
        return nodes;
    }
    
    
    public void consoleUI() {
        scan = new Scanner(System.in);
        while (true) {
            System.out.println("\n1. Add R random items\n"
                    + "2. Delete half of the nodes randomly\n"
                    + "3. Add items\n"
                    + "4. Delete items\n"
                    + "5. Check items\n"
                    + "6. Print tree\n"
                    + "7. Delete tree\n"
                    + "8. List Tree Versions\n"
                    + "9. Print Tree Version\n"
                    + "\u001b[1;31m-999 to Exit Commands 3, 4 and 5\u001b[0m");
            int choice = scan.nextInt();

            int item;
            long version;
            	RedBlackNode node;
            switch (choice) {
            		case 1:
            			int R = scan.nextInt();
            			addNodesRandomly(R);
            			printTree(root);
            			break;
            		case 2:
            			int total = countNodes(root);
            			deleteNodesRandomly(total/2);
            			printTree(root);
            			break;
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
                case 8:
                		listTreeVersions();
                		break;
                case 9:
                		version = scan.nextLong();
                		accessTreeVersion(version);
                		break;
            }
        }
    }
    

	private void listTreeVersions() {
		for (Map.Entry<Long, RedBlackNode> entry : rbVersions.entrySet())
		{
		    System.out.println("Key: "+ entry.getKey() + " Root: " + entry.getValue().key);
		}
	}
	
	private void accessTreeVersion(Long key) {
		if (rbVersions.containsKey(key)) {
			   RedBlackNode node = rbVersions.get(key);
			 System.out.println("Version: "+ key +" Tree: ");
			 printTreeVersion(node, key);
		}
	}

	private void printTreeVersion(RedBlackNode node, Long key) {
		for (Map.Entry<Long, RedBlackNode> entry : node.versions.entrySet())
		{
			if(entry.getKey().equals(key)) {
		    		System.out.println("Key: "+ entry.getKey() + " Node Left: " + entry.getValue().left.key);
			}
		}
	}

	public static void main(String[] args) {
        RedBlackTree rbt = new RedBlackTree();
        rbt.consoleUI();
    }
}

