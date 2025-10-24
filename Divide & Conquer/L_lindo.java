import java.util.ArrayList;
import java.util.Scanner;

public class L_lindo {

    public static void main(String[] args) {
        Scanner scannerValores = new Scanner(System.in);
        int cantidad_tests = scannerValores.nextInt();
        ArrayList<Integer> lista_res = new ArrayList<>();
        scannerValores.close();

        for (int i = 0; i < cantidad_tests; i++) {
            int cantidad_letras = scannerValores.nextInt();
            String palabra = scannerValores.next();
            if (cantidad_letras != palabra.length()) {
                return;
            } else {
                lista_res.add(l_lindo(palabra, 'a'));
            }
        }

        for (int i = 0; i < lista_res.size(); i++) {
            System.out.println(lista_res.get(i));
        }
    }

    // Paso de 2 funciones separadas a una unica funcion recursiva que trabaja con
    // Divide and Conquer -> Estaba usando 2 funciones que hacian lo mismo pero que
    // la segunda calculaba erroneamente el Divide and Conquer -> Daba errores de resultados
    // Comienzo evaluando que mitad cuesta menos ser reemplazada por todos 'a'
    // De ahi en adelante reviso cuanto cuesta hacer una funcion b-linda en la otra mitad
    // Luego una c-linda, etc etc

    public static int l_lindo(String palabra, char letra) {
        if (palabra.length() == 1) {
            return (palabra.charAt(0) == letra) ? 0 : 1;
        }

        int mitad = palabra.length() / 2;
        String mitad_izq = palabra.substring(0, mitad);
        String mitad_der = palabra.substring(mitad);

        // Iterador para calcular costo de
        // reemplzar letras por letra deseada en mitad_izq
        int res_izq = 0;
        for (int i = 0; i < mitad_izq.length(); i++) {
            if (mitad_izq.charAt(i) != letra) {
                res_izq++;
            }
        }

        // Calculo el mejor costo posible de la siguiente letra l-linda
        // si 'a' debe estar de todo el lado izquierdo
        int mejor_costo_izq = res_izq + l_lindo(mitad_der, (char) (letra + 1));

        // Iterador para calcular costo de
        // reemplzar letras por letra deseada en mitad_der
        int res_der = 0;
        for (int j = 0; j < mitad_der.length(); j++) {
            if (mitad_der.charAt(j) != letra) {
                res_der++;
            }
        }

        // Calculo el mejor costo posible de la siguiente letra l-linda
        // si 'a' debe estar de todo el lado derecho
        int mejor_costo_der = res_der + l_lindo(mitad_izq, (char) (letra + 1));

        if (mejor_costo_izq < mejor_costo_der) {
            return mejor_costo_izq;
        } else {
            return mejor_costo_der;
        }
    }
}
