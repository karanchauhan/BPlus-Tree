package com.karan.ads.bplustree;

/* 
 * author : Karan Chauhan
 */

import java.util.ArrayList;
// Note: The linked list is not used in the B Plus Tree implementation. 
// It's only used for a testing method that prints the b plus tree using a level order traversal 
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


/**
 * The Class BPlusTree.
 */

public class BPlusTree {

	/** The degree m. */
	private int m;

	/** The root of the B Plus Tree. */
	private Node root;

	/**
	 * Instantiates a new b plus tree.
	 */
	public BPlusTree() {

	}

	/**
	 * Initializes the B Plus Tree. Sets the degree of the BPlus Tree as m
	 *
	 * @param order
	 *            the degree of the B Plus Tree
	 */
	public void initialize(int order) {
		// At initialization, order of the tree is set to m. Root is set to null
		this.m = order;
		this.root = null;
		// System.out.println("B+ Tree Initialized. Degree is " + m);
	}

	/**
	 * Insert a key and value pair to the B Plus Tree
	 *
	 * @param key
	 *            the key to be inserted
	 * @param value
	 *            the value to be inserted
	 */
	public void insert(double key, String value) {

		// Case 1: Inserting to an Empty B Plus Tree
		if (null == this.root) {
			// For an empty B Tree, root is created and its key is set as the
			// newly inserted key
			Node newNode = new Node();
			newNode.getKeys().add(new Key(key, value));
			this.root = newNode;
			// Since the root has no parent, parent set to null
			this.root.setParent(null);
		}

		// Case 2: Only one node that is not full
		else if (this.root.getChildren().isEmpty() && this.root.getKeys().size() < (this.m - 1)) {
			// For all insertions until the root gets overfull for the first
			// time, we just update the root node, adding the new keys
			insertWithinExternalNode(key, value, this.root);
		}

		// Case 3: Normal insert
		else {
			Node curr = this.root;
			// Since we insert the element only at the external node, we
			// traverse to the last level
			while (!curr.getChildren().isEmpty()) {
				curr = curr.getChildren().get(binarySearchWithinInternalNode(key, curr.getKeys()));
			}
			insertWithinExternalNode(key, value, curr);
			if (curr.getKeys().size() == this.m) {
				// If the external node becomes full, we split it
				splitExternalNode(curr, this.m);
			}
		}

	}

	/**
	 * Insert the key value pair to the external node.
	 *
	 * @param key
	 *            the key to be inserted
	 * @param value
	 *            the value to be inserted
	 * @param node
	 *            the node where the key value pair has to be inserted
	 */
	private void insertWithinExternalNode(double key, String value, Node node) {
		// A binary search is executed to find the correct place where the node
		// is to be inserted
		int indexOfKey = binarySearchWithinInternalNode(key, node.getKeys());
		if (indexOfKey != 0 && node.getKeys().get(indexOfKey - 1).getKey() == key) {
			// Key already exists. Add the new value to the list
			node.getKeys().get(indexOfKey - 1).getValues().add(value);
		} else {
			// Key doesn't exist. Add key and value
			Key newKey = new Key(key, value);
			node.getKeys().add(indexOfKey, newKey);
		}
	}

	/**
	 * Split external node.
	 *
	 * @param curr
	 *            the over-full external node to be split
	 * @param m
	 *            the degree of the B Plus Tree
	 */
	private void splitExternalNode(Node curr, int m) {

		// Find the middle index
		int midIndex = m / 2;

		Node middle = new Node();
		Node rightPart = new Node();

		// Set the right part to have middle element and the elements right to
		// the middle element
		rightPart.setKeys(curr.getKeys().subList(midIndex, curr.getKeys().size()));
		rightPart.setParent(middle);
		// While making middle as the internal node, we add only the key since
		// internal nodes of bplus tree do not contain values
		middle.getKeys().add(new Key(curr.getKeys().get(midIndex).getKey()));
		middle.getChildren().add(rightPart);
		// Curr holds the left part, so update the split node to contain just
		// the left part
		curr.getKeys().subList(midIndex, curr.getKeys().size()).clear();

		boolean firstSplit = true;
		// propogate the middle element up the tree and merge with parent of
		// previously overfull node
		splitInternalNode(curr.getParent(), curr, m, middle, firstSplit);

	}

