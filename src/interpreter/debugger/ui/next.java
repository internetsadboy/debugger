import java.util.*;
public class next {
	public static void main(String[] args) {
		Scanner keyboard = new Scanner(System.in);
		String line = keyboard.nextLine();
		String[] values = line.split("\\s+");
		//System.out.println(values[0]);
		int i = 1; 
		Vector<Integer> brks = new Vector<Integer>();
		while(i < values.length) {
			brks.add(Integer.parseInt(values[i]));
			System.out.println(brks.get(i-1)+" ");
			i++;
		}
		System.out.print("entered: " + Arrays.toString(values) + "\n");
	}
}