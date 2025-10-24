package aed.t3;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import aed.Handle;

public class ListaEnlazada<T> implements Secuencia<T> {
    private Nodo primero;
    private Nodo ultimo;

    public class Nodo {
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
        ultimo = null;
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

    public void agregarAdelante(T elem) {
        Nodo nuevo = new Nodo(elem);
        if (primero != null) {
            primero.prev = nuevo;
            nuevo.sig = primero;
        } else {
            ultimo = nuevo;
        }
        primero = nuevo;
    }

    public void agregarAtras(T elem) {
        Nodo nuevo = new Nodo(elem);
        if (primero == null) {
            primero = nuevo;
            ultimo = nuevo;
        } else {
            ultimo.sig = nuevo;
            nuevo.prev = ultimo;
            ultimo = nuevo;
        }
    }

    private Nodo obtenerNodo(int i) {
        Nodo actual = primero;
        int contador = 0;
        while (actual != null && contador < i) {
            actual = actual.sig;
            contador++;
        }
        if (actual == null) throw new IndexOutOfBoundsException("Índice fuera de rango");
        return actual;
    }


    @Override
    public T obtener(int i) {
        return obtenerNodo(i).valor;
    }

    public List<T> obtenerListaCompleta() {
        List<T> lista = new ArrayList<>();
        Nodo actual = primero;
        while (actual != null) {
            lista.add(actual.valor);
            actual = actual.sig;
        }
        return lista;
    }

    @Override
    public void eliminar(int i) {
        Nodo actual = obtenerNodo(i);
        eliminarNodo(actual);
    }

    public void eliminarConHandle(Handle<T> h) {
        Nodo nodo = h.getNodo();
        eliminarNodo(nodo);
    }

    public Nodo agregarYRetornarNodo(T elem) {
        Nodo nuevo = new Nodo(elem);
        if (primero == null) {
            primero = nuevo;
            ultimo = nuevo;
        } else {
            ultimo.sig = nuevo;
            nuevo.prev = ultimo;
            ultimo = nuevo;
        }
        return nuevo;
    }

    public void eliminarNodo(Nodo actual) {
        if (actual == null) return;

        if (actual.prev != null) {
            actual.prev.sig = actual.sig;
        } else {
            primero = actual.sig;
        }

        if (actual.sig != null) {
            actual.sig.prev = actual.prev;
        } else {
            ultimo = actual.prev;
        }

        actual.prev = null;
        actual.sig = null;
    }

    @Override
    public void modificarPosicion(int indice, T elem) {
        Nodo actual = obtenerNodo(indice);
        actual.valor = elem;
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

        private ListaIterador(Nodo primero) {
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
                actual = primero;
            } else {
                actual = actual.sig;
            }
            return actual.valor;
        }

        @Override
        public T anterior() {
            if (!hayAnterior()) {
                throw new NoSuchElementException();
            }
            T valor = actual.valor;
            actual = actual.prev;
            return valor;
        }
    }

    public Iterador<T> iterador() {
        return new ListaIterador(this.primero);
    }
}
