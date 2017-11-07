package app;

import java.util.Random;

public class NORNode {
	
	private NORNode right;
	private NORNode left;
	private final static Random rand = new Random();

	// For exercice 2
	private char deterministicDirection;
	
	
	// if null -> not leaf, else leaf.
	private Boolean value;
	
	public NORNode(Boolean value) {
		this.value = value;
	}
	
	public NORNode(NORNode left, NORNode right) {
		this.value = null;
		this.left = left;
		this.right = right;
	}
	
	/**
	 * 
	 * @param value
	 * @param deterministicDirection 0 if this node first check left on evaluating. 1 otherwise.
	 */
	public NORNode(char deterministicDirection) {
		this.deterministicDirection = deterministicDirection;
	}
	
	public static int nbEvaluations = 0;
	
	public Boolean evaluate() {
		nbEvaluations++;
		if(this.value != null)
			return this.value;
		Boolean choice = rand.nextBoolean();
		// If true, we check the right child and if it's 0, we evaluate the left one.
		if(choice) 
			// If it is evaluated to 1, then return false
			if(this.right.evaluate())
				return this.value = false;
			// Otherwise, evaluate the right
			else 
				return this.left.evaluate() ? (this.value = false) : (this.value = true);
		// Otherwise, we evaluate the left one and if it's 0, we evaluate the right one.
		else 
			// If it is evaluated to 1, then return false
			if(this.left.evaluate()) 
				return this.value = false;
			// Otherwise, evaluate the right
			 else 
				return this.right.evaluate() ? (this.value = false) : (this.value = true);
	}
	
	public Boolean deterministicEvaluation() {
		nbEvaluations++;
		if(this.value != null)
			return this.value;
		// If true, we check the right child and if it's 0, we evaluate the left one.
		if(this.deterministicDirection == '1') 
			// If it is evaluated to 1, then return false
			if(this.right.evaluate())
				return this.value = false;
			// Otherwise, evaluate the right
			else 
				return this.left.evaluate() ? (this.value = false) : (this.value = true);
		// Otherwise, we evaluate the left one and if it's 0, we evaluate the right one.
		else 
			// If it is evaluated to 1, then return false
			if(this.left.evaluate()) 
				return this.value = false;
			// Otherwise, evaluate the right
			 else 
				return this.right.evaluate() ? (this.value = false) : (this.value = true);
	}
	
	//@Override
	public String toString() {
		return this.value == null ? 
				this.value + " -> {" + this.left.toString() + ":" + this.left.deterministicDirection  
						   + ";"    + this.right.toString() + ":" + this.right.deterministicDirection + "}" :
				this.value + "";
	}
	
	
	// ====== static part ====== //
	
	private static int indexPath = 0;
	private static int indexInput = 0;
	private static int height = 0;

	/**
	 * Create a game tree given its path.
	 * @param path
	 * @param input
	 * @return
	 */
	public static NORNode createGameTree(int h, String path, String input) {
		height = h;
		indexInput = 0;
		indexPath = 0;
		NORNode root = new NORNode(path.charAt(indexPath++));
		root.createChildrenUntilEnoughNodes(path, input, 1);
		return root;
	}
	
	public static void resetNbEvaluation() {
		nbEvaluations = 0;
	}

	/**
	 * @param path
	 * @param input
	 * @param currentDepth
	 */
	private void createChildrenUntilEnoughNodes(String path, String input, int currentDepth) {
		if(currentDepth < height) {
			this.left  = new NORNode(path.charAt(indexPath++));
			this.right = new NORNode(path.charAt(indexPath++));
			this.left.createChildrenUntilEnoughNodes(path, input, currentDepth + 1);
			this.right.createChildrenUntilEnoughNodes(path, input, currentDepth + 1);
			
		} else if(currentDepth == height) {
			this.left  = new NORNode(input.charAt(indexInput++) == '0' ? false : true);
			this.right = new NORNode(input.charAt(indexInput++) == '0' ? false : true);
		}
	}
}
