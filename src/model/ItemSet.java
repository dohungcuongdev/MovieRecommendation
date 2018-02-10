package model;

///////////This class is created to get the input sequence data from dataset
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemSet extends Vector<Integer> {

    private int number;

    ItemSet() {
    }

    ItemSet(int i) {
        this.add(i);
    }

    ItemSet(Integer i) {
        this.add(i);
    }

    ItemSet(String i) {
        if (i.startsWith("(") && i.endsWith(")")) {
            String[] aa = i.substring(1, (i.length() - 1)).split(",");
            Vector tmp = new Vector();
            for (int j = 0; j < aa.length; j++) {
                this.add(Integer.valueOf(aa[j]));
                // System.out.println("WithinItemset--"+aa[j]);
            }

        } else {
            if (this.IsNum(i)) {
                this.add(Integer.valueOf(i));
            } else {
                System.out.println("Error: " + i + " is not a valid itemset");
            }
        }
    }

    public void print() {
        System.out.println("-------------The new sequence ----------------");
        for (int i = 0; i < this.size(); i++) {
            System.out.println((i + 1) + "--" + this.get(i));
        }
    }

    public int getIndex() {
        return number;
    }

    public void setIndex(int index) {
        this.number = index;
    }

    public static boolean IsNum(String strin) {
        String s = strin.replaceAll("[0-9;]+", "");
        if (s.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        int size = this.size();
        String content = "[ItemSet: ";
        for (int i = 0; i < size; i++) {
            if(i == size - 1)
                content += get(i) + "";
            else 
                content += get(i) + " ";
        }
        return content + "]";
    }

}
