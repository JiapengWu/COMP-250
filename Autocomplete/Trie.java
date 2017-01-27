package a3posted;

//Name:Jiapeng Wu
//Id:260727743

//  COMP 250 - Introduction to Computer Science - Fall 2016
//  Assignment #3 - Question 1

import java.util.*;

/*
 *  Trie class.  Each node is associated with a prefix of some key 
 *  stored in the trie.   (Any string is a prefix of itself.)
 */

public class Trie
{
	private TrieNode root;

	// Empty trie has just a root node.  All the children are null.

	public Trie() 
	{
		root = new TrieNode();
	}

	public TrieNode getRoot(){
		return root;
	}

	
	/*
	 * Insert key into the trie.  First, find the longest 
	 * prefix of a key that is already in the trie (use getPrefixNode() below). 
	 * Then, add TrieNode(s) such that the key is inserted 
	 * according to the specification in PDF.
	 * Change the value of endOfKey.
	 */
	public void insert(String key)
	{
		//  ADD YOUR CODE BELOW HERE
		int len = key.length();
		
		TrieNode cur = new TrieNode();
		
		cur=getRoot();
		//initial cur with the root
		
		int count=0;
		
		while(count<len){
			cur=cur.createChild(key.charAt(count));
			if(cur.isEndOfKey()==true){
				count++;
				continue;
			}
			//If the current node is end of some key, leave it and continue
				if(count==len-1){
					cur.setEndOfKey(true);
				}
				else{
					cur.setEndOfKey(false);
				}
			//otherwise set it to true if it's the end of a key
			count++;
		}
		
	

		//  ADD YOUR ABOVE HERE
	}

	// insert each key in the list (keys)

	public void loadKeys(ArrayList<String> keys)
	{
		for (int i = 0; i < keys.size(); i++)
		{
			insert(keys.get(i));
		}
		return;
	}

	/*
	 * Given an input key, return the TrieNode corresponding the longest prefix that is found. 
	 * If no prefix is found, return the root. 
	 * In the example in the PDF, running getPrefixNode("any") should return the
	 * dashed node under "n", since "an" is the longest prefix of "any" in the trie. 
	 */
	private TrieNode getPrefixNode(String key)
	{
		//   ADD YOUR CODE BELOW HERE
		
		int len = key.length();
		
		TrieNode cur = new TrieNode();
		
		cur=getRoot();
		//initial cur with the root
		
		int count=0;
		while(count<len){
			if(cur.getChild(key.charAt(count))==null){
				return cur;
			}
			else{
				cur=cur.getChild(key.charAt(count));
			}
			count++;
		}
		//Continue to get the corresponding child and see if it matches. 
		//When it doesn't match, return the last node that matches.
		
		return cur;
		//If all matches, return the leaf node of this path
		
		//   ADD YOUR CODE ABOVE HERE

	}

	/*
	 * Similar to getPrefixNode() but now return the prefix as a String, rather than as a TrieNode.
	 */

	public String getPrefix(String key)
	{
		return getPrefixNode(key).toString();
	}

	
	/*
	 *  Return true if key is contained in the trie (i.e. it was added by insert), false otherwise.
	 *  Hint:  any string is a prefix of itself, so you can use getPrefixNode().
	 */
	public boolean contains(String key)
	{  
		//   ADD YOUR CODE BELOW HERE
		TrieNode cur = new TrieNode();
		cur=getPrefixNode(key);
		
		int len=key.length();
		
		if((cur.isEndOfKey()==true)&&(len==cur.getDepth())){
			return true;
		}
		else{
			return false;
		}
		//check if cur is the end of the key and if the depth equals to the length of the key
		
		//   ADD YOUR CODE ABOVE HERE
	}

	/*
	 *  Return a list of all keys in the trie that have the given prefix. 
	 */
	public ArrayList<String> getAllPrefixMatches( String prefix )
	{
		//  ADD YOUR CODE BELOW 
		
		//int len = key.length();
		
		ArrayList<String> stringList=new ArrayList<String>();
		
		TrieNode cur = new TrieNode();
		
		cur=getPrefixNode(prefix);
		//return the prefix node of the input string
		
		if(cur.getDepth()!=prefix.length()){
			return stringList;
		}
		else{
			findAllKeys(cur, stringList);
			//invoke the helper method with the arguement cur and stringList
		}
		//  ADD YOUR CODE ABOVE HERE

		return stringList;
	}

	
	
	//helper method
	//Traverse the subtree of node t and append all the keys to the 
	//arrayList.
	
