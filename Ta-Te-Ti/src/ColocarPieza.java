import java.util.Scanner;

public class ColocarPieza {
    public void colocar(int player, Tablero tablero) {
        Scanner scn = new Scanner(System.in);
        FormasDeGanar terminarJuego = new FormasDeGanar();

        String primero;
        String segundo;

        if (player == 1) {
            primero = "X";
            segundo = "O";
        } else {
            primero = "O";
            segundo = "X";
        }

        int jugadorActual = player;
        String piezaActual = primero;

        String[] listaDePosiciones = {"L1","L2","L3","M1","M2","M3","D1","D2","D3"};

        while (!terminarJuego.finDelJuego(tablero)) {
            System.out.println("Turno del jugador " + jugadorActual + " (" + piezaActual + ")");
            System.out.println("Colocar en la posicion: ");
            String pos = scn.nextLine();

            while (!tablero.posValida(pos)) {
                tablero.printTablero();
                System.out.println("\nPosicion no valida. Intenta nuevamente.");
                System.out.println("Colocar en la posicion: ");
                pos = scn.nextLine();

            }

            while (!tablero.vacio(pos)) {
                tablero.printTablero();
                System.out.println("\nLugar no disponible. Intenta nuevamente.");
                System.out.println("Colocar en la posicion: ");
                pos = scn.nextLine();

            }

            tablero.setearPieza(pos, piezaActual);
            tablero.printTablero();
            System.out.println();

            if (terminarJuego.finDelJuego(tablero)) {
                System.out.println("¡Jugador " + jugadorActual + " gana!");
                break;
            }

            // Verificar empate (tablero lleno)
            int contadorVacio = 0;
            for (String listaDePos : listaDePosiciones) {
                if (!tablero.vacio(listaDePos)) {
                    contadorVacio++;
                }
            }
            if (contadorVacio == 9){
                System.out.println("Empate!");
                break; // Salir del bucle principal
            }


            // Alternar jugador
            if (jugadorActual == player) {
                jugadorActual = (player == 1) ? 2 : 1;
                piezaActual = segundo;
            } else {
                jugadorActual = player;
                piezaActual = primero;
            }
        }
        scn.close();
    }
}
