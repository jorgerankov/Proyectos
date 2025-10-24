public class FormasDeGanar {
    public boolean finDelJuego(Tablero tablero) {

        return  // Formas horizontales X
                "X".equals(tablero.getL1()) && "X".equals(tablero.getL2()) && "X".equals(tablero.getL3()) ||
                "X".equals(tablero.getM1()) && "X".equals(tablero.getM2()) && "X".equals(tablero.getM3()) ||
                "X".equals(tablero.getD1()) && "X".equals(tablero.getD2()) && "X".equals(tablero.getD3()) ||
                // Formas verticales X
                "X".equals(tablero.getL1()) && "X".equals(tablero.getM1()) && "X".equals(tablero.getD1()) ||
                "X".equals(tablero.getL2()) && "X".equals(tablero.getM2()) && "X".equals(tablero.getD2()) ||
                "X".equals(tablero.getL3()) && "X".equals(tablero.getM3()) && "X".equals(tablero.getD3()) ||
                // Formas diagonales X
                "X".equals(tablero.getL1()) && "X".equals(tablero.getM2()) && "X".equals(tablero.getD3()) ||
                "X".equals(tablero.getL3()) && "X".equals(tablero.getM2()) && "X".equals(tablero.getD1()) ||

                // Formas horizontales O
                "O".equals(tablero.getL1()) && "O".equals(tablero.getL2()) && "O".equals(tablero.getL3()) ||
                "O".equals(tablero.getM1()) && "O".equals(tablero.getM2()) && "O".equals(tablero.getM3()) ||
                "O".equals(tablero.getD1()) && "O".equals(tablero.getD2()) && "O".equals(tablero.getD3()) ||
                // Formas verticales O
                "O".equals(tablero.getL1()) && "O".equals(tablero.getM1()) && "O".equals(tablero.getD1()) ||
                "O".equals(tablero.getL2()) && "O".equals(tablero.getM2()) && "O".equals(tablero.getD2()) ||
                "O".equals(tablero.getL3()) && "O".equals(tablero.getM3()) && "O".equals(tablero.getD3()) ||
                // Formas diagonales O
                "O".equals(tablero.getL1()) && "O".equals(tablero.getM2()) && "O".equals(tablero.getD3()) ||
                "O".equals(tablero.getL3()) && "O".equals(tablero.getM2()) && "O".equals(tablero.getD1());
    }
}