	/**
	 * Split internal node.
	 *
	 * @param curr
	 *            the current Internal Node
	 * @param prev
	 *            the child of the current Internal Node (Previous internal
	 *            node)
	 * @param m
	 *            the degree of the B Plus Tree
	 * @param toBeInserted
	 *            the part split to be inserted to this internal Node
	 * @param firstSplit
	 *            indicates if the split is happening at the first internal node
	 *            from the bottom
	 */
	private void splitInternalNode(Node curr, Node prev, int m, Node toBeInserted, boolean firstSplit) {
		if (null == curr) {
			// if we split the root before, then a new root has to be created
			this.root = toBeInserted;
			// we find where the child has to be inserted by doing a binary
			// search on keys
			int indexForPrev = binarySearchWithinInternalNode(prev.getKeys().get(0).getKey(), toBeInserted.getKeys());
			prev.setParent(toBeInserted);
			toBeInserted.getChildren().add(indexForPrev, prev);
			if (firstSplit) {
				// update the linked list only for first split (for external
				// node)
				if (indexForPrev == 0) {
					toBeInserted.getChildren().get(0).setNext(toBeInserted.getChildren().get(1));
					toBeInserted.getChildren().get(1).setPrev(toBeInserted.getChildren().get(0));
				} else {
					toBeInserted.getChildren().get(indexForPrev + 1)
							.setPrev(toBeInserted.getChildren().get(indexForPrev));
					toBeInserted.getChildren().get(indexForPrev - 1)
							.setNext(toBeInserted.getChildren().get(indexForPrev));
				}
			}
		} else {
			// merge the internal node with the mid + right of previous split
			mergeInternalNodes(toBeInserted, curr);
			if (curr.getKeys().size() == m) {
				// do a split again if the internal node becomes full
				int midIndex = (int) Math.ceil(m / 2.0) - 1;
				Node middle = new Node();
				Node rightPart = new Node();

				// since internal nodes follow a split like the b tree, right
				// part contains elements right of the mid element, and the
				// middle becomes parent of right part
				rightPart.setKeys(curr.getKeys().subList(midIndex + 1, curr.getKeys().size()));
				rightPart.setParent(middle);

				middle.getKeys().add(curr.getKeys().get(midIndex));
				middle.getChildren().add(rightPart);

				List<Node> childrenOfCurr = curr.getChildren();
				List<Node> childrenOfRight = new ArrayList<>();

				int lastChildOfLeft = childrenOfCurr.size() - 1;

				// update the children that have to be sent to the right part
				// from the split node
				for (int i = childrenOfCurr.size() - 1; i >= 0; i--) {
					List<Key> currKeysList = childrenOfCurr.get(i).getKeys();
					if (middle.getKeys().get(0).getKey() <= currKeysList.get(0).getKey()) {
						childrenOfCurr.get(i).setParent(rightPart);
						childrenOfRight.add(0, childrenOfCurr.get(i));
						lastChildOfLeft--;
					} else {
						break;
					}
				}

				rightPart.setChildren(childrenOfRight);

				// update the overfull node to contain just the left part and
				// update its children
				curr.getChildren().subList(lastChildOfLeft + 1, childrenOfCurr.size()).clear();
				curr.getKeys().subList(midIndex, curr.getKeys().size()).clear();

				// propogate split one level up
				splitInternalNode(curr.getParent(), curr, m, middle, false);
			}
		}
	}

