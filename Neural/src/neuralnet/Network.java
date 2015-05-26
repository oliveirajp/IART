package neuralnet;

import java.util.ArrayList;

public class Network {

	public static Node[][] network;
	public static ArrayList<ArrayList<Double>> input;
	static double target;
	static double error, realError;
	static double learningRate;

	static public void main(String[] args) {
		network = new Node[3][];
		network[0] = new Node[26];
		network[1] = new Node[27];// TODO provisorio
		network[2] = new Node[1];
		Node.init();

		for (int i = 1; i < network.length; i++) {
			for (int j = 0; j < network[i].length; j++) {
				network[i][j] = new Node(network[i - 1].length, i);
				for (int j2 = 0; j2 < network[i][j].dweights.length; j2++) {
					// System.out.println("Peso N� " + i + "-" + j + "[" + j2
					// + "]" + network[i][j].dweights[j2]);
				}
			}
		}
		// teste
		target = 0.7;
		error = 1;
		realError = 1;
		learningRate = 0.9;
		for (int i = 0; i < network[0].length; i++) {
			if (i % 2 == 0)
				network[0][i] = new Node(5);
			else
				network[0][i] = new Node(7);
		}

		int i = 0;
		while (realError > 0.001) {
			forward();
			backPropagation();
			System.out.println("Error is " + realError);

			System.out.println("Output is "
					+ network[network.length - 1][0].getOutput());
			i++;
		}

	}

	static void forward() {
		for (int i = 1; i < network.length; i++) {
			for (int j = 0; j < network[i].length; j++) {
				network[i][j].forward();
			}
		}
		error = target - network[network.length - 1][0].getOutput();
		realError = 0.5 * (error * error);
	}

	static void backPropagation() {

		network[network.length - 1][0].delta = network[network.length - 1][0]
				.getOutput()
				* (1 - network[network.length - 1][0].getOutput())
				* error;

		for (int i = network.length - 2; i > 0; i--) {
			for (int j = 0; j < network[i].length; j++) {
				// retropropagar o erro para as camadas seguintes
				double sum = 0;
				for (int k = 0; k < network[i + 1].length; k++) {
					sum += network[i + 1][k].delta
							* network[i + 1][k].dweights[j];
				}
				network[i][j].delta = network[i][j].getOutput()
						* (1 - network[i][j].getOutput()) * sum;
			}
		}

		for (int i = network.length - 1; i > 0; i--) {
			for (int j = 0; j < network[i].length; j++) {
				for (int k = 0; k < network[i][j].dweights.length; k++) {
					// modificar os pesos
					double deltaW = network[i][j].delta
							* network[i - 1][k].getOutput();
					network[i][j].dweights[k] -= learningRate * deltaW;
				}
			}
		}

	}

}