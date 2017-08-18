package com.ood.clean.waterball.teampathy.MyUtils;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;


public class IndexSet<E> implements Set<E> {
    private Set<E> elementData;

    public IndexSet(Set<E> elementData) {
        this.elementData = elementData;
    }

    @Override
    public int size() {
        return elementData.size();
    }

    @Override
    public boolean isEmpty() {
        return elementData.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return elementData.contains(o);
    }

    @NonNull
    @Override
    public Iterator<E> iterator() {
        return elementData.iterator();
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return elementData.toArray();
    }

    @NonNull
    @Override
    public <T> T[] toArray(@NonNull T[] a) {
        return elementData.toArray(a);
    }

    @Override
    public boolean add(E e) {
        return elementData.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return elementData.remove(o);
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> c) {
        return elementData.containsAll(c);
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends E> c) {
        return elementData.addAll(c);
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> c) {
        return elementData.retainAll(c);
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> c) {
        return elementData.removeAll(c);
    }

    @Override
    public void clear() {
        elementData.clear();
    }

    public E get(int index){
        int i = 0;
        for ( E e : elementData )
            if (i++ == index)
                return e;

        throw new IndexOutOfBoundsException();
    }

    public void update(E e){
        elementData.remove(e);
        elementData.add(e);
    }
}