	/**
	 * Merge internal nodes.
	 *
	 * @param mergeFrom
	 *            to part from which we have to merge (middle of the previous
	 *            split node)
	 * @param mergeInto
	 *            the internal node to be merged to
	 */
	private void mergeInternalNodes(Node mergeFrom, Node mergeInto) {
		Key keyToBeInserted = mergeFrom.getKeys().get(0);
		Node childToBeInserted = mergeFrom.getChildren().get(0);
		// Find the index where the key has to be inserted to by doing a binary
		// search
		int indexToBeInsertedAt = binarySearchWithinInternalNode(keyToBeInserted.getKey(), mergeInto.getKeys());
		int childInsertPos = indexToBeInsertedAt;
		if (keyToBeInserted.getKey() <= childToBeInserted.getKeys().get(0).getKey()) {
			childInsertPos = indexToBeInsertedAt + 1;
		}
		childToBeInserted.setParent(mergeInto);
		mergeInto.getChildren().add(childInsertPos, childToBeInserted);
		mergeInto.getKeys().add(indexToBeInsertedAt, keyToBeInserted);

		// Update Linked List of external nodes
		if (!mergeInto.getChildren().isEmpty() && mergeInto.getChildren().get(0).getChildren().isEmpty()) {

			// If merge is happening at the last element, then only pointer
			// between new node and previously last element
			// needs to be updated
			if (mergeInto.getChildren().size() - 1 != childInsertPos
					&& mergeInto.getChildren().get(childInsertPos + 1).getPrev() == null) {
				mergeInto.getChildren().get(childInsertPos + 1).setPrev(mergeInto.getChildren().get(childInsertPos));
				mergeInto.getChildren().get(childInsertPos).setNext(mergeInto.getChildren().get(childInsertPos + 1));
			}
			// If merge is happening at the last element, then only pointer
			// between new node and previously last element
			// needs to be updated
			else if (0 != childInsertPos && mergeInto.getChildren().get(childInsertPos - 1).getNext() == null) {
				mergeInto.getChildren().get(childInsertPos).setPrev(mergeInto.getChildren().get(childInsertPos - 1));
				mergeInto.getChildren().get(childInsertPos - 1).setNext(mergeInto.getChildren().get(childInsertPos));
			}
			// If merge is happening in between, then the next element and the
			// previous element's prev and next pointers have to be updated
			else {
				mergeInto.getChildren().get(childInsertPos)
						.setNext(mergeInto.getChildren().get(childInsertPos - 1).getNext());
				mergeInto.getChildren().get(childInsertPos).getNext()
						.setPrev(mergeInto.getChildren().get(childInsertPos));
				mergeInto.getChildren().get(childInsertPos - 1).setNext(mergeInto.getChildren().get(childInsertPos));
				mergeInto.getChildren().get(childInsertPos).setPrev(mergeInto.getChildren().get(childInsertPos - 1));
			}
		}

	}

	/**
	 * Helper method - Prints the tree using a level order traversal
	 */
	public void printTree() {
		Queue<Node> queue = new LinkedList<Node>();
		queue.add(this.root);
		queue.add(null);
		Node curr = null;
		int levelNumber = 2;
		System.out.println("Printing level 1");
		while (!queue.isEmpty()) {
			curr = queue.poll();
			if (null == curr) {
				queue.add(null);
				if (queue.peek() == null) {
					break;
				}
				System.out.println("\n" + "Printing level " + levelNumber++);
				continue;
			}

			printNode(curr);

			if (curr.getChildren().isEmpty()) {
				break;
			}
			for (int i = 0; i < curr.getChildren().size(); i++) {
				queue.add(curr.getChildren().get(i));
			}
		}

		curr = curr.getNext();
		while (null != curr) {
			printNode(curr);
			curr = curr.getNext();
		}

	}

