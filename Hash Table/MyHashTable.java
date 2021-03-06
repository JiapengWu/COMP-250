package a4q1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

class MyHashTable<K,V> implements Iterable<MyHashTable<K,V>.HashEntry>{
	/*
	 *   Number of entries in the HashTable. 
	 */
	private int entryCount = 0;
	
	/*
	 * Number of buckets. The constructor sets this variable to its initial value,
	 * which eventually can get changed by invoking the rehash() method.
	 */
	private int numBuckets;
	
	/**
	 * Threshold load factor for rehashing.
	 */
	private final double MAX_LOAD_FACTOR=0.75;
	
	/**
	 *  buckets to store the key-value pairs.   Traditionally an array is used for the buckets
	 *  and a linked allEntries is use for the entries within each bucket.   
	 *  
	 *  We use an ArrayallEntries rather than array, since the former is simpler to use in Java.   
	 */
	 
	ArrayList<LinkedList<HashEntry>>  buckets;
	
	/* 
	 * Constructor.
	 * 
	 * numBuckets is the initial number of buckets used by this hash table
	 */
	 
	MyHashTable(int numBuckets) {
		
		//  ADD YOUR CODE HERE
		this.numBuckets = numBuckets;
		this.buckets = new ArrayList<LinkedList<HashEntry>>(numBuckets);
		
	    for (int i = 0; i < numBuckets; i++) {
	    	buckets.get(i) = new LinkedList<HashEntry>();
	    }

		
		//set the size of buckets to be equal to numBuckets
		
		
	}
	
	/**
	 * Given a key, return the bucket position for the key. 
	 */
	private int hashFunction(K key) {
		
		return  Math.abs( key.hashCode() ) % numBuckets ;
	}
	
	/**
	 * Checking if the hash table is empty.  
	 */
	public boolean isEmpty()
	{
		if (entryCount == 0)
			return true;
		else
			return(false);
	}
	
	/**
	 *   return the number of entries in the hash table.
	 */
	public int size()
	{
		return(entryCount);
	}
	
	/**
	 * Adds a key-value pair to the hash table. If the load factor goes above the 
	 * MAX_LOAD_FACTOR, then call the rehash() method after inserting. 
	 * 
	 *  If there was a previous value for the given key in this hashtable, then return it.
	 *  Otherwise return null.   
	 */
	
	public  V  put(K key, V value) {
		
		//  ADD YOUR CODE HERE
		//System.out.println("putting:" + key);
		//System.out.println("size of Buckets:" + this.buckets.size());
		
		V preValue = null;//to be modified
		
		LinkedList<HashEntry> cur = new LinkedList<HashEntry>();
		
		int flag = 0;
		for(int i = 0; i < this.buckets.size(); i++) {
			
			cur = this.buckets.get(i);
			if(cur == null){
				continue;
			}
			for(int j = 0; j < cur.size(); j++){
				
				if(key.equals(cur.get(j).key)){
					preValue = cur.get(j).value;
					cur.get(j).value = value;
					flag = 1;
				}
				
			}
		}
		// if there was mapping for the key, set the value of the previous entry to the current
		// key and set the previous key.
		
		if(flag == 0){
			
			HashEntry newEntry = new HashEntry(key,value);
		
			int position = hashFunction(key); //get the index of bucket
		
			LinkedList<HashEntry> toBeAdded = this.buckets.get(position); 
			//System.out.println("position:" + position);
			//get the linked list at corresponding position 
			if(toBeAdded == null){
				LinkedList<HashEntry> cur1 = new LinkedList<HashEntry>();
				cur1.add(newEntry);
				buckets.set(position, cur1);
				
			}
			else{
				toBeAdded.add(newEntry);
			}
			
			
			// add the new entry to the linked list
		
			// set preValue to null
			
			this.entryCount++;
			// increase entryCount by 1
		}
		
		// else insert the entry and return null
		float ratio = (float)this.entryCount/this.numBuckets;
		//System.out.println(ratio);
		if(ratio > MAX_LOAD_FACTOR){
			this.rehash();
		}
		// If the load factor goes above the 
		// MAX_LOAD_FACTOR, then call the rehash()
		
		return preValue;
	}
	
	/**
	 * Retrieves a value associated with some given key in the hash table.
	   Returns null if the key could not be found in the hash table)
	 */
	public V get(K key) {	
		LinkedList<HashEntry> cur = new LinkedList<HashEntry>();
		
		for(int i = 0; i < this.numBuckets; i++) {
		//loop through every bucket
			cur = this.buckets.get(i);
			if(cur == null){
				continue;
			}
			for(HashEntry node : cur){
			//loop through the linked list in cur			
				if(key.equals(node.getKey())){
					return node.getValue();
				}
			}
		}

		return null;
		// Returns null if the key could not be found in the hash table
	}
	
	/**
	 * Removes a key-value pair from the hash table.
	 * Return value associated with the provided key.   If the key is not found, return null.
	 */
	public V remove(K key) {

		LinkedList<HashEntry> cur = new LinkedList<HashEntry>();

		for(int i = 0; i < this.buckets.size(); i++) {
			
			cur = this.buckets.get(i);
			if(cur == null){
				continue;
			}
			for(int j = 0; j < cur.size(); j++){
				HashEntry node = cur.get(j);
				if(key.equals(node.getKey())){
					V preValue = node.getValue();
					cur.remove(j);
					// Removes a key-value pair
					return preValue;
					// Return value associated with the provided key
				}
				
			}
		}
	
		return null;
		
	}
	

