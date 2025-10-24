import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
 
public class AleYLos1s {
 
    public static void main(String[] args) {
        Scanner scannerValores = new Scanner(System.in);
        int primervalor = scannerValores.nextInt();
        int inicio = scannerValores.nextInt();
        int fin = scannerValores.nextInt();
 
        ArrayList<Integer> lista_res = new ArrayList<>();
        lista_res.add(primervalor);
 
        scannerValores.close();
        ale(lista_res, inicio, fin);
    }
 
    public static void ale(List<Integer> lista, int inicio, int fin){
        
        // Recorro la lista y voy modificando los elems de acuerdo a lo pedido
        // Con ArrayLists / Lists puedo agregar los 3 elems pedidos reemplazando el original
        // Luego, llamo recursivamente a la funcion hasta que todos los elems sean  0 o 1
        // Y aplico return para pasar a contar los 1s
 
        for (int i = 0; i < lista.size(); i++){
            if (lista.get(i) != 1 && lista.get(i) != 0) {
 
                List<Integer> reemplazo = Arrays.asList(lista.get(i) / 2, lista.get(i) % 2, lista.get(i) / 2);
                lista.remove(i);
                lista.addAll(i, reemplazo);
                ale(lista, inicio, fin);
                return;
            }
        }
 
        // Cuento cuantos 1s hay en mi lista de 0s y 1s, y lo devuelvo -> Termino la funcion
        int contador = 0;
        for (int i = inicio-1; i < fin; i++){
            if (lista.get(i) == 1){
                contador++;
            }
        }        
 
        System.out.println(contador);
        return;
    }
}