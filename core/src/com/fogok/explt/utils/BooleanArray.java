/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.fogok.explt.utils;

/** A resizable, ordered or unordered float array. Avoids the boxing that occurs with ArrayList<Float>. If unordered, this class
 * avoids a memory copy when removing elements (the last element is moved to the removed element's position).
 * @author Nathan Sweet */
public class BooleanArray {
    private boolean[] items;
    private int size;
    private boolean ordered;

    /** Creates an ordered array with a capacity of 16. */
    public BooleanArray () {
        this(true, 128);
    }

    public BooleanArray (boolean ordered) {
        this(ordered, 16);
    }


    /** Creates an ordered array with the specified capacity. */
    public BooleanArray (int capacity) {
        this(true, capacity);
    }

    /** @param ordered If false, methods that remove elements may change the order of other elements in the array, which avoids a
     *           memory copy.
     * @param capacity Any elements added beyond this will cause the backing array to be grown. */
    public BooleanArray (boolean ordered, int capacity) {
        this.ordered = ordered;
        items = new boolean[capacity];
    }


    /** Creates a new ordered array containing the elements in the specified array. The capacity is set to the number of elements,
     * so any subsequent elements added will cause the backing array to be grown. */
    public BooleanArray (float[] array) {
        this(true, array, 0, array.length);
    }

    /** Creates a new array containing the elements in the specified array. The capacity is set to the number of elements, so any
     * subsequent elements added will cause the backing array to be grown.
     * @param ordered If false, methods that remove elements may change the order of other elements in the array, which avoids a
     *           memory copy. */
    public BooleanArray (boolean ordered, float[] array, int startIndex, int count) {
        this(ordered, count);
        size = count;
        System.arraycopy(array, startIndex, items, 0, count);
    }

    public void add (boolean value) {
        boolean[] items = this.items;
        if (size == items.length) items = resize(Math.max(8, (int)(size * 1.75f)));
        items[size++] = value;
    }


    public void addAll (float... array) {
        addAll(array, 0, array.length);
    }

    public void addAll (float[] array, int offset, int length) {
        boolean[] items = this.items;
        int sizeNeeded = size + length;
        if (sizeNeeded > items.length) items = resize(Math.max(8, (int)(sizeNeeded * 1.75f)));
        System.arraycopy(array, offset, items, size, length);
        size += length;
    }

    public boolean get (int index) {
        if (index >= size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + size);
        return items[index];
    }

    public void set (int index, boolean value) {
        if (index >= size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + size);
        items[index] = value;
    }








    /** Removes and returns the item at the specified index. */
    public boolean remove (int index) {
        if (index >= size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + size);
        boolean[] items = this.items;
        boolean value = items[index];
        size--;
        if (ordered)
            System.arraycopy(items, index + 1, items, index, size - index);
        else
            items[index] = items[size];
        return value;
    }


    /** Removes from this array all of elements contained in the specified array.
     * @return true if this array was modified. */
    public boolean removeAll (BooleanArray array) {
        int size = this.size;
        int startSize = size;
        boolean[] items = this.items;
        for (int i = 0, n = array.size; i < n; i++) {
            boolean item = array.get(i);
            for (int ii = 0; ii < size; ii++) {
                if (item == items[ii]) {
                    remove(ii);
                    size--;
                    break;
                }
            }
        }
        return size != startSize;
    }








    /** Reduces the size of the backing array to the size of the actual items. This is useful to release memory when many items have
     * been removed, or if it is known that more items will not be added.
     * @return {@link #items} */


    /** Increases the size of the backing array to accommodate the specified number of additional items. Useful before adding many
     * items to avoid multiple backing array resizes.
     * @return {@link #items} */
    public boolean[] ensureCapacity (int additionalCapacity) {
        int sizeNeeded = size + additionalCapacity;
        if (sizeNeeded > items.length) resize(Math.max(8, sizeNeeded));
        return items;
    }

    protected boolean[] resize (int newSize) {
        boolean[] newItems = new boolean[newSize];
        boolean[] items = this.items;
        System.arraycopy(items, 0, newItems, 0, Math.min(size, newItems.length));
        this.items = newItems;
        System.out.println("qqqqq" + newItems.length);
        return newItems;

    }




    static public FloatArray with (float... array) {
        return new FloatArray(array);
    }
}
