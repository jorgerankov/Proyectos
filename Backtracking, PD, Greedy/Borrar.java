import java.util.Scanner;
import java.util.Arrays;

public class Borrar {

    public static int borrar(int n, String palabra) {
        // -1 indica que no está resuelto aún.
        int[][] memo = new int[n][n];
        for (int[] row : memo)
            Arrays.fill(row, -1);

        return dp(0, n - 1, palabra, memo);         // Llamo recursivamente a toda la palabra
    }

    private static int dp(int l, int r, String palabra, int[][] memo) {
        if (l > r) return 0;                        // Si me quede sin letras
        if (l == r) return 1;                       // Si l y r son el mismo indice (un solo caracter)
        if (memo[l][r] != -1) return memo[l][r];    // Chequeo si el par existe en mi memoria
                                                    // Si existe, lo retorno
        int res = 1 + dp(l + 1, r, palabra, memo);  // Sino, borro el caracter en la pos l y resuelvo recursivamente el resto

        for (int k = l + 1; k <= r; k++) {
            if (palabra.charAt(k) == palabra.charAt(l)) {       // Busco posiciones donde la letra k sea igual a l
                res = Math.min(res,                             // Si la hay, intento borrar lo de en medio,
                        dp(l + 1, k - 1, palabra, memo) + dp(k, r, palabra, memo)); // para juntar palabra[l] y palabra[k]
            }
        }
        memo[l][r] = res;       // Guardo el menor costo en mi memo
        return res;             // Devuelvo el costo
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int cantidad_letras = scan.nextInt();
        String palabra = scan.next();
        scan.close();
        int resultado = borrar(cantidad_letras, palabra);
        System.out.println(resultado);
    }
}