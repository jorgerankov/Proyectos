package aed;

import aed.t3.ListaEnlazada;

public class Bloque {
    private ListaEnlazada<Transaccion> transacciones;
    int montoTotal;
    int cantidadTransferencias;
    Heap heapTransacciones;
    private Handle<Transaccion>[] handles;

    public Bloque(Transaccion[] transacciones) {
        this.transacciones = new ListaEnlazada<>();
        this.handles = new Handle[transacciones.length];


        heapTransacciones = new Heap();
        montoTotal = 0;
        cantidadTransferencias = 0;
            for (int i = 0; i < transacciones.length; i++) {
                transacciones[i].setIndiceOriginal(i);
                ListaEnlazada<Transaccion>.Nodo nodo = this.transacciones.agregarYRetornarNodo(transacciones[i]);
                handles[i] = new Handle<>(nodo);
                // Sumamos monto y transferencias solo si NO es creación
                if (transacciones[i].id_comprador() != 0) {
                    montoTotal += transacciones[i].monto();
                    cantidadTransferencias += 1;  // Contar cantidad de transacciones
                }
            }

       heapTransacciones.generarHeapify(transacciones);      // CREO EL HEAP-MAX TRANSACCIONES O(Nb)
    }

    public void hackearBloque() {
        if (heapTransacciones == null || transacciones == null || transacciones.longitud() == 0) {
            return;
        }

        Transaccion transaccionHackeada = (Transaccion) heapTransacciones.devolverMaximo();
        if (transaccionHackeada == null) {
            return;
        }

        int indice = transaccionHackeada.indiceOriginal();

        if (transacciones.longitud() <= 1) {
            // Si hay una sola transacción, vaciar todo directamente
            transacciones = new ListaEnlazada<>();
            heapTransacciones.vaciar();
            handles = new Handle[0];
            return;
        } else if (handles[indice] != null) {
            heapTransacciones.robarTransaccion(transaccionHackeada);
            transacciones.eliminarConHandle(handles[indice]);
            handles[indice] = null;
        }

        if (transaccionHackeada.id_comprador() != 0) {
            montoTotal -= transaccionHackeada.monto();
            cantidadTransferencias -= 1;
        }
    }
    public Heap obtenerHeap() {
        return heapTransacciones;
    }

    public Transaccion[] obtenerTransacciones(){
        return transacciones.obtenerListaCompleta().toArray(new Transaccion[0]);
    }
    public int montoTotal() {
        return montoTotal;
    }
    public int cantidadTransferencias() {
        return cantidadTransferencias;
    }


}
