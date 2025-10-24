import java.util.ArrayList;
import java.util.Scanner;

public class Convertibilidad {

    public static void main(String[] args) {
        Scanner scannerValores = new Scanner(System.in);
        int x = scannerValores.nextInt();
        int y = scannerValores.nextInt();
        scannerValores.close();
        convertibilidad(x, y);
    }

    public static void convertibilidad(int x, int y) {
        ArrayList<Integer> secuencia_transformacion = new ArrayList<>();
        int valor_actual = y;
        // Hago el camino a la inversa para no probar TODOS los caminos posibles
        // Si probara todos los caminos tendria que armar muchas condiciones
        // -> Tomo el valor al que quiero llegar
        // y lo voy reduciendo hasta llegar al de inicio
        // -> Si llego, devuelvo lo pedido (YES + #elems + lista de elems),
        // -> Sino no existe forma -> devuelvo "NO"

        while (valor_actual > x) {
            secuencia_transformacion.add(valor_actual);
            if (valor_actual % 2 == 0) {
                valor_actual /= 2; // Si y mod 2 = 0, avanzo
            } else if ((valor_actual - 1) % 10 != 0) {
                // Sino, veo si (y - 1) mod 10 = 0
                System.out.println("NO"); // Si no lo es, no encuentro solucion
                return;
            } else {
                valor_actual = (valor_actual - 1) / 10; // Sino, avanzo
            }
        }
        secuencia_transformacion.add(valor_actual); // Agrego el valor que buscaba al final (y)

        if (valor_actual == x) {
            System.out.println("YES");
            System.out.println(secuencia_transformacion.size());
            for (int i = secuencia_transformacion.size() - 1; i >= 0; i--) {
                System.out.print(secuencia_transformacion.get(i));
                System.out.print(" ");
            }
        } else {
            System.out.println("NO");
        }
    }
}
