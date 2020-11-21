package byog.Core;

import edu.princeton.cs.algs4.StdOut;

public class testrandom {
    private int value = 5;
    void printValue() {
        System.out.println(value);
    }

    public static void main(String[] args) {
        testrandom tr = new testrandom();
        System.out.println(tr.value);
        tr.printValue();
    }
}
