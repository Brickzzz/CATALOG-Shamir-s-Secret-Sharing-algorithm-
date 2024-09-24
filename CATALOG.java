import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ShamirSecretSharing {

    // Function to compute the constant term (c) using Lagrange interpolation
    public static double computeConstantTerm(double[] x, double[] y, int k) {
        double constantTerm = 0.0;

        // Apply Lagrange Interpolation Formula
        for (int i = 0; i < k; i++) {
            double term = y[i];  // Start with y_i

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    term *= x[j] / (x[j] - x[i]);  // Multiply with the Lagrange basis polynomials
                }
            }
            constantTerm += term;  // Sum up all terms to get the constant (c)
        }

        return constantTerm;
    }

    // Function to convert a value from a given base to decimal
    public static double convertToDecimal(String base, String value) {
        int baseValue = Integer.parseInt(base);
        return Integer.parseInt(value, baseValue);
    }

    public static void main(String[] args) {
        // Read JSON from a file
        String jsonData = readJsonFromFile("input.json");

        // Parse the JSON data
        JSONObject jsonObject = new JSONObject(jsonData);
        JSONObject keys = jsonObject.getJSONObject("keys");
        int n = keys.getInt("n");
        int k = keys.getInt("k");

        // Validate if n is greater than or equal to k
        if (n < k) {
            System.out.println("Error: Number of provided roots (n) is less than the required roots (k).");
            return;
        }

        // Arrays to hold the x and y values
        double[] x = new double[k];
        double[] y = new double[k];

        // Extracting points from the JSON input
        for (int i = 1; i <= k; i++) {
            JSONObject point = jsonObject.getJSONObject(String.valueOf(i));
            x[i - 1] = Integer.parseInt(point.getString("base")); // Using base as the x value
            y[i - 1] = convertToDecimal(point.getString("base"), point.getString("value")); // Decode y values
        }

        // Calculate the constant term (c)
        double constantTerm = computeConstantTerm(x, y, k);
        
        // Output the constant term
        System.out.println("The constant term (c) of the polynomial is: " + constantTerm);
    }

    // Function to read JSON data from a file
    private static String readJsonFromFile(String filePath) {
        StringBuilder jsonData = new StringBuilder();
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                jsonData.append(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
            e.printStackTrace();
        }
        return jsonData.toString();
    }
}