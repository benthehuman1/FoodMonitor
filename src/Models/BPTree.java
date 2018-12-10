package Models;

import java.util.*;

/**
 * Implementation of a B+ tree to allow efficient access to
 * many different indexes of a large data set. 
 * BPTree objects are created for each type of index
 * needed by the program.  BPTrees provide an efficient
 * range search as compared to other types of data structures
 * due to the ability to perform log_m N lookups and
 * linear in-order traversals of the data items.
 * 
 * @author sapan (sapan@cs.wisc.edu), William Huang
 *
 * @param <K> key - expect a string that is the type of id for each item
 * @param <V> value - expect a user-defined type that stores all data for a food item
 */
public class BPTree<K extends Comparable<K>, V> implements BPTreeADT<K, V> {

    // Root of the tree
    private Node root;
    
    // Branching factor is the number of children nodes 
    // for internal nodes of the tree
    private int branchingFactor;
    
    /**
     * Public constructor
     * 
     * @param branchingFactor 
     */
    public BPTree(int branchingFactor) {
        if (branchingFactor <= 2) {
            throw new IllegalArgumentException(
               "Illegal branching factor: " + branchingFactor);
        }
        root = new LeafNode();
        this.branchingFactor = branchingFactor;
    }
    
    /*
     * (non-Javadoc)
     * @see BPTreeADT#insert(java.lang.Object, java.lang.Object)
     */
    public void insert(K key, V value) {
        root.insert(key, value);
    }
    
    /**
     * (non-Javadoc)
     * @see BPTreeADT#rangeSearch(java.lang.Object, java.lang.String)
     */
    public List<V> rangeSearch(K key, String comparator) {
        if (!comparator.contentEquals(">=") && 
            !comparator.contentEquals("==") && 
            !comparator.contentEquals("<=") )
            return new ArrayList<V>();
        if(key == null)
        	return new ArrayList<V>();
        return root.rangeSearch(key, comparator);
    }
    
    /**
     * Returns all values that satisfies the range query using the 
     * given key. Range query should be passed in the form of 
     * the Comparator enum. 
     * @param key Key on which to run a query on
     * @param comparator Comparator enum specifying query
     * @return List of all values satisfying the query
     */
    public List<V> rangeSearch(K key, Comparator comparator) {
    	if(key == null)
    		return new ArrayList<V>();
    	return root.rangeSearch(key, comparator);
    }
    
