import java.util.Random;

public class Main {

    public static void main(String[] args) {

        System.out.println("===== Ta Te Ti =====\n");
        System.out.println("Jugador 1 es X");
        System.out.println("Jugador 2 es O\n");

        Tablero tablero = new Tablero();
        Random quienEmpieza = new Random();
        ColocarPieza colocarPieza = new ColocarPieza();

        tablero.printTablero();

        int primero = quienEmpieza.nextInt(1,3);

        System.out.println("\nComienza el jugador " + primero);
        colocarPieza.colocar(primero, tablero);

    }
}
