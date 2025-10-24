import java.util.Scanner;
import java.util.LinkedList;
import java.util.Queue;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt(); 
        scanner.close();

        maquina_min(n, m);
    }

    public static void maquina_min(int n, int m){

        // Si la salida es igual que la llegada, no tengo que hacer pasos -> Devuelvo 0
        if (n == m){
            System.out.println(0);
            return;
        }

        // Creo una Deque (cola de doble entrada y salida) para poder hacer acciones en O(1)
        Queue<Tupla> cola = new LinkedList<>();
        cola.add(new Tupla(n, 0));
        
        // Creo un set de visitados para, en caso de llegar mas de 1 vez al mismo sitio, no volver a analizarlo
        Set<Integer> visitados = new HashSet<>();
        visitados.add(n);
        
        // Mientras la cola tenga elementos por recorrer / Pueda llegar a m
        while (cola.size() > 0){
            Tupla tupla = cola.poll();
            int actual = tupla.actual;
            int pasos = tupla.pasos;
            
            // Guardo n * 2 y n-1, con el n actual que estoy visitando
            int[] vecinos = {actual * 2, actual - 1};
            
            // Si vecino se hace negativo, se descompone el equipo, no quiero recorrer ese camino
            for (int vecino : vecinos){
                if (vecino < 1){
                    continue;
                }

                // Si vecino llega a ser el valor que buscamos, devuelvo los pasos que necesite para llegar a el
                if (vecino == m){
                    System.out.println(pasos + 1);
                    return;
                }

                // Si no sucede eso, agrego el vecino a la lista de visitados y sigo buscando a m con el proximo valor n
                if (!visitados.contains(vecino)){
                    visitados.add(vecino);                  // O(1)
                    cola.add(new Tupla(vecino, pasos + 1)); // O(1)
                }
            }
        }
    // Si se descompone la maquina, devuelvo -1
    System.out.println(-1);
    }
}


// Clase Tupla para guardar el valor actual (n*2 o n-1) en la Queue ya que no puedo crear tuplas directamente como en Python :s
class Tupla {
    int actual;
    int pasos;

    public Tupla(int actual, int pasos) {
        this.actual = actual;
        this.pasos = pasos;
    }
}