    /**
     * Deletes every node in the B+ tree
     */
    public void deleteAll() {
    	root = new LeafNode();
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @SuppressWarnings("unchecked")
	@Override
    public String toString() {
        Queue<List<Node>> queue = new LinkedList<List<Node>>();
        queue.add(Arrays.asList(root));
        StringBuilder sb = new StringBuilder();
        while (!queue.isEmpty()) {
            Queue<List<Node>> nextQueue = new LinkedList<List<Node>>();
            while (!queue.isEmpty()) {
                List<Node> nodes = queue.remove();
                sb.append('{');
                Iterator<Node> it = nodes.iterator();
                while (it.hasNext()) {
                    Node node = it.next();
                    sb.append(node.toString());
                    if (it.hasNext())
                        sb.append(", ");
                    if (node instanceof BPTree.InternalNode)
                        nextQueue.add(((InternalNode) node).children);
                }
                sb.append('}');
                if (!queue.isEmpty())
                    sb.append(", ");
                else {
                    sb.append('\n');
                }
            }
            queue = nextQueue;
        }
        return sb.toString();
    }
    
    /**
     * This abstract class represents any type of node in the tree
     * This class is a super class of the LeafNode and InternalNode types.
     * 
     * @author sapan
     */
    private abstract class Node {
        
        // List of keys
        List<K> keys;
        
        /**
         * Package constructor
         */
        Node() {
            keys = new ArrayList<K>();
        }
        
        /**
         * Inserts key and value in the appropriate leaf node 
         * and balances the tree if required by splitting
         *  
         * @param key
         * @param value
         */
        abstract void insert(K key, V value);

        /**
         * Gets the first leaf key of the tree
         * 
         * @return key
         */
        abstract K getFirstLeafKey();
        
        /**
         * Gets the new sibling created after splitting the node
         * 
         * @return Node
         */
        abstract Node split();
        
        /**
         * (non-Javadoc)
         * @see BPTree#rangeSearch(java.lang.Object, java.lang.String)
         */
        abstract List<V> rangeSearch(K key, String comparator);
        
        /**
         * (non-Javadoc)
         * @see BPTree#rangeSearch(java.lang.Object, Comparator)
         */
        abstract List<V> rangeSearch(K key, Comparator comparator);

        /**
         * 
         * @return boolean
         */
        abstract boolean isOverflow();
        
        public String toString() {
            return keys.toString();
        }
    
    } // End of abstract class Node
    
    /**
     * This class represents an internal node of the tree.
     * This class is a concrete sub class of the abstract Node class
     * and provides implementation of the operations
     * required for internal (non-leaf) nodes.
     * 
     * @author sapan
     */
    private class InternalNode extends Node {

        // List of children nodes
        List<Node> children;
        
        /**
         * Package constructor
         */
        InternalNode() {
            super();
            children = new ArrayList<Node>();
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#getFirstLeafKey()
         */
        K getFirstLeafKey() {
        	return children.get(0).getFirstLeafKey();
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#isOverflow()
         */
        boolean isOverflow() {
            return keys.size() == branchingFactor;
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#insert(java.lang.Comparable, java.lang.Object)
         */
        void insert(K key, V value) {
        	
        	//Insert into the appropriate child
        	int loc = 0;
        	while(loc < keys.size() && keys.get(loc).compareTo(key) < 0)
        		loc++;
        	Node child = children.get(loc);
			child.insert(key, value);
			
			//Split if overflow in child
			if (child.isOverflow()) {
				
				//Search for location to insert child based on sibling's first key
				Node sibling = child.split();
//				loc = 0;
	        	while(loc < keys.size() && keys.get(loc).compareTo(sibling.getFirstLeafKey()) < 0)
	        		loc++;
	        	keys.add(loc, sibling.getFirstLeafKey());
	        	children.add(loc + 1, sibling);
				
			}
			
			//Split if root overflow
			//Note: This code only runs if this node is the root
			if (root.isOverflow()) {
				
				//Split node
				Node sibling = split();
				InternalNode newRoot = new InternalNode();
				
				//Promote middle key
				newRoot.keys.add(sibling.getFirstLeafKey());
				newRoot.children.add(this);
				newRoot.children.add(sibling);
				root = newRoot;
				
			}
			
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#split()
         */
        Node split() {

//        	System.out.println("\n*****Split*****");
//        	System.out.println(keys + " size: " + keys.size());
        	
        	//Creates the right sibling node
        	InternalNode sibling = new InternalNode();
        	int start = keys.size() / 2 + 1;
        	
        	//Add the right half to the sibling
        	sibling.children.addAll(this.children.subList(start, children.size()));
        	sibling.keys.addAll(this.keys.subList(start, keys.size()));
        	
        	//Adds remaining members (removes the right half), note the middle key gets
        	//promoted and is thus not included
        	children = children.subList(0, start);
        	keys = keys.subList(0, start - 1);
			
//			System.out.println(keys + " " + sibling.keys);
//			System.out.println("***************");
			
            return sibling;
            
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#rangeSearch(java.lang.Comparable, java.lang.String)
         */
        List<V> rangeSearch(K key, String comparator) {
        	int loc = 0;
        	if(comparator.equals("<=")) {
        		
        		//Locate the last node with equal key
        		while(loc < keys.size() && keys.get(loc).compareTo(key) <= 0)
	        		loc++;
	        	return children.get(loc).rangeSearch(key, comparator);
	        	
        	}
        	else {
        		
        		//Locate the first node with equal key
	        	while(loc < keys.size() && keys.get(loc).compareTo(key) < 0)
	        		loc++;
	        	return children.get(loc).rangeSearch(key, comparator);
	        	
        	}
        }

        /**
         * (non-Javadoc)
         * @see BPTree.Node#rangeSearch(java.lang.Comparable, Comparator)
         */
		List<V> rangeSearch(K key, Comparator comparator) {
			int loc = 0;
        	if(comparator == Comparator.LESSTHAN) {
        		
        		//Locate the last node with equal key
        		while(loc < keys.size() && keys.get(loc).compareTo(key) <= 0)
	        		loc++;
	        	return children.get(loc).rangeSearch(key, comparator);
	        	
        	}
        	else {
        		
        		//Locate the first node with equal key
	        	while(loc < keys.size() && keys.get(loc).compareTo(key) < 0)
	        		loc++;
	        	return children.get(loc).rangeSearch(key, comparator);
	        	
        	}
		}
    
    } // End of class InternalNode
    
    
    /**
     * This class represents a leaf node of the tree.
     * This class is a concrete sub class of the abstract Node class
     * and provides implementation of the operations that
     * required for leaf nodes.
     * 
     * @author sapan
     */
    private class LeafNode extends Node {
        
        // List of values
        List<V> values;
        
        // Reference to the next leaf node
        LeafNode next;
        
        // Reference to the previous leaf node
        LeafNode previous;
        
        /**
         * Package constructor
         */
        LeafNode() {
            super();
            values = new ArrayList<V>();
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#getFirstLeafKey()
         */
        K getFirstLeafKey() {
            return keys.get(0);
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#isOverflow()
         */
        boolean isOverflow() {
        	return values.size() > branchingFactor - 1;
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#insert(Comparable, Object)
         */
        void insert(K key, V value) {
        	
        	//Insert key-value pair into appropriate location
        	int loc = 0;
        	while(loc < keys.size() && keys.get(loc).compareTo(key) < 0)
        		loc++;
        	keys.add(loc, key);
        	values.add(loc, value);
        	
        	//Deal with possible root overflow
        	//Note: this code only runs if this node is the root
			if (root.isOverflow()) {
				
				//Split node
				Node sibling = split();
				InternalNode newRoot = new InternalNode();
				
				//Promote middle key
				newRoot.keys.add(sibling.getFirstLeafKey());
				newRoot.children.add(this);
				newRoot.children.add(sibling);
				root = newRoot;
				
			}
			
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#split()
         */
        Node split() {
        	
//        	System.out.println("\n*****Split*****");
//        	System.out.println(keys + " size: " + keys.size());
        	
        	//Create the right sibling node
        	LeafNode sibling = new LeafNode();
			int start = keys.size() / 2;
        	
			//Add the right half to the sibling
        	sibling.values.addAll(this.values.subList(start, values.size()));
        	sibling.keys.addAll(this.keys.subList(start, keys.size()));
        	
        	//Add remaining members (remove the right half)
        	values = values.subList(0, start);
        	keys = keys.subList(0, start);

        	//Update pointers
			sibling.next = next;
			sibling.previous = this;
			if(next != null)
				next.previous = sibling;
			next = sibling;
			
//			System.out.println(keys + " " + sibling.keys);
//			System.out.println("***************");
			
			return sibling;
			
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#rangeSearch(Comparable, String)
         */
        List<V> rangeSearch(K key, String comparator) {

        	List<V> ans = new ArrayList<V>();
        	int loc = 0;
        	LeafNode cur = this;
        	
        	//Attempt to find a node equal to key
        	while(cur.keys.get(loc).compareTo(key) < 0) {
				loc++;
				if(loc == cur.keys.size()) {
					if(cur.next == null)
						return ans;
					loc = 0;
					cur = cur.next;
				}
				
				//If we ever encounter a key greater than the query, then the query
				//does not exist within the tree
				if(cur.keys.get(loc).compareTo(key) > 0)
					return ans;
			}
        	
        	int firstEqualIndex = loc;
        	LeafNode firstEqualNode = cur;
        	
        	//Add all equal values to list
        	while(cur != null && cur.keys.get(loc).compareTo(key) == 0) {
        		ans.add(cur.values.get(loc));
        		loc++;
				if(loc == cur.keys.size()) {
					loc = 0;
					cur = cur.next;
				}
        	}
        	
        	if(comparator.equals(">=") && cur != null) {
        		
        		//Add every value after the equal ones
        		if(loc < cur.values.size())
        			ans.addAll(cur.values.subList(loc, cur.values.size()));
        		cur = cur.next;        		
        		while(cur != null) {
        			ans.addAll(cur.values);
        			cur = cur.next;
        		}
        		
        	}
        	
        	if(comparator.equals("<=")) {
        		
        		//Reset pointers to beginning
        		loc = firstEqualIndex;
        		cur = firstEqualNode;
        		
        		//Add every value before the equal ones
        		ans.addAll(cur.values.subList(0, loc));
        		cur = cur.previous;
        		while(cur != null) {
        			ans.addAll(cur.values);
        			cur = cur.previous;
        		}
        		
        	}
        	
            return ans;
        }

        /**
         * (non-Javadoc)
         * @see BPTree.Node#rangeSearch(java.lang.Comparable, Comparator)
         */
		List<V> rangeSearch(K key, Comparator comparator) {

        	List<V> ans = new ArrayList<V>();
        	int loc = 0;
        	LeafNode cur = this;
        	
        	//Attempt to find a node equal to key
        	while(cur.keys.get(loc).compareTo(key) < 0) {
				loc++;
				if(loc == cur.keys.size()) {
					if(cur.next == null)
						return ans;
					loc = 0;
					cur = cur.next;
				}
				
				//If we ever encounter a key greater than the query, then the query
				//does not exist within the tree
				if(cur.keys.get(loc).compareTo(key) > 0)
					return ans;
			}
        	
        	int firstEqualIndex = loc;
        	LeafNode firstEqualNode = cur;
        	
        	//Add all equal values to list
        	while(cur != null && cur.keys.get(loc).compareTo(key) == 0) {
        		ans.add(cur.values.get(loc));
        		loc++;
				if(loc == cur.keys.size()) {
					loc = 0;
					cur = cur.next;
				}
        	}
        	
        	if(comparator == Comparator.GREATERTHAN && cur != null) {
        		
        		//Add every value after the equal ones
        		if(loc < cur.values.size())
        			ans.addAll(cur.values.subList(loc, cur.values.size()));
        		cur = cur.next;        		
        		while(cur != null) {
        			ans.addAll(cur.values);
        			cur = cur.next;
        		}
        		
        	}
        	
        	if(comparator == Comparator.LESSTHAN) {
        		
        		//Reset pointers to beginning
        		loc = firstEqualIndex;
        		cur = firstEqualNode;
        		
        		//Add every value before the equal ones
        		ans.addAll(cur.values.subList(0, loc));
        		cur = cur.previous;
        		while(cur != null) {
        			ans.addAll(cur.values);
        			cur = cur.previous;
        		}
        		
        	}
        	
            return ans;
            
		}
        
    } // End of class LeafNode
    
    
    /**
     * Contains a basic test scenario for a BPTree instance.
     * It shows a simple example of the use of this class
     * and its related types.
     * 
     * @param args
     */
    public static void main(String[] args) {
        // create empty BPTree with branching factor of 3
        BPTree<Double, Double> bpTree = new BPTree<>(3);

        // create a pseudo random number generator
        Random rnd1 = new Random();

        // some value to add to the BPTree
        Double[] dd = {0.0d, 0.5d, 0.2d, 0.8d};

        // build an ArrayList of those value and add to BPTree also
        // allows for comparing the contents of the ArrayList 
        // against the contents and functionality of the BPTree
        // does not ensure BPTree is implemented correctly
        // just that it functions as a data structure with
        // insert, rangeSearch, and toString() working.
        List<Double> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Double j = dd[rnd1.nextInt(4)];
            System.out.println((i + 1) + ": Inserting " + j + "...");
            list.add(j);
            bpTree.insert(j, j);
            System.out.println("\n\nTree structure:\n" + bpTree.toString());
            System.out.println("------------------------------------");
        }
        List<Double> filteredValues = bpTree.rangeSearch(0.8d, "<=");
        System.out.println("Filtered values: " + filteredValues.toString());
    }

} // End of class BPTree