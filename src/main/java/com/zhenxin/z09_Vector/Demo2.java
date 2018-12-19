package z09_Vector;

public class Demo2 {
    public static void main(String[] args) {
        int[] arr = {7,3,3,4,7,7};
        int v = 0;
        for (int i = 0; i < arr.length; i++) {
            int temp = v;
            v ^= arr[i];
            System.out.println(i + ":" + temp + "^" + arr[i] + "=" + v);
        }
        System.out.println("sum:" + v);
    }
}
