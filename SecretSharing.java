import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SecretSharing {

    // Function to decode the value from the given base to decimal
    public static BigInteger decodeValue(int base, String value) {
        return new BigInteger(value, base);
    }

    // Lagrange interpolation to find the constant term (L(0))
    public static BigInteger lagrangeInterpolation(Map<Integer, BigInteger> points, int k) {
        BigInteger result = BigInteger.ZERO;
        
        for (int i : points.keySet()) {
            BigInteger xi = BigInteger.valueOf(i);
            BigInteger yi = points.get(i);

            BigInteger term = yi;
            
            for (int j : points.keySet()) {
                if (i != j) {
                    BigInteger xj = BigInteger.valueOf(j);
                    BigInteger numerator = BigInteger.ZERO.subtract(xj); // 0 - xj
                    BigInteger denominator = xi.subtract(xj);
                    term = term.multiply(numerator).divide(denominator); // term *= (0 - xj) / (xi - xj)
                }
            }

            result = result.add(term); // Add the computed term to the result
        }

        return result;
    }

    // Function to find the constant term (c) of the polynomial
    public static BigInteger findConstantTerm(Map<String, Object> testCase) {
        Map<String, Object> keys = (Map<String, Object>) testCase.get("keys");
        int n = (Integer) keys.get("n");  // Total number of roots
        int k = (Integer) keys.get("k");  // Minimum number of roots required

        // Extract and decode the points (x, y)
        Map<Integer, BigInteger> points = new HashMap<>();
        for (String xStr : testCase.keySet()) {
            if (!xStr.equals("keys")) {
                int x = Integer.parseInt(xStr);
                Map<String, String> valueDict = (Map<String, String>) testCase.get(xStr);
                int base = Integer.parseInt(valueDict.get("base"));
                String value = valueDict.get("value");
                BigInteger y = decodeValue(base, value);
                points.put(x, y);
            }
        }

        // We only need k points for interpolation
        return lagrangeInterpolation(points, k);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Reading keys
        System.out.print("Enter the number of roots (n): ");
        int n = scanner.nextInt();
        System.out.print("Enter the minimum number of roots required (k): ");
        int k = scanner.nextInt();

        // Prepare to read roots
        Map<String, Object> testCase = new HashMap<>();
        Map<String, Object> keys = new HashMap<>();
        keys.put("n", n);
        keys.put("k", k);
        testCase.put("keys", keys);

        // Read each root
        for (int i = 1; i <= n; i++) {
            System.out.println("Enter details for root " + i + ":");
            System.out.print("Base of the value: ");
            int base = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.print("Value in the given base: ");
            String value = scanner.nextLine();

            Map<String, String> valueDict = new HashMap<>();
            valueDict.put("base", String.valueOf(base));
            valueDict.put("value", value);

            testCase.put(String.valueOf(i), valueDict);
        }

        scanner.close();

        // Find the constant term (c)
        BigInteger constantTerm = findConstantTerm(testCase);
        System.out.println("Constant term (c): " + constantTerm);
    }
}