import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

public class project3main {

	public static void main(String[] args) throws FileNotFoundException {
		
		Scanner in = new Scanner(new File(args[0]));
		PrintStream out = new PrintStream(new File(args[1]));
		
		HashMap<Integer,ArrayList<int[]>> cCities = new HashMap<Integer,ArrayList<int[]>>();
		HashMap<Integer, int[]> cCitiesParentPath = new HashMap<Integer, int[]>();  // If the parent's id = 0, it means it is null
		HashMap<Integer, Boolean> cCitiesFinished = new HashMap<Integer, Boolean>();
		HashMap<Integer,ArrayList<int[]>> dCities = new HashMap<Integer,ArrayList<int[]>>();
		HashMap<Integer, int[]> dCitiesParentCost = new HashMap<Integer, int[]>(); //  If the parent's id = 0, it means it is null
		HashMap<Integer, Boolean> dCitiesFinished = new HashMap<Integer, Boolean>();
		
		
		
		
		
		String myLine = in.nextLine();
		String[] myOrder = myLine.split(" ");
		int limit = Integer.parseInt(myOrder[0]);
		String numCities = in.nextLine();
		String[] numCitiesArray = numCities.split(" ");
		int mecnun = 0;
		int leyla = 0;
		for (int i = 0; i <= Integer.parseInt(numCitiesArray[0]); i++) {
			String data = in.nextLine();
			String[] dataArray = data.split(" ");
			int type = 0; // 1 for cCities map, -1 for dCities map
			if (i == 0) {
				mecnun = Integer.parseInt(dataArray[0].substring(1));
				leyla = Integer.parseInt(dataArray[1].substring(1));
				continue;
			}
			int id = 0;
			int leyla_error = 0;
			ArrayList<int[]> pairs = new ArrayList<int[]>();
			for (int j = 0; j < dataArray.length; j++) {
				if (j == 0) {
					if (dataArray[0].substring(0, 1).equals("c")) {
						if (Integer.parseInt(dataArray[0].substring(1)) == leyla) {
							cCities.put(leyla, null);
							int[] parentPath = new int[2];
							parentPath[0] = 0;
							parentPath[1] = Integer.MAX_VALUE;
							cCitiesParentPath.put(leyla, parentPath);
							cCitiesFinished.put(leyla, false);
							id = -1; // id -1 stands for leyla's city
							type = -1;
							leyla_error = 1;  // if there is connection from leyla's city to some other c city
						}
						else {
							id = Integer.parseInt(dataArray[0].substring(1));
							type = 1;
						}
					}
					else if (dataArray[0].substring(0, 1).equals("d")) {
						id = Integer.parseInt(dataArray[0].substring(1));
						type = -1;
					}
					continue;
				}
				int[] pair = new int[2];
				pair[0] = Integer.parseInt(dataArray[j].substring(1));
				pair[1] = Integer.parseInt(dataArray[j + 1]);
				j++;
				if (pair[0] == id)
					continue;
				if (leyla_error == 1) {
					if (dataArray[j - 1].substring(0, 1).equals("c"))
						continue;
				}
				pairs.add(pair);
			}
			if (type == 1) {
				if (id == mecnun) {
					cCities.put(id, pairs);
					int[] parentPath = new int[2];
					parentPath[0] = 0;
					parentPath[1] = 0;
					cCitiesParentPath.put(id, parentPath);
					cCitiesFinished.put(id, false);
					
				}
				else {
					cCities.put(id, pairs);
					int[] parentPath = new int[2];
					parentPath[0] = 0;
					parentPath[1] = Integer.MAX_VALUE;
					cCitiesParentPath.put(id, parentPath);
					cCitiesFinished.put(id, false);
				}
				continue;
			}
			else if (type == -1) {
				if (id == -1) {
					dCities.put(id, pairs);
					int[] parentCost = new int[2];
					parentCost[0] = 0;
					parentCost[1] = 0;
					dCitiesParentCost.put(id, parentCost);
					dCitiesFinished.put(id, false);
				}
				else {
					dCities.put(id, pairs);
					int[] parentCost = new int[2];
					parentCost[0] = 0;
					parentCost[1] = Integer.MAX_VALUE;
					dCitiesParentCost.put(id, parentCost);
					dCitiesFinished.put(id, false);
				}
				continue;
			}	
		}
		
		for (Integer myint5 : dCitiesFinished.keySet()) {  // To make the dCities undirected graph
			ArrayList<int[]> beUndirected = dCities.get(myint5);
			for (int[] becUndirected : beUndirected) {
				int[] monPair = new int[2];
				monPair[0] = myint5;
				monPair[1] = becUndirected[1];
				if (!(dCities.get(becUndirected[0]).contains(monPair)))
					dCities.get(becUndirected[0]).add(monPair);
			}
		}
		
		ArrayList<int[]> sPList = new ArrayList<int[]>();
		for (Integer myint: cCitiesParentPath.keySet()) {
			int[] mArray = new int[2];
			mArray[0] = myint; // id
			mArray[1] = cCitiesParentPath.get(myint)[1]; // path
			sPList.add(mArray);
		}
		
		PriorityQueue<int[]> shortestPath = new PriorityQueue<int[]>(new ArrayComparator()); // ArrayComparator compares according to the second element
		shortestPath.addAll(sPList);
		
		while (!(shortestPath.isEmpty())) {  // For shortest path
			int[] toPop = shortestPath.poll();  // index 0 for id, index 1 for path length
			if (toPop[0] == leyla)
				break;
			if (cCitiesFinished.get(toPop[0]))
				continue;
			int[] currentParentPath = cCitiesParentPath.get(toPop[0]);
			cCitiesFinished.replace(toPop[0], true);
			ArrayList<int[]> myArrayList = cCities.get(toPop[0]);
			for (int k = 0; k < myArrayList.size(); k++) {
				int[] myEdge = myArrayList.get(k); // index 0 for id, index 1 for weight
				if (currentParentPath[1] + myEdge[1] < cCitiesParentPath.get(myEdge[0])[1]) {
					int[] nouvelleBest = new int[2];
					int[] toBeAdded = new int[2];
					nouvelleBest[0] = toPop[0];
					nouvelleBest[1] = currentParentPath[1] + myEdge[1];
					cCitiesParentPath.replace(myEdge[0], nouvelleBest);
					toBeAdded[0] = myEdge[0];
					toBeAdded[1] = currentParentPath[1] + myEdge[1];
					shortestPath.add(toBeAdded);
				}
			}
		}
		
		if (cCitiesParentPath.get(leyla)[1] == Integer.MAX_VALUE || cCitiesParentPath.get(leyla)[1] < 0) {
			out.println("-1");
			out.println("-1");
			out.close();
			return;
		}
		else {
			ArrayList<Integer> shortestPathList = new ArrayList<Integer>();
			for (int i = leyla; i < Integer.MAX_VALUE; i = cCitiesParentPath.get(i)[0]) {
				shortestPathList.add(i);
				if (cCitiesParentPath.get(i)[0] == 0)
					break;
			}
			int lengthPath = cCitiesParentPath.get(leyla)[1];
			for (int i = shortestPathList.size() - 1; i >= 0; i--) {
				out.print("c");
				out.print(shortestPathList.get(i));
				out.print(" ");
			}
			out.println();
			if (lengthPath > limit) {
				out.print("-1");
				out.close();
				return;
			}
		}
		
		
		ArrayList<int[]> MSTList = new ArrayList<int[]>();
		for (Integer myint: dCitiesParentCost.keySet()) {
			int[] mArray = new int[2];
			mArray[0] = myint; // id
			mArray[1] = dCitiesParentCost.get(myint)[1]; // cost
			MSTList.add(mArray);
		}
		
		PriorityQueue<int[]> minSpanning = new PriorityQueue<int[]>(new ArrayComparator());  // ArrayComparator compares according to the second element
		minSpanning.addAll(MSTList);
		
		while (!(minSpanning.isEmpty())) {  // For minimum spanning tree
			int[] toPop = minSpanning.poll();  // index 0 for id, index 1 for cost
			if (dCitiesFinished.get(toPop[0]))
				continue;
			ArrayList<int[]> myArrayList = dCities.get(toPop[0]);
			if (myArrayList.isEmpty())
				continue;
			else
				dCitiesFinished.replace(toPop[0], true);
			for (int k = 0; k < myArrayList.size(); k++) {
				int[] myEdge = myArrayList.get(k); // index 0 for id, index 1 for weight
				if (dCitiesFinished.get(myEdge[0]))
					continue;
				if (myEdge[1] < dCitiesParentCost.get(myEdge[0])[1]) {
					int[] nouvelleBest = new int[2];
					int[] toBeAdded = new int[2];
					nouvelleBest[0] = toPop[0];
					nouvelleBest[1] = myEdge[1];
					dCitiesParentCost.replace(myEdge[0], nouvelleBest);
					toBeAdded[0] = myEdge[0];
					toBeAdded[1] = myEdge[1];
					minSpanning.add(toBeAdded);
				}
			}
		}
		
		if (dCitiesFinished.values().contains(false)) {
			out.print("-2");
			out.close();
			return;
		}
		else {
			int totalTax = 0;
			for (Integer myint9: dCitiesParentCost.keySet()) {
				totalTax += dCitiesParentCost.get(myint9)[1];
			}
			out.print(totalTax * 2);
			out.close();
			return;	
		}
		
		
		
	}
}
