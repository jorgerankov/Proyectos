package aed;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;


public class BerretacoinTestsExtra {
    private Berretacoin berretacoin;
    private Transaccion[] transacciones;
    private Transaccion[] transacciones2;
    private Transaccion[] transacciones3;

    // Helper class para trackear saldos de usuarios
    private class SaldoTracker {
        private Map<Integer, Integer> saldos;
        
        public SaldoTracker(int usuarios) {
            saldos = new HashMap<>();
            for (int i = 1; i <= usuarios; i++) {
                saldos.put(i, 0);
            }
        }
        
        public void aplicarTransaccion(Transaccion tx) {
            if (tx.id_comprador() == 0) {
                saldos.put(tx.id_vendedor(), saldos.get(tx.id_vendedor()) + tx.monto());
            } else {
                saldos.put(tx.id_comprador(), saldos.get(tx.id_comprador()) - tx.monto());
                saldos.put(tx.id_vendedor(), saldos.get(tx.id_vendedor()) + tx.monto());
            }
        }
        
        public int getSaldo(int usuario) {
            return saldos.get(usuario);
        }
        
        public boolean puedeGastar(int usuario, int monto) {
            return usuario == 0 || getSaldo(usuario) >= monto;
        }
        
        public int getMaximoTenedor() {
            int maxSaldo = -1;
            int maxUsuario = Integer.MAX_VALUE;
            
            for (Map.Entry<Integer, Integer> entry : saldos.entrySet()) {
                int usuario = entry.getKey();
                int saldo = entry.getValue();
                
                if (saldo > maxSaldo || (saldo == maxSaldo && usuario < maxUsuario)) {
                    maxSaldo = saldo;
                    maxUsuario = usuario;
                }
            }
            return maxUsuario;
        }
        
        public void revertirTransaccion(Transaccion tx) {
            if (tx.id_comprador() == 0) {
                saldos.put(tx.id_vendedor(), saldos.get(tx.id_vendedor()) - tx.monto());
            } else {
                saldos.put(tx.id_comprador(), saldos.get(tx.id_comprador()) + tx.monto());
                saldos.put(tx.id_vendedor(), saldos.get(tx.id_vendedor()) - tx.monto());
            }
        }
    }

    @BeforeEach
    void setUp() {
        berretacoin = new Berretacoin(10);

        transacciones = new Transaccion[] {
            new Transaccion(0, 0, 2, 1), // 2 -> $1
            new Transaccion(1, 2, 3, 1), // 3 -> $1
            new Transaccion(2, 3, 4, 1) // 4 -> $1
        };

        transacciones2 = new Transaccion[] {
            new Transaccion(0, 0, 4, 1), // 4 -> $2
            new Transaccion(1, 4, 1, 2), // 1 -> $2
            new Transaccion(2, 1, 2, 1)  // 1 -> $1 , 2 -> $1
        };

        transacciones3 = new Transaccion[] {
            new Transaccion(0, 0, 1, 1), // 1 -> $2, 2 -> $1
            new Transaccion(1, 1, 2, 2), // 2 -> $3
            new Transaccion(2, 2, 3, 3), // 3 -> $3
            new Transaccion(3, 3, 1, 2), // 1 -> $2, 3 -> $1
            new Transaccion(4, 1, 2, 1), // 1 -> $1, 2 -> $1, 3 -> $1
            new Transaccion(5, 2, 3, 1)  // 1 -> $1, 3 -> $2
        };
    }
    

    
    @Test
    public void bloqueVacioNoSeAgrega() {
        berretacoin.agregarBloque(new Transaccion[0]);
        assertEquals(1, berretacoin.maximoTenedor());
        assertEquals(0, berretacoin.montoMedioUltimoBloque());
        assertEquals(0, berretacoin.txUltimoBloque().length);
    }

    @Test
    public void hackearBloqueVacioNoRompe() {
        berretacoin.agregarBloque(new Transaccion[0]);
        berretacoin.hackearTx();
        assertEquals(0, berretacoin.txUltimoBloque().length);
    }

    @Test
    public void transaccionMontoCeroNoAfecta() {
        Transaccion[] bloque = {
            new Transaccion(0, 0, 1, 0)
        };
        berretacoin.agregarBloque(bloque);
        assertEquals(1, berretacoin.maximoTenedor());
        assertEquals(0, berretacoin.montoMedioUltimoBloque());
    }

    @Test
    public void transaccionMontoNegativoNoAfecta() {
        Transaccion[] bloque = {
            new Transaccion(0, 0, 1, -5)
        };
        berretacoin.agregarBloque(bloque);
        assertEquals(1, berretacoin.maximoTenedor());
        assertEquals(0, berretacoin.montoMedioUltimoBloque());
    }

    @Test
    public void transferenciaAUnoMismo() {
        Transaccion[] bloque = {
            new Transaccion(0, 0, 1, 10),
            new Transaccion(1, 1, 1, 5)
        };
        berretacoin.agregarBloque(bloque);
        assertEquals(1, berretacoin.maximoTenedor());
        assertEquals(0, berretacoin.montoMedioUltimoBloque());
    }

    @Test
    public void multipleTransferenciaAUnoMismo() {
        Transaccion[] primerBloque = {
            new Transaccion(0, 0, 2, 8),
            new Transaccion(1, 2, 3, 4)
        };
        berretacoin.agregarBloque(primerBloque);
        assertEquals(4, berretacoin.montoMedioUltimoBloque());

        Transaccion[] segundoBloque = {
            new Transaccion(0, 1, 1, 7),
            new Transaccion(1, 3, 3, 3)
        };
        berretacoin.agregarBloque(segundoBloque);
        assertEquals(4, berretacoin.montoMedioUltimoBloque());
    }

    @Test
    public void hackearHastaVaciarBloque() {
        Transaccion[] bloque = {
            new Transaccion(0, 0, 1, 2),
            new Transaccion(1, 1, 2, 1)
        };
        berretacoin.agregarBloque(bloque);
        berretacoin.hackearTx();
        berretacoin.hackearTx();
        assertEquals(0, berretacoin.txUltimoBloque().length);
        assertEquals(1, berretacoin.maximoTenedor());
    }

    @Test
    public void secuenciaCreacionYHackeo() {
        Transaccion[] bloque = {
            new Transaccion(0, 0, 1, 3),
            new Transaccion(1, 1, 2, 2),
            new Transaccion(2, 2, 3, 1)
        };
        berretacoin.agregarBloque(bloque);
        assertEquals(1, berretacoin.maximoTenedor());
        berretacoin.hackearTx();
        assertEquals(2, berretacoin.maximoTenedor());
        berretacoin.hackearTx();
        assertEquals(3, berretacoin.maximoTenedor());
    }

}
