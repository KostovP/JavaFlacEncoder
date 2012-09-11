/*
 * Copyright (C) 2010  Preston Lacey http://javaflacencoder.sourceforge.net/
 * All Rights Reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package javaFlacEncoder;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The purpose of this class is to provide a source for reusable int arrays.
 * When using large numbers of arrays in succession, it is inefficient to
 * constantly go in an allocate/free loop. This way, we may pass a single,
 * thread-safe recycler to all objects. No matter where the arrays end their
 * life, we can then add it to the same resource store.
 *
 * @author Preston Lacey
 */
public class ArrayRecycler {
    BlockingQueue<int[]> usedIntArrays;

    ArrayRecycler() {
        usedIntArrays = new LinkedBlockingQueue<int[]>(32);
    }

    public void add(int[] array) {
    	usedIntArrays.offer(array);
    }

    // TODO: Doesn't this grow forever? If arrays too small, never get removed.

    /**
     *
     * @param size the minimum size the array must be
     * @return
     */
    public int[] getArray(int size) {
       final int[] found = usedIntArrays.poll();
       int[] result = found;
       if(found == null) {
          result = new int[size];
          //System.err.println("Created new int array from null");
       }
       else if(found.length < size) {
          usedIntArrays.offer(found);
          result = new int[size];
          //System.err.println("created new int array from bad size");
       }
       return result;
    }

}