package aed;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ListaEnlazada<T> implements Secuencia<T> {
    // Completar atributos privados
    private Nodo primero;

    private class Nodo {
        T valor;
        Nodo sig;
        Nodo prev;

        Nodo(T v) {
            this.valor = v;
            this.sig = null;
            this.prev = null;
        }
    }

    public ListaEnlazada() {
        primero = null;
    }

    @Override
    public int longitud() {
        int longitud = 0; 
        Nodo actual = primero;
        while (actual != null) {
            longitud++;
            actual = actual.sig;
        }
        return longitud;
    }

    @Override
    public void agregarAdelante(T elem) {
        Nodo nuevo = new Nodo(elem);
        if (primero != null){
            primero.prev = nuevo;
            nuevo.sig = primero;
        }
        primero = nuevo;
    }

    @Override
    public void agregarAtras(T elem) {
        Nodo nuevo = new Nodo(elem);
        if (primero == null) {
            primero = nuevo;
        }else {
            Nodo actual = primero;
            while (actual.sig != null) {
                actual = actual.sig;
            }
            actual.sig = nuevo;
            nuevo.prev = actual;
        }
    }

    @Override
    public T obtener(int i) {
        Nodo actual = primero;
        int contador = 0;
        while (actual != null && contador < i) {
            actual = actual.sig;
            contador++;
        }
        return actual.valor;
    }

    @Override
    public void eliminar(int i) {
        Nodo actual = primero;
        int contador = 0;
        while (actual != null && contador < i) {
            actual = actual.sig;
            contador++;
        }
        if (i == 0) {
            primero = actual.sig;
            if (primero != null){
                primero.prev = null;
            }
        } else {
            if (actual.sig != null){
                actual.sig.prev = actual.prev;
            }
            if (actual.prev != null){
                actual.prev.sig = actual.sig;
            }
        }
    }

    @Override
    public void modificarPosicion(int indice, T elem) {
        Nodo actual = primero;
        int contador = 0;
        while (actual != null && contador < indice) {
            actual = actual.sig;
            contador++;
        }
        actual.valor = elem;
    }


    public ListaEnlazada(ListaEnlazada<T> lista) {
        if (lista.primero == null) {
            this.primero = null;
        } else {
            this.primero = new Nodo(lista.primero.valor);
            Nodo actualNuevo = this.primero;
            Nodo actualOriginal = lista.primero.sig;
            while (actualOriginal != null) {
                actualNuevo.sig = new Nodo(actualOriginal.valor);
                actualNuevo = actualNuevo.sig;
                actualOriginal = actualOriginal.sig;
            }
        }
    }
    
    @Override
    public String toString() {
        List<String> listaDeElems = new ArrayList<>();
        Nodo actual = primero;
        while (actual != null) {
            listaDeElems.add(actual.valor.toString());
            actual = actual.sig;
        }
        return listaDeElems.toString();

    }

    public class ListaIterador implements Iterador<T> {
    	private Nodo actual;
        private Nodo primero;

        private ListaIterador(ListaEnlazada<T>.Nodo primero) {
            this.primero = primero;
            this.actual = null;
        }

        @Override
        public boolean haySiguiente() {
            if (actual == null) {
                return primero != null;
            }
            return actual.sig != null;
        }

        @Override
        public boolean hayAnterior() {
            return actual != null;
        }

        @Override
        public T siguiente() {
            if (!haySiguiente()) {
                throw new NoSuchElementException();
            }
            if (actual == null) {
                actual = primero; // Si no arranque, voy al primer nodo
            } else {
                actual = actual.sig; // Si ya arranque, voy al siguiente
            }
            return actual.valor;
        }

        @Override
        public T anterior() {
            if (!hayAnterior()) {
                throw new NoSuchElementException();
            }
            T valor = actual.valor;
            actual = actual.prev; // Voy al nodo anterior
            return valor;
        }
            
    }

    public Iterador<T> iterador() {
        return new ListaIterador(this.primero);
    }

}
