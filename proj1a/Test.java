public class Test {
    public static void main(String[] args) {
        int[] arr1 = new int[]{1,2,3,4,5};
        int[] arr2 = new int[]{3,4,5,6,7};
        System.arraycopy(arr1,0,arr2,0,0);
        System.out.print(arr2[0]);
    }
}