	/*
	 *  This method is used for testing rehash().  Normally one would not provide such a method. 
	 */
	public int getNumBuckets(){
		return numBuckets;
	}

	/*
	 * Returns an iterator for the hash table. 
	 */
	
	//@Override
	public MyHashTable<K, V>.HashIterator	iterator(){
		return new HashIterator();
	}
	
	/**
	 * Removes all the entries from the hash table, but keeps the number of buckets intact.
	 */
	public void clear()
	{
		for (int ct = 0; ct < this.buckets.size(); ct++){
			buckets.get(ct).clear();
		}
		entryCount=0;		
	}
	
	/**
	 *   Create a new hash table that has twice the number of buckets.
	 */
	
	public void rehash()
	{
		//System.out.println("rehashing.............................................................");
		this.numBuckets *= 2;
		MyHashTable<K, V> newHashTable = new MyHashTable<K, V>(this.numBuckets);
		
		ArrayList<K> keyList = this.keys();
		int keyListSize = keyList.size();
		if(keyListSize != 0){
			for(int i = 0; i < keyListSize; i++){
				
				K curKey = keyList.get(i);
				V curValue = this.get(curKey);
				newHashTable.put(curKey, curValue);
			}
		
			this.buckets = newHashTable.buckets;
		}
		//System.out.println("rehashing finished..........................");
		
	}
	
	/*
	 * Checks if the hash table contains the given key.
	 * Return true if the hash table has the specified key, and false otherwise.
	 */
	public boolean containsKey(K key)
	{
		return (get(key) != null);
	}
		
	/*
	 * return an ArrayList of the keys in the hashtable
	 */
	
	public ArrayList<K>  keys()
	{
		
		ArrayList<K>  listKeys = new ArrayList<K>();
		
		LinkedList<HashEntry> cur = new LinkedList<HashEntry>();
		
		for(int i = 0; i < this.buckets.size(); i++) {
		//loop through every bucket
			cur = this.buckets.get(i);
			if(cur == null){
				continue;
			}
			for(HashEntry node : cur){
			//loop through the linked list in cur
				listKeys.add(node.getKey());
			}
		}

		return listKeys;

	}
	
	/*
	 * return an ArrayList of the values in the hashtable
	 */
	public ArrayList <V> values()
	{
		ArrayList<V>  listValues = new ArrayList<V>();
		
		LinkedList<HashEntry> cur = new LinkedList<HashEntry>();
		
		for(int i = 0; i < this.buckets.size(); i++) {
		//loop through every bucket
			cur = this.buckets.get(i);
			if(cur == null){
				continue;
			}
			for(HashEntry node : cur){
			//loop through the linked list in cur
				listValues.add(node.getValue());
			}
		}
		
		return listValues;
	}

	@Override
	public String toString() {
		/*
		 * Implemented method. You do not need to modify.
		 */
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buckets.size(); i++) {
			sb.append("Bucket ");
			sb.append(i);
			sb.append(" has ");
			sb.append(buckets.get(i).size());
			sb.append(" entries.\n");
		}
		sb.append("There are ");
		sb.append(entryCount);
		sb.append(" entries in the hash table altogether.");
		return sb.toString();
	}
	
	/*
	 *    Inner class:   Iterator for the Hash Table.
	 */
	public class HashIterator implements  Iterator<HashEntry>
	{
		private static final int MyHashTable = 0;
		LinkedList<HashEntry>  allEntries;
		
		/**
		 * Constructor:   make a linkedlist 'allEntries' of all the entries in the hash table
		 */
		public  HashIterator()
		{
			
			//  ADD YOUR CODE HERE
			this.allEntries = new LinkedList<HashEntry>();
			for(K key : MyHashTable.this.keys()){
				HashEntry curKey = MyHashTable.this.getEntry(key);
				allEntries.add(curKey);
			}
			
		}
		
		//  Override
		@Override
		public boolean hasNext()
		{
			return !allEntries.isEmpty();
		}
			
		//  Override
		@Override
		public HashEntry next()
		{	
			return allEntries.removeFirst();
		}

		@Override
		public void remove() {

		// not implemented,  but must be declared because it is in the Iterator interface
			
		}		
	}
	
	//  helper method
	
	private HashEntry  getEntry(K key){
		int index = hashFunction(key);
 		LinkedList<HashEntry> bucketList = buckets.get(index);
 		for (HashEntry node : bucketList ){
 			if (node.getKey() == key)
 				return node; 
 		}
 		return null;	 	
	}

	class HashEntry {
		
		private K key;
		private V value;
		
		/*
		 * Constructor.
		 */
		HashEntry(K key, V value) {
			this.key = key;
			this.value = value;
		}
		
		/*
		 * Returns this hash entry's key.   Assume entry is not null.
		 * @return This hash entry's key
		 */
		K getKey() {
			return(key);
		}
		
		/**
		 * Returns this hash entry's value.  Assume entry is not null.
		 */
		V getValue() {
			return(value);
		}
		
		/**
		 * Sets this hash entry's value.
		 */
		void setValue(V value) {
			this.value =  value;		
		}		
	}


}
