package app;

import java.util.Arrays;

import scpsolver.problems.LPSolution;
import scpsolver.problems.LPWizard;
import scpsolver.problems.LPWizardConstraint;

public class Main {
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{
		// exercice1();
		// exercice2();
		exercice3();
	}

	public static void exercice1() {
		// 1. Générer une matrice de payoffs
		double[][] matricePayoff1 = new double[][] {{30, -2},
												    {-12, 31}};
		findNashEquilibrium(matricePayoff1);										    
	}
	
	public static LPSolution findNashEquilibrium(double[][] matrice) {
		LPWizard linearProgram = new LPWizard(); 
		
		LPWizardConstraint c;
		linearProgram.plus("z", 1.0); // On maximise z
		
		// For the row player...
		for(int i = 0; i < matrice[0].length; i++) {
			c = linearProgram.addConstraint("colonne_" + i, 0, "<=");
			c.plus("z", -1);
			for(int j = 0; j < matrice.length; j++) {
				c.plus("p_" + j, matrice[j][i]);
			}
		}
		c = linearProgram.addConstraint("sum_proba", 1, "=");
		
		for(int i = 0; i < matrice.length; i++) {
			c.plus("p_" + String.valueOf(i), 1);
			linearProgram.addConstraint("cnstrnt p_" + i, 0, "<=").plus("p_" + i);
		}
		
		linearProgram.setMinProblem(false);
		
		LPSolution sol = linearProgram.solve();
		
		System.out.println("Solution : ");
		System.out.println("Expected payoffs : ");
		for(int i = 0; i < matrice[0].length; i++) {
			System.out.print(" -> If player C plays column " + i + ", expected payoff = " );
			double sum = 0;
			for(int j = 0; j < matrice.length; j++) {
				sum += (matrice[j][i] * sol.getDouble("p_" + j));
			}
			System.out.println(sum);
		}

		System.out.println("Probabilities at equilibrium : ");
		for(int i = 0; i < matrice.length; i++) {
			System.out.println("p_" + i + " = " + sol.getDouble("p_" + i));
		}
		return sol;
	}
	
	private static void exercice2() {
		// ... k = 1
		// double[][] payoffMatrix1 = generatePayoffMatrix(1, 2);
		// printMatrix(payoffMatrix1);

		// ... k = 1.5
		// double[][] payoffMatrix15 = generatePayoffMatrix(1.5, 3);	
		// printMatrix(payoffMatrix15);
		
		// ... k = 2
		// double[][] payoffMatrix2 = generatePayoffMatrix(2, 4);
		// printMatrix(payoffMatrix2);
	}

	private static void exercice3() {
		double[][] payoffMatrixk1 = generatePayoffMatrix(1, 2);
		printMatrix(payoffMatrixk1);
		findNashEquilibrium(payoffMatrixk1);
		
	}
	
	
	
	private static void printMatrix(double[][] matrix) {
		for(int i = 0; i < matrix.length; i++) {
			System.out.print("[");
			for(int j = 0; j < matrix[0].length; j++) {
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println("]");
		}
	}
	
	private static double[][] generatePayoffMatrix(double k, int height) {
		
		// 1. Generate all the inputs
		String[] inputs = new String[(int) Math.pow(2, Math.pow(2, 2*k))];
		String[] algorithms = new String[(int) (Math.pow(2, Math.pow(2, 2*k) - 1))];
		double[][] payoffMatrix = new double[inputs.length][algorithms.length];
		
		NORNode gameTree;
		
		for(int i = 0; i < inputs.length; i++) {
			inputs[i] = String.format("%" + (int) Math.pow(2, 2*k) + "s", Integer.toBinaryString(i)).replace(' ', '0');
		}
		
		// 2. Generate all the possible algorithms
		// (all possible order of evaluation)
		for(int i = 0; i < algorithms.length; i++) {
			algorithms[i] = String.format("%" + ((int) (Math.pow(2, (2*k)))-1) + "s", Integer.toBinaryString(i)).replace(' ', '0');
		}

		// 3. Generate the matrix
		
		for(int i = 0; i < inputs.length; i++) {
			for(int j = 0; j < algorithms.length; j++) {
				gameTree = NORNode.createGameTree(height, algorithms[j], inputs[i]);
				gameTree.deterministicEvaluation();
				payoffMatrix[i][j] = (double) NORNode.nbEvaluations;
				NORNode.resetNbEvaluation();
			}
		}
		
		return payoffMatrix;
	}
}
