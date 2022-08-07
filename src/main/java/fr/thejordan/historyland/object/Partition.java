package fr.thejordan.historyland.object;

import java.util.AbstractList;
import java.util.ArrayList;

public final class Partition<T> extends AbstractList<ArrayList<T>> {

    private final ArrayList<T> list;
    private final int chunkSize;

    public Partition(ArrayList<T> list, int chunkSize) {
        this.list = new ArrayList<>(list);
        this.chunkSize = chunkSize;
    }

    public static <T> Partition<T> ofSize(ArrayList<T> list, int chunkSize) {
        return new Partition<>(list, chunkSize);
    }

    @Override
    public ArrayList<T> get(int index) {
        int start = index * chunkSize;
        int end = Math.min(start + chunkSize, list.size());
        if (start > end)
            throw new IndexOutOfBoundsException("Index " + index + " is out of the list range <0," + (size() - 1) + ">");
        return new ArrayList<>(list.subList(start, end));
    }

    @Override
    public int size() {
        return (int) Math.ceil((double) list.size() / (double) chunkSize);
    }
}