package gene;

import java.util.Arrays;

public class tmp {

public static void quickSort(int[] arr, int s, int e) {

	    if (s < e) {
	        int pivot = findPivot(arr, s, e);
	        System.out.println(pivot);
	        // sort left
	        quickSort(arr, s, pivot - 1);
	        // sort right
	        quickSort(arr, pivot + 1, e);
	    }

	}

	public static int findPivot(int[] arr, int s, int e) {
	    int cout=1;
		int p = arr[e];
	    int i = s;
	    for (int j = s; j < e; j++) {
	        if (arr[j] == p) {
	        	 
	            swap(arr, i, j);
	            cout++;
	            i = i + 1;
	        }
	    }
	    swap(arr, e, i);
	    System.out.println(p+"has "+cout);
	    return i;
	}

	public static void findDuplication(int[] arr){
		
	  int k=0;
	  for(int i=0 ; i<arr.length;){
		
		  k=i+1;
		  for(int j=i+1; j<arr.length; j++){
			   if(arr[i]==arr[j]){
				 
				 swap(arr,j,k);
				  k++; 
				 }
		 
			 		  
		  }
		   
		i=k;  
		  
	  }
	
		
		
	}
	
	
	
	
	public static void swap(int[] arr, int i, int j) {
	    int t = arr[i];
	    arr[i] = arr[j];
	    arr[j] = t;
	}
	
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
		
		int[] arr = { 5, 2, 4, 2, 3, 7, 8, 1 ,2, 1 , 10 };
		//quickSort(arr,0,arr.length-1);
		findDuplication(arr);
		System.out.println(Arrays.toString(arr));
		int[] br=removeDuplicates(arr);
		
		System.out.println(Arrays.toString(br));
	}
}
