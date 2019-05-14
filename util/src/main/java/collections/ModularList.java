/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package collections;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.RandomAccess;

/**
 *
 * @author dpf
 * @param <T>
 */
public class ModularList<T> extends AbstractList<T> implements List<T>, Serializable, Cloneable, Iterable<T>, Collection<T>, RandomAccess {

    private class Module {
        
        Module prev = null;
        Module next = null;
        T cells[];
        int noe;

        Module(int module_size) {
            cells = (T[]) new Object[module_size];
        }

        int size() {
            return noe;
        }

        int moduleSize() {
            return cells.length;
        }

        int free() {
            return cells.length - noe;
        }

        T set(int index, T element) {
            checkRange(index);
            T old = cells[index];
            cells[index] = element; 
            return old;
        }

        T get(int index) {
            checkRange(index);
            return cells[index];
        }

        boolean add(int index, T element) {
            if (index < 0 || index > noe || free()<1) {
                return false;
            } else {
                System.arraycopy(cells, index, cells, index+1, noe-index);
                cells[index] = element;
                noe++;
                return true;
            }
        }

        boolean add(T element) {
            if (free()<1) {
                return false;
            } else {
                cells[noe] = element;
                noe++;
                return true;
            }
        }

        T remove(int index) {
            checkRange(index);
            T old = cells[index];
            noe--;
            System.arraycopy(cells, index+1, cells, index, noe-index);
            return old;
        }
        
