import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*This class also calculates the complexity of the code which is usually equals to n(n-1)/2
*/
public class SelectionSort{
    public static void main(String[] s) throws IOException {
        BufferedReader br =
            new BufferedReader(new InputStreamReader(System.in));
        String vals = br.readLine();
        char a[] = vals.toCharArray();
        int ab[] = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ab[i] = a[i];
        }
        selectionSort(ab);

    }

    private static void selectionSort(int[] ab) {
        int min;
        int count = 0;
        for (int i = 0; i < ab.length; i++) {
            min = i;
            for (int j = i + 1; j < ab.length; j++) {
                if (ab[j] < ab[min]) {
                    min = j;
                }
                count++;
            }
            if (min != i) {
                int temp = ab[min];
                ab[min] = ab[i];
                ab[i] = temp;
            }
            for (int k = 0; k < ab.length; k++) {
                System.out.print((char)ab[k]);
            }
            System.out.println(""+count);
        }


    }
    
    }
