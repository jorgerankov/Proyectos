package aed;
import aed.t3.ListaEnlazada;

// Clase Handle

// APROVECHO ACÁ PARA EXPLICAR QUÉ HICE CON EL HANDLE

// Agregué el atributo indiceOriginal a Transacción
// Este mismo me indica su índice en el momento en que creo la lista enlazada
// Al mismo tiempo que creo la lista enlazada, creo el arreglo de handles a la lista
// Por lo que, el índice de cada arreglo de handles coincide con el de la lista enlazada
// Entonces cuando necesito usar el handle, en lugar de buscar qué handle corresponde a cada nodo
// Directamente accedo con el índiceOriginal ---> handle[indiceOriginal]
// Y a la lista enlazada le indico diréctamente cuál es el nodo a eliminar
// Todo este proceso de eliminación es sólo accesos directos a posiciones que yo ya sabía -----> O(1)

public class Handle<T> {
    private ListaEnlazada<T>.Nodo nodo;

    public Handle(ListaEnlazada<T>.Nodo nodo) {
        this.nodo = nodo;
    }

    public ListaEnlazada<T>.Nodo getNodo() {
        return nodo;
    }
}