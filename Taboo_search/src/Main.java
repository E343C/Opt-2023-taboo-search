import java.util.*;


public class Main
{
	public static final int GLOBAL_CYCLE_NUMBER=6;
	public static final int LOCAL_CYCLE_NUMBER=50;
	public static final int TABOO_CONST=4;

	public static Product best;
	public static int bestOut;
	public static int currentOut;
	public static List<Product> products=new ArrayList<>();

	/**
	 * This method initializes a list of products, performs swaps to find a locally optimal solution,
	 * and repeats the process for a specified number of global and local cycles. The final best
	 * optimal order and its optimization value are printed.
	 *
	 * @param args Command line arguments (not used in this implementation).
	 */
	public static void main(String[] args)
	{
		// Initialize a list of Product ordered by the velocity of productions
		products.add(new Product(4, 1, 8, 2, 0));
		products.add(new Product(6, 1, 22, 3, 0));
		products.add(new Product(2, 1, 12, 4, 0));
		products.add(new Product(1, 1, 9, 6, 0));
		products.add(new Product(3, 1, 15, 8, 0));
		products.add(new Product(5, 1, 20, 10, 0));

		//Collections.shuffle(products);

		List<Product> bestOrder=new ArrayList<>(products);
		bestOut=calcOptim(bestOrder);
		printOut(bestOrder, bestOut, 0);
		int li=1;// Local cycle counter
		int i; // Global cycle counter

		for (i=1; i<=GLOBAL_CYCLE_NUMBER; i++)
		{
			for (li=1; li<=LOCAL_CYCLE_NUMBER; li++)
			{
				products=new ArrayList<>(swap(products));
				if (currentOut<bestOut) // Update the best order and its optimization value if a better solution is found
				{
					bestOut=currentOut;
					bestOrder=new ArrayList<>(products);
				}
				if (li==10 && i==1) // Print details of the best order after the first 10 iterations.
					printOut(bestOrder, bestOut, li);
				updateTaboo(products);
			}
			for (Product p : products)  // Reset taboo status for all products after completing local cycles
				p.setTaboo(0);
			Collections.shuffle(products); // Shuffle the products for the next global cycle
		}
		printOut(bestOrder, bestOut, (li-1)*(i-1));  // Print the final details of the best order and its optimization value
	}

	/**
	 * This method calculates the optimization value based on the input products.
	 * It iterates through each product in the list, updating the cumulative workload and
	 * calculating the optimization value. In doing this it's also calculate the product that is satisfied with greater advanced.
	 *
	 * @param order A list of Product.
	 * @return The optimization value for the given list of products.
	 */
	public static int calcOptim(List<Product> order)
	{
		int c=0; // Cumulative workload
		int opt=0; // Optimization value
		int bestValue=Integer.MAX_VALUE;
		for (Product o : order)
		{
			c+=o.getP();
			int calc=c-o.getD();
			int value=o.getW()*calc;
			if (calc>0)
				opt+=o.getW()*calc;
			if (value<bestValue) //Check if a better solution is found
			{
				bestValue=value;
				best=o;
			}
		}
		return opt;
	}

	/**
	 * This method attempts to find a locally optimal solution by swapping a single pair of products.
	 * It iterates through the list and evaluates the optimization value after each swap. The method returns
	 * a new order that represents a locally optimal solution.
	 *
	 * @param order The original list of Product representing the initial order.
	 * @return A new list of Product objects representing a local optimum.
	 */
	public static List<Product> swap(List<Product> order)
	{
		List<Product> bestLocalOrder=new ArrayList<>(order);
		int bestIndex=order.indexOf(best);
		Product bestNewValue=best;
		int localBestValue=Integer.MAX_VALUE;

		for (int i=bestIndex+1; i<order.size(); i++)
		{
			if (order.get(i).getTaboo()<=0) // Check if swapping is allowed based on taboo status
			{
				List<Product> copyOrder=new ArrayList<>(order);
				copyOrder.get(bestIndex).setTaboo(TABOO_CONST); // Set taboo value for the swapped product
				Collections.swap(copyOrder, bestIndex, i); //Swap two products in the list
				int opt=calcOptim(copyOrder);
				if (opt<localBestValue)  // Update the best local order if a better solution is found
				{
					bestLocalOrder=new ArrayList<>(copyOrder);
					bestNewValue=best;
					localBestValue=opt;
					currentOut=opt;
				}
			}
		}
		best=bestNewValue;
		return bestLocalOrder;
	}

	/**
	 * This method takes a list of products , the objective function value
	 * and the number of iterations performed. It then prints the product IDs,
	 * the number of iterations, and the objective function value of the best solution.
	 *
	 * @param order A list of Product representing the best current order.
	 * @param out   The objective function value of the best current solution.
	 * @param i     The number of iterations performed.
	 */
	public static void printOut(List<Product> order, int out, int i)
	{
		System.out.print("The best solution found after "+i+" iterations is:");
		for (Product o : order)
			System.out.print(" "+(o.getId()));
		System.out.println("\nWith objective function value of: "+out+"\n");
	}

	/**
	 * This method takes a list of Product and updates the taboo
	 * status of each product by decrementing its taboo counter by 1.
	 *
	 * @param order A list of Product objects.
	 */
	public static void updateTaboo(List<Product> order)
	{
		for (Product p : order)
		{
			p.setTaboo(p.getTaboo()-1);
		}
	}
}