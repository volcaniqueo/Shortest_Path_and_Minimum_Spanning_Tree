import java.util.Comparator;

public class ArrayComparator  implements Comparator<int[]>{
	
	
	public int compare(int[] first, int[] second) {
		if (first[1] < second[1])
			return -1;
		else if (first[1] > second[1])
			return 1;
		else
			return -1;
	}
	
	

}
