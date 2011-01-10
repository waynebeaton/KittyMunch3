package ca.rokc.kittymunch.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This implementation purposefully avoids dealing with any 
 * concurrency issues. We assume that the game itself is 
 * running in a single thread.
 */
public class DelayedChangeList<T> implements Iterable<T> {
	public List<T> objects = new LinkedList<T>();
	private List<T> objectsToRemove = new ArrayList<T>();
	private List<T> objectsToAdd = new ArrayList<T>();

	List<ChangeListener<T>> listeners = new ArrayList<ChangeListener<T>>();
	
	public void add(T object) {
		objectsToRemove.remove(object);
		objectsToAdd.add(object);
	}
	
	public void remove(T object) {
		objectsToAdd.remove(object);
		objectsToRemove.add(object);		
	}
	
	public void addChangeListener(ChangeListener<T> listener) {
		if (listeners.contains(listener)) return;
		listeners.add(listener);
	}
	
	public void removeChangeListener(ChangeListener<T> listener) {
		listeners.remove(listener);
	}
	
	public void update() {
		for(T object : objectsToAdd) {
			objects.add(object);
			for(ChangeListener<T> listener : listeners)
				listener.objectAdded(object);
		}
		objectsToAdd.clear();
		
		for(T object : objectsToRemove) {
			objects.remove(object);
			for(ChangeListener<T> listener : listeners)
				listener.objectRemoved(object);
		}
		objectsToRemove.clear();
	}
	
	@Override
	public Iterator<T> iterator() {
		return objects.iterator();
	}

	public int size() {
		return objects.size() + objectsToAdd.size() - objectsToRemove.size();
	}
	
}
