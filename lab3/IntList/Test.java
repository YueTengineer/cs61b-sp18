public class Test {
    public void change(int a) {
        a = 6;
    }

    public static void main(String[] args) {
        Test t = new Test();
        int a = 0;
        t.change(a);
        System.out.println(a);
    }
}