	/**
	 * Helper method Prints a node of the tree.
	 *
	 * @param curr
	 *            the node to be printed
	 */
	private void printNode(Node curr) {
		for (int i = 0; i < curr.getKeys().size(); i++) {
			System.out.print(curr.getKeys().get(i).getKey() + ":(");
			String values = "";
			for (int j = 0; j < curr.getKeys().get(i).getValues().size(); j++) {
				values = values + curr.getKeys().get(i).getValues().get(j) + ",";
			}
			System.out.print(values.isEmpty() ? ");" : values.substring(0, values.length() - 1) + ");");
		}
		System.out.print("||");
	}

	/**
	 * Modified Binary search within internal node.
	 *
	 * @param key
	 *            the key to be searched
	 * @param keyList
	 *            the list of keys to be searched
	 * @return the first index of the list at which the key which is greater
	 *         than the input key
	 */
	public int binarySearchWithinInternalNode(double key, List<Key> keyList) {
		int st = 0;
		int end = keyList.size() - 1;
		int mid;
		int index = -1;
		// Return first index if key is less than the first element
		if (key < keyList.get(st).getKey()) {
			return 0;
		}
		// Return array size + 1 as the new positin of the key if greater than
		// last element
		if (key >= keyList.get(end).getKey()) {
			return keyList.size();
		}
		while (st <= end) {
			mid = (st + end) / 2;
			// Following condition ensures that we find a location s.t. key is
			// smaller than element at that index and is greater than or equal
			// to the element at the previous index. This location is where the
			// key would be inserted
			if (key < keyList.get(mid).getKey() && key >= keyList.get(mid - 1).getKey()) {
				index = mid;
				break;
			} // Following conditions follow normal Binary Search
			else if (key >= keyList.get(mid).getKey()) {
				st = mid + 1;
			} else {
				end = mid - 1;
			}
		}
		return index;
	}

	/**
	 * Search values for a key
	 *
	 * @param key
	 *            the key to be searched
	 * @return the list of values for the key
	 */
	public List<String> search(double key) {
		List<String> searchValues = null;

		Node curr = this.root;
		// Traverse to the corresponding external node that would 'should'
		// contain this key
		while (curr.getChildren().size() != 0) {
			curr = curr.getChildren().get(binarySearchWithinInternalNode(key, curr.getKeys()));
		}
		List<Key> keyList = curr.getKeys();
		// Do a linear search in this node for the key. Set the parameter
		// 'searchValues' only if success
		for (int i = 0; i < keyList.size(); i++) {
			if (key == keyList.get(i).getKey()) {
				searchValues = keyList.get(i).getValues();
			}
			if (key < keyList.get(i).getKey()) {
				break;
			}
		}

		return searchValues;
	}

	/**
	 * Search for all key values pairs between key1 and key2.
	 *
	 * @param key1
	 *            the starting key
	 * @param key2
	 *            the ending key
	 * @return the list of key value pairs between the two keys
	 */
	
	public List<Key> search(double key1, double key2) {
		//System.out.println("Searching between keys " + key1 + ", " + key2);
		List<Key> searchKeys = new ArrayList<>();
		Node currNode = this.root;
		// Traverse to the corresponding external node that would 'should'
		// contain starting key (key1)
		while (currNode.getChildren().size() != 0) {
			currNode = currNode.getChildren().get(binarySearchWithinInternalNode(key1, currNode.getKeys()));
		}
		
		// Start from current node and add keys whose value lies between key1 and key2 with their corresponding pairs
		// Stop if end of list is encountered or if value encountered in list is greater than key2
		
		boolean endSearch = false;
		while (null != currNode && !endSearch) {
			for (int i = 0; i < currNode.getKeys().size(); i++) {
				if (currNode.getKeys().get(i).getKey() >= key1 && currNode.getKeys().get(i).getKey() <= key2)
					searchKeys.add(currNode.getKeys().get(i));
				if (currNode.getKeys().get(i).getKey() > key2) {
					endSearch = true;
				}
			}
			currNode = currNode.getNext();
		}

		return searchKeys;
	}

}
