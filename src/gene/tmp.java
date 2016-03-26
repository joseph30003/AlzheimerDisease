package gene;

import java.util.Arrays;

public class tmp {

	
	
	public static int[] removeDuplicates(int[] A) {
		if (A.length < 2)
			return A;
	 
		int j = 0;
		
	 
		for(int i=1;i<A.length;i++){
			if (A[i] == A[j]) {
				
			} else {
				j++;
				A[j] = A[i];
				
			}
		}
	 
		int[] B = Arrays.copyOf(A, j + 1);
	 
		return B;
	}
	 
	public static void main(String[] args) {
		int[] arr = { 1, 2, 2, 3, 3 };
		arr = removeDuplicates(arr);
		
		System.out.println(Arrays.toString(arr));
	}
}
