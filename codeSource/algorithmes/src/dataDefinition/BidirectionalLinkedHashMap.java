package dataDefinition;

import java.util.LinkedHashMap;

public class BidirectionalLinkedHashMap<K, V> {

	private LinkedHashMap<K, V> keyValue = new LinkedHashMap<>();
	private LinkedHashMap<V, K> valueKey = new LinkedHashMap<>();
	
	public V getValueByKey(K key) {
		return keyValue.get(key);
	}
	
	public K getKeyByValue(V value) {
		return valueKey.get(value);
	}
	
	public void putKeyValue(K key, V value) {
		this.keyValue.put(key, value);
		this.valueKey.put(value, key);
	}
	
	public void putValueKey(V value, K key) {
		this.keyValue.put(key, value);
		this.valueKey.put(value, key);
	}
	
	public boolean containsKey(K key) {
		return this.keyValue.containsKey(key);
	}
	
	public boolean containsValue(V value) {
		return this.valueKey.containsKey(value);
	}

	public void clear() {
		this.keyValue.clear();
		this.valueKey.clear();
	}
}
