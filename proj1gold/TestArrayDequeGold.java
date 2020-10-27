import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {
    StudentArrayDeque<Integer> student = new StudentArrayDeque<>();
    ArrayDequeSolution<Integer> solution = new ArrayDequeSolution<>();
    public void testAddFirst(Integer i) {
        student.addFirst(i);
        solution.addFirst(i);
    }

    public void testAddLast(Integer i) {
        student.addLast(i);
        solution.addLast(i);
    }

    public void testRemoveFirst(String s) {
        assertEquals(s,solution.removeFirst(),student.removeFirst());
    }
    public void testRemoveLast(String s) {
        assertEquals(s,solution.removeLast(),student.removeLast());
    }

    @Test
    public void testDeque() {
        int size = 0;
        String info = "";

        for (int i = 0; i < 200; i +=1) {
            double r = StdRandom.uniform();
            Integer a = (int)StdRandom.uniform(1,11);
            if (size == 0) {
                if (StdRandom.uniform() > 0.5) {
                    info += "addLast("+a.toString()+")\n";
                    testAddLast(a);
                } else {
                    info += "addFirst("+a.toString()+")\n";
                    testAddFirst(a);
                }
                size += 1;
                continue;
            }
            if (r < 0.3) {
                info += "addFirst("+a.toString()+")\n";
                testAddFirst(a);
                size += 1;
            } else if(r < 0.6) {
                info += "addLast("+a.toString()+")\n";
                testAddLast(a);
                size += 1;
            } else if(r < 0.8) {
                info += "removeFirst()\n";
                testRemoveFirst(info);
                size -= 1;
            } else {
                info += "removeLast()\n";
                testRemoveLast(info);
                size -= 1;
            }
        }


    }

}