	public void findAllKeys(TrieNode t, ArrayList<String> list){
		if(t!=null){ //if the current node is not null
			if(t.isEndOfKey()==true){
				 list.add(t.toString());
			}
			//if the current node is the end of key, append it to the ArrayList
			for(int i=0;i<256;i++){
				findAllKeys(t.getChild((char)i), list);
			}
			//traverse through the subtree of this node.
		}
	}
	
	
	
	
	/*
	 *  A node in a Trie (prefix) tree.  
	 *  It contains an array of children: one for each possible character.
	 *  The ith child of a node corresponds to character (char)i
	 *  which is the UNICODE (and ASCII) value of i. 
	 *  Similarly the index of character c is (int)c.
	 *  So children[97] = children[ (int) 'a']  would contain the child for 'a' 
	 *  since (char)97 == 'a'   and  (int)'a' == 97.
	 * 
	 * References:
	 * -For all unicode charactors, see http://unicode.org/charts
	 *  in particular, the ascii characters are listed at http://unicode.org/charts/PDF/U0000.pdf
	 * -For ascii table, see http://www.asciitable.com/
	 * -For basic idea of converting (casting) from one type to another, see 
	 *  any intro to Java book (index "primitive type conversions"), or google
	 *  that phrase.   We will cover casting of reference types when get to the
	 *  Object Oriented Design part of this course.
	 */

	private class TrieNode
	{
		/*  
		 *   Highest allowable character index is NUMCHILDREN-1
		 *   (assuming one-byte ASCII i.e. "extended ASCII")
		 *   
		 *   NUMCHILDREN is constant (static and final)
		 *   To access it, write "TrieNode.NUMCHILDREN"
		 */

		public static final int NUMCHILDREN = 256;

		private TrieNode   parent;
		private TrieNode[] children;
		private int        depth;            // 0 for root, 1 for root's children, 2 for their children, etc..
		private char       charInParent;    // Character associated with edge between this node and its parent.
		        			        		// See comment above for relationship between an index in 0 to 255 and a char value.
		private boolean endOfKey;   // Set to true if prefix associated with this node is also a key.

		// Constructor for new, empty node with NUMCHILDREN children.  All the children are null. 

		public TrieNode()
		{
			children = new TrieNode[NUMCHILDREN];
			endOfKey = false;
			depth = 0; 
			charInParent = (char)0; 
		}

		
		/*
		 *  Add a child to current node.  The child is associated with the character specified by
		 *  the method parameter.  Make sure you set all fields in the child node.
		 *  
		 *  To implement this method, see the comment above the inner class TrieNode declaration.  
		 */
		public TrieNode createChild(char  c) 
		{	   
			TrieNode child = new TrieNode();

			// ADD YOUR CODE BELOW HERE
			
			if(this.children[(int)c]!=null){
				return this.children[(int)c];
			}
			else{
				//check if there already exist such child
				child.parent=this;
				this.children[(int)c]=child;
				//parent and child get to know each other
				
				child.children=new TrieNode[NUMCHILDREN];
				//child is gonna have his own bunch of children
			
				child.depth=this.depth+1;
				//depth increment
				
				child.charInParent=c;
				child.endOfKey=false;
		
				// ADD YOUR CODE ABOVE HERE

				return child;
			}
		}

		// Get the child node associated with a given character, i.e. that character is "on" 
		// the edge from this node to the child.  The child could be null.  
		
		//helper method, get the charInParent of this node	
		public char getCharInParent(){
			return charInParent;
		}
		
		//helper method, get the depth of this node
		public int getDepth(){
			return depth;
		}

		public TrieNode getChild(char c) 
		{
			return children[ c ];
		}

		// Test whether the path from the root to this node is a key in the trie.  
		// Return true if it is, false if it is prefix but not a key.

		public boolean isEndOfKey() 
		{
			return endOfKey;
		}

		// Set to true for the node associated with the last character of an input word

		public void setEndOfKey(boolean endOfKey)
		{
			this.endOfKey = endOfKey;
		}

		/*  
		 *  Return the prefix (as a String) associated with this node.  This prefix
         *  is defined by descending from the root to this node.  However, you will
         *  find it is easier to implement by ascending from the node to the root,
         *  composing the prefix string from its last character to its first.  
		 *
		 *  This overrides the default toString() method.
		 */

		public String toString()
		{
			// ADD YOUR CODE BELOW HERE
			
			String reverse="";
			String s="";
			TrieNode cur = new TrieNode();
			cur=this;
			while(cur.getDepth()>=1){
				s=s+cur.getCharInParent();
				cur=cur.parent;
			}
			//Creat a string by ascending from the current node to the root
			
			int len=s.length();
			
			for(int i=len-1;i>=0;i--){
				reverse = reverse + s.charAt(i);
			}
			//reverse the created string
			return reverse;
		
			// ADD YOUR CODE ABOVE HERE
		}
	}
}
