package aed;

import java.util.ArrayList;

public class Heap<T extends Comparable<T>> {
    private ArrayList<T> heap;

    public Heap() {
        heap = new ArrayList<>();
    }

    public void heapify(int i) {
        while (i > 0) {
            int padre = (i - 1) / 2;
            if (heap.get(i).compareTo(heap.get(padre)) > 0) {
                intercambiar(i, padre);
                i = padre;
            } else {
                return;
            }
        }
    }

    public void reordenamientoMaximo(int i) {
        int hijoIzq = 2 * i + 1;

        while (hijoIzq < heap.size()) {
            int hijoDer = 2 * i + 2;
            int hijoMayor = hijoIzq;

            if (hijoDer < heap.size() && heap.get(hijoDer).compareTo(heap.get(hijoIzq)) > 0) {
                hijoMayor = hijoDer;
            }

            if (heap.get(i).compareTo(heap.get(hijoMayor)) >= 0) {
                return;
            }

            intercambiar(i, hijoMayor);
            i = hijoMayor;
            hijoIzq = 2 * i + 1;
        }
    }

    public void generarHeapify(T[] arreglo) {
        heap.clear(); //Limpio el heap

        // Agrego los usuarios del arreglo
        if(arreglo.length > 0 && arreglo[0] == null) {
        for (int i = 1; i < arreglo.length; i++) {
            T elemActual = arreglo[i];
            heap.add(elemActual);
            ((Usuario) elemActual).actualizarIndiceHeap(heap.size() - 1);
            }

        }else{
            for (int i = 0; i < arreglo.length; i++) {
                T elemActual = arreglo[i];
                heap.add(elemActual);
        }



        }

        int n = heap.size();
        for (int i = (n / 2) - 1; i >= 0; i--) {
            reordenamientoMaximo(i);
        }
    }

    public T devolverMaximo() {
        if (heap.isEmpty()){
            return null;
        }
        return heap.get(0);
    }

    private void intercambiar(int i, int j) {
        T elemActual = heap.get(i);

        T usuarioI = heap.get(i);
        T usuarioJ = heap.get(j);
        heap.set(i, heap.get(j));
        heap.set(j, elemActual);

        if(elemActual.getClass() == Usuario.class){
        Usuario.class.cast(usuarioI).actualizarIndiceHeap(j); // Acá actualizo la información de su ubicación en el heap
        Usuario.class.cast(usuarioJ).actualizarIndiceHeap(i);
        }
    }

    public void robarTransaccion(Transaccion hack) {
        if (heap.isEmpty())
        {
            return;
        }

        int last = heap.size() - 1;
        if(heap.size() == 1){
            heap.remove(0);
            vaciar();
            return;
        }
        intercambiar(0, last);
        heap.remove(last);


        // Después de eliminar, reordenamos según corresponda
        if (heap.size()>1) {
            heapify(0);               // por si al subir rompe heap
            reordenamientoMaximo(0); // por si al bajar rompe heap
        }
    }

    public void vaciar() {
        heap.clear();
    }

    public void actualizar(Usuario usuario) {
        int indice = usuario.obtenerIndiceHeap();
        heapify(indice); // por si sube
        reordenamientoMaximo(indice); // por si baja
    }
}
