package logic.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;



public class SortListsExample {
    public static void main(String[] args) {
        // Example: List of lists of MyObject
        List<List<MyObject>> listOfLists = new ArrayList<>();
        System.out.println(Arrays.stream("A;B;C;D".split(";")).toList());
        // Populate lists with MyObject
        List<MyObject> list1 = new ArrayList<>(List.of(new MyObject(3), new MyObject(3),new MyObject(3), new MyObject(2)));
        List<MyObject> list2 = new ArrayList<>(List.of(new MyObject(3), new MyObject(3), new MyObject(1), new MyObject(5)));
        List<MyObject> list3 = new ArrayList<>(List.of(new MyObject(3), new MyObject(2), new MyObject(3), new MyObject(4)));
        
        listOfLists.add(list1);
        listOfLists.add(list2);
        listOfLists.add(list3);
        
        // Create an index array that represents the original positions
        List<Integer> indices = new ArrayList<>(IntStream.range(0, list1.size()).boxed().toList());
        System.out.println(indices);
        // Sort the indices based on custom comparator
        indices.sort((i1, i2) -> {
            // Compare the first list
            int cmp = Integer.compare(list1.get(i1).getValue(), list1.get(i2).getValue());
            if (cmp != 0) {
                return cmp;
            }
            // If values are equal in the first list, compare the other lists
            for (int i = 1; i < listOfLists.size(); i++) {
                List<MyObject> otherList = listOfLists.get(i);
                cmp = Integer.compare(otherList.get(i1).getValue(), otherList.get(i2).getValue());
                if (cmp != 0) {
                    return cmp;
                }
            }
            return 0; // Fully equal if no differences found in all lists
        });
        
        // Reorder all the lists based on the sorted indices
        for (List<MyObject> list : listOfLists) {
            reorderListByIndices(list, indices);
        }
        
        // Printing the sorted lists
        for (List<MyObject> list : listOfLists) {
            list.forEach(obj -> System.out.print(obj.getValue() + " "));
            System.out.println();
        }
        
        System.out.println(indices);
    }
    
    // Reorder a list based on the provided sorted indices
    private static void reorderListByIndices(List<MyObject> list, List<Integer> sortedIndices) {
        List<MyObject> temp = new ArrayList<>(list);
        for (int i = 0; i < sortedIndices.size(); i++) {
            list.set(i, temp.get(sortedIndices.get(i)));
        }
    }
}