        void checkRange(int index){
            if (index < 0 || index >= noe ) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + noe);
            }
        }

    }

    private class Location {

        Module module;
        int index;

        Location(Module module, int index) {
            this.module = module;
            this.index = index;
        }

        T get() {
            return module.get(index);
        }

        T set(T element) {
            return module.set(index, element);
        }

    }

    private final static int DEFAULT_BASE_SIZE = 32;
    private final static int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
    private final static double DEFAULT_FACTOR = 1.25;
    //private final static double DEFAULT_FACTOR = 1.0; // not dynamic

    private Module first_module;
    private Module last_module;

    private int size;

    private final int intial_mod_size;
    private final double increment;


    public ModularList() {
        this(DEFAULT_BASE_SIZE, DEFAULT_FACTOR);
    }
    
    
    
    
    public ModularList(int initial_capacity) {
        this(initial_capacity, DEFAULT_FACTOR);
    }
    
    private ModularList(int base_mod_size, double increment) {
        this.intial_mod_size = base_mod_size;
        this.increment = increment;
        initialize();
    }
    
    //TODO
    public ModularList(Collection<? extends T> c) {
        this();
        Objects.requireNonNull(c);
        
        Object[] collection = c.toArray();
        for(int i = 0; i<collection.length ; i++){
            
            
            
            
        }
        
    }

    private void initialize() {
        first_module = new Module(intial_mod_size);
        last_module = first_module;
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return (size() == 0);
    }

    @Override
    public boolean contains(Object o) {
        return find(o) != null;
    }

    @Override
    public Iterator<T> iterator() {
        return listIterator();
    }

    @Override
    public Object[] toArray() {
        return toArray(new Object[size]);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        T[] array = (T[]) new Object[size];
        Module aux = first_module;
        int ai = 0;
        do {
            System.arraycopy(aux.cells, 0, array, ai, aux.size());
            ai += aux.size();
            aux = aux.next;
        } while (aux != null);
        return array;
    }

    @Override
    public boolean add(T e) {
        return addToIndex(new Location(last_module, last_module.size()), e);
    }

    @Override
    public boolean remove(Object o) {
        Location r = find(o);
        return removeIndex(r) != null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return c.stream().noneMatch((o) -> (!contains((T) o)));
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return addAll(size, c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        boolean added = false;
        if (index == size) {
            for (T o : c) {
                added = add(o);
            }
        } else {
            for (T o : c) {
                add(index, o);
                index++;
                added = true;
            }
        }
        return added;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        if (c.size() < size()) {
            Module aux;
            aux = first_module;
            do {
                for (int i = 0; i < aux.size(); i++) {
                    if (c.contains(aux.get(i))) {
                        aux.remove(i);
                        i--;
                        changed = true;
                    }
                }
                aux = aux.next;
            } while (aux != null);
        } else {
            for (Object o : c) {
                changed = removeIndex(find(o)) != null;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        if (c.size() < size()) {
            Module aux;
            aux = first_module;
            do {
                for (int i = 0; i < aux.size(); i++) {
                    if (!c.contains(aux.get(i))) {
                        aux.remove(i);
                        i--;
                        changed = true;
                    }
                }
                aux = aux.next;
            } while (aux != null);
        } else {
            Location l;
            for (Object o : c) {
                l = find(o);
                if (l == null) {
                    changed = removeIndex(l) != null;
                }
            }
        }
        return changed;
    }

    @Override
    public void clear() {
        initialize();
    }

    @Override
    public T get(int index) {
        Location l = getLocation(index);
        return l.get();
    }

    @Override
    public T set(int index, T element) {
        return setIndexTo(getLocation(index), element);
    }

    @Override
    public void add(int index, T element) {
        if (index == size) {
            add(element);
        } else {
            addToIndex(getLocation(index), element);
        }
    }

    @Override
    public T remove(int index) {
        return removeIndex(getLocation(index));
    }

    @Override
    public int indexOf(Object o) {
        T obj = (T) o;
        Module aux = first_module;
        int index = 0;
        do {
            for (int i = 0; i < aux.size(); i++) {
                if (obj.equals(aux.get(i))) {
                    return index;
                }
                index++;
            }
            aux = aux.next;
        } while (aux != null);
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        Module aux = last_module;
        T obj = (T) o;
        int index = size - 1;
        while (aux.prev != null) {
            for (int i = aux.size() - 1; i >= 0; i--) {
                if (obj.equals(aux.get(i))) {
                    return index;
                }
                index--;
            }
            aux = aux.prev;
        }
        return -1;
    }

    @Override
    public ListIterator<T> listIterator() {
        return new ModularListIterator(0);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new ModularListIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        if (fromIndex > toIndex) {
            throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")");
        }
        List<T> sublist = new ModularList<>(intial_mod_size, increment);
        Iterator<T> it = listIterator(fromIndex);
        int c = fromIndex;
        while (c < toIndex) {
            sublist.add(it.next());
            c++;
        }
        return sublist;
    }

    private T setIndexTo(Location location, T element) {
        return location.set(element);
    }

    private boolean addToIndex(Location location, T element) {
        boolean add;
        Module aux = location.module;
        int index = location.index;
        if (last_module == aux && aux.size() == index) { // To add the last element
            if (aux.size() == aux.moduleSize()) { // If the last module is full
                int new_capacity = (int) (((double) aux.moduleSize()) * increment);
                if(new_capacity > MAX_ARRAY_SIZE){
                    new_capacity = MAX_ARRAY_SIZE;
                }
                aux.next = new Module(new_capacity);
                aux.next.prev = aux;
                aux = aux.next;
                last_module = aux;
            }
            add = aux.add(element);
        }else if (aux.size() < aux.moduleSize()) { // If there is space available in the module
            add = aux.add(index, element);
        } else { // If there is NO space available in the module
            int free_prev = aux.prev != null? aux.prev.free():0;
            int free_next = aux.next != null? aux.next.free():0;
            if (free_prev == 0 && free_next == 0) { // No space prev neither next
                Module new_module = new Module(aux.moduleSize());
                new_module.prev = aux;
                if (aux == last_module) {
                    last_module = new_module;
                } else {
                    new_module.next = aux.next;
                    new_module.next.prev = new_module;
                }
                aux.next = new_module;
                aux.noe = aux.size() / 2;
                new_module.noe = aux.free();
                System.arraycopy(aux.cells, aux.size(), new_module.cells, 0, aux.free());
                add = index > aux.moduleSize() / 2 ? new_module.add(index - aux.moduleSize() / 2, element):aux.add(index, element);
            } else if (free_prev == 0 || (aux!=last_module && (index > (index-aux.size()+aux.next.size())))) { // No space prev but space next or better to expand to next
                aux.next.add(0, aux.remove(aux.size()-1));
                add = aux.add(index, element);
            } else if (index == 0 && aux.prev != null && aux.prev.size() < aux.moduleSize()) { // If index=0 and the previous module has space
                add = aux.prev.add(element);
            } else{ // No space next but space prev or better to expand to prev
                aux.prev.add(aux.get(0));
                index--;
                System.arraycopy(aux.cells, 1, aux.cells, 0, index);
                aux.set(index, element);
                add = true;
            }
        }
        size++;
        return add;
    }

    private T removeIndex(Location l) {
        Module aux = l.module;
        T value = aux.remove(l.index);
        size--;
        if (aux.size() == 0) {
            if (aux.prev != null) {
                aux.prev.next = aux.next;
                if (aux.next != null) {
                    aux.next.prev = aux.prev;
                }
                if (aux == last_module) {
                    last_module = aux.prev;
                }
            } else if (aux.next != null) {
                aux = aux.next;
                aux.prev = null;
                first_module = aux;
            }
        }
        return value;
    }

    private Location getLocation(int index) {
        if (index < 0 || index >= size ) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Module aux = null;
        if (index <= size / 2) { // simple search
            aux = first_module;
            while (index >= aux.size()) {
                index -= aux.size();
                aux = aux.next;
            }
        } else { // Reverse search
            aux = last_module;
            int index_ = size;
            while (index_ - index > aux.size()) {
                index_ -= aux.size();
                aux = aux.prev;
            }
            index = aux.size() - (index_ - index);
        }
 
        return new Location(aux, index);
    }
    
    private Location find(Object obj) {
        Module aux = first_module;
        do {
            for (int i = 0; i < aux.size(); i++) {
                if (((T) obj).equals(aux.get(i))) {
                    return new Location(aux, i);
                }
            }
            aux = aux.next;
        } while (aux != null);
        return null;
    }

    private class ModularListIterator implements ListIterator<T> {

        private int index;
        private final Location location;

        private ModularListIterator(int index) {
            this.index = index;
            this.location = getLocation(index);
        }

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public T next() {
            T value = location.get();
            index++;
            location.index++;
            if (location.index == location.module.size()) {
                location.index = 0;
                location.module = location.module.next;
            }
            return value;
        }

        @Override
        public boolean hasPrevious() {
            return index > 0;
        }

        @Override
        public T previous() {
            index--;
            location.index--;
            if (location.index == -1) {
                location.index = 0;
                location.module = location.module.prev;
            }
            return location.get();
        }

        @Override
        public int nextIndex() {
            return index + 1;
        }

        @Override
        public int previousIndex() {
            return index - 1;
        }

        @Override
        public void remove() {
            removeIndex(location);
        }

        @Override
        public void set(T e) {
            setIndexTo(location, e);
        }

        @Override
        public void add(T e) {
            addToIndex(location, e);
        }

    }

}
