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
        assertEquals(s,student.removeFirst(),solution.removeFirst());
    }
    public void testRemoveLast(String s) {
        assertEquals(s,student.removeLast(),solution.removeLast());
    }

    @Test
    public void testDeque() {
        int size = 0;
        String info = "";

        for (int i = 0; i < 50; i +=1) {
            double r = StdRandom.uniform();
            Integer a = (int)StdRandom.uniform(1,11);
            if (size == 0) {
                testAddLast(a);
                info += "testAddLast("+a.toString()+")\n";
                size += 1;
                continue;
            }
            if (r < 0.25) {
                testAddFirst(a);
                info += "testAddFirst("+a.toString()+")\n";
                size += 1;
            } else if(r < 0.5) {
                testAddLast(a);
                info += "testAddLast("+a.toString()+")\n";
                size += 1;
            } else if(r < 0.75) {
                testRemoveFirst(info);
                info += "testRemoveFirst("+a.toString()+")\n";
                size -= 1;
            } else {
                testRemoveLast(info);
                info += "testRemoveLast("+a.toString()+")\n";
                size -= 1;
            }
        }


    }

}
