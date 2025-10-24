import java.util.Scanner;

public class Alfabeticamente {
    public static void main(String[] args) {
        Scanner scannerValores = new Scanner(System.in);
        int cantidad_palabras = scannerValores.nextInt();
        
        int[] costos = new int[cantidad_palabras];
        for (int i = 0; i < cantidad_palabras; i++) {
            costos[i] = scannerValores.nextInt();
        }
        
        String[] palabras = new String[cantidad_palabras];
        for (int i = 0; i < cantidad_palabras; i++) {
            palabras[i] = scannerValores.next();
        }
        
        long resultado = alfabeticamente(palabras, costos);
        System.out.println(resultado == Long.MAX_VALUE ? -1 : resultado);
        
        scannerValores.close();
    }

    // dp[i][j] representa el costo mínimo para ordenar las primeras i palabras,
    // j indica si la i-ésima palabra está invertida (0 = normal, 1 = invertida)
    public static long alfabeticamente(String[] palabras, int[] costos) {
        int n = palabras.length;
        
        if (n == 0) return 0;
        if (n == 1) return 0;
        
        long[][] dp = new long[n][2];
        
        for (int i = 0; i < n; i++) {
            dp[i][0] = Long.MAX_VALUE;
            dp[i][1] = Long.MAX_VALUE;
        }
        
        dp[0][0] = 0;         
        dp[0][1] = costos[0]; 

        for (int i = 1; i < n; i++) {
            String actualNormal = palabras[i];
            String actualInvertida = new StringBuilder(palabras[i]).reverse().toString();
            String prevNormal = palabras[i - 1];
            String prevInvertida = new StringBuilder(palabras[i - 1]).reverse().toString();
            
            if (dp[i - 1][0] != Long.MAX_VALUE && prevNormal.compareTo(actualNormal) <= 0) {
                dp[i][0] = Math.min(dp[i][0], dp[i - 1][0]);
            }
            
            if (dp[i - 1][1] != Long.MAX_VALUE && prevInvertida.compareTo(actualNormal) <= 0) {
                dp[i][0] = Math.min(dp[i][0], dp[i - 1][1]);
            }
            
            if (dp[i - 1][0] != Long.MAX_VALUE && prevNormal.compareTo(actualInvertida) <= 0) {
                dp[i][1] = Math.min(dp[i][1], dp[i - 1][0] + costos[i]);
            }
            
            if (dp[i - 1][1] != Long.MAX_VALUE && prevInvertida.compareTo(actualInvertida) <= 0) {
                dp[i][1] = Math.min(dp[i][1], dp[i - 1][1] + costos[i]);
            }
        }
        
        return Math.min(dp[n - 1][0], dp[n - 1][1]);
    }
}


