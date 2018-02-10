package model;

///////////This class is created to get the input sequence data from dataset
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sequence extends Vector<ItemSet> {

    private int index;
    static int test_number = 0;
    int classlabel;

    public Sequence(String str, int i, int classlabel) {
        ////////*
        //test(str);
        //**************

        String[] aa = str.split("\\ ");
//        for (String s : aa) {
//            System.out.print(s);
//        }
        index = i;
        for (int j = 0; j < aa.length; j++) {
            this.add(new ItemSet(aa[j]));
        }
        this.classlabel = classlabel;
    }

    public void print() {
        System.out.println("-------------The new sequence ----------------");
        for (int i = 0; i < this.size(); i++) {
            System.out.println((i + 1) + "--" + this.get(i));
        }
        System.out.println("The classlabel is" + classlabel);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        int size = this.size();
        String content = "Sequense: [";
        for (int i = 0; i < size; i++) {
            if (i == size - 1) {
                content += this.get(i).toString() + "";
            } else {
                content += this.get(i).toString() + " ";
            }
        }
        return content + "]";

    }
//	public void test(String str){
//		
//		Pattern pattern = Pattern.compile(".*54.*54.*54.*132.*132.*54.*");
//		Matcher matcher = pattern.matcher(str);
//		boolean b= matcher.matches();
//		if(b)
//			test_number++;
//		System.out.println("now the test_number is "+test_number);
//	}
//	public static int getTest_number() {
//		return test_number;
//	}
//	public static void setTest_number(int test_number) {
//		Sequence.test_number = test_number;
//	}

}
