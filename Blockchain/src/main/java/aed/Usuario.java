package aed;

public class Usuario implements Comparable<Usuario> {
    public int id;
    public int monto;
    public int indiceHeap;

    // INDICEHEAP:
    // Esto es para guardar el indice en el que está el usuario en el heapUsuarios
    // Yo cada vez que trabajo con el heap lo puedo obtener fácilmente, entonces lo guardo
    // ¿Para qué lo quiero obtener fácilmente?
    // PARA ACTUALIZAR LOS MONTOS EN EL HEAP después del hackeo
    // Si yo tengo este índice a mano, no tengo que andar buscandolo en ese momento
    // Simplemente me lo guardo y lo uso

    public Usuario(int id, int monto) {
        this.id = id;
        this.monto = monto;

        this.indiceHeap = -1; //  Esto lo actualizamos al momento en que:
                             // * Creamos el heap y definimos la posición en la que entra
                            // ** Cada vez que se mueve
    }

    @Override
    public int compareTo(Usuario otro) {
        if(this.monto!=otro.monto){         // Si son distintos
            if (this.monto > otro.monto) { // devuelvo 1 si el monto1>monto2
                return 1;
            }else {                       // devuelvo -1 si monto2>monto1
                return -1;
            }
        } else {                               // Si los montos son iguales
            if (this.id > otro.id) {          // devuelvo -1 si el id1>id2
                return -1;
            } else if (this.id < otro.id) {
                return 1;                      // devuelvo -1 si el id2>id1
            } else {
                return 0;                  // si es la misma transacción devuelvo 0
            }
        }
    }

    public int obtenerId() {
        return this.id;
    }
    public int obtenerMonto() {
        return this.monto;
    }

    public void actualizarMonto(int monto) {
        this.monto = monto;
    }

    public int obtenerIndiceHeap() {
        return indiceHeap;
    }

    // Acá me guardo la nueva posición de este usuario en el heap
    public void actualizarIndiceHeap(int i) {
        indiceHeap = i;
    }

}
