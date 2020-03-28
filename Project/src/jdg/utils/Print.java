package jdg.utils;

/**
 * Provides methods for printing/formatting outputs
 *
 * @author Luca Castelli Aleardi (Ecole Polytechnique, 2017)
 */
public class Print {
	
	/**
	 * Print a (column) vector
	 * 
	 * @param precision  number of digits to print (for a matrix)
	 */
	public static void print(String msg, double[] a, int precision, boolean verbosity) {
		if(verbosity==false)
			return;
		if(msg!=null)
			System.out.println(msg);
		
		for(int i=0;i<a.length;i++) {
			String format="%."+precision+"f";
			String s=String.format(format,a[i]);
			System.out.println(s+"\t");
		}
		System.out.println();
	}

	/**
	 * Print a (dense) matrix
	 * 
	 * @param precision  number of digits to print (for a matrix)
	 */
	public static void print(String msg, double[][] m, int precision, boolean verbosity) {
		if(verbosity==false)
			return;
		if(msg!=null)
			System.out.println(msg);
		
		for(int i=0;i<m.length;i++) {
			for(int j=0;j<m[i].length;j++) {
				String format="%."+precision+"f";
				String s=String.format(format, m[i][j]);
				System.out.print(s+" \t");
			}
			System.out.println("");
		}
		System.out.println("");
	}

}
