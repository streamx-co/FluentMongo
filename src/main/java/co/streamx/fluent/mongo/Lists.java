package co.streamx.fluent.mongo;

import java.util.function.Predicate;

interface Lists {
    static <T> int indexOf(T[] array,
                           Predicate<T> predicate) {

        for (int i = 0; i < array.length; i++) {
            if (predicate.test(array[i]))
                return i;
        }
        
        return -1;
    }

    static <T> void reverse(T[] data) {
        for (int left = 0, right = data.length - 1; left < right; left++, right--) {
            // swap the values at the left and right indices
            T temp = data[left];
            data[left] = data[right];
            data[right] = temp;
        }
    }
}
