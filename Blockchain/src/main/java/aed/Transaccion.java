package aed;
public class Transaccion implements Comparable<Transaccion> {
    public int id;
    private int id_comprador;
    private int id_vendedor;
    private int monto;
    private int indiceOriginal;

    public Transaccion(int id, int id_comprador, int id_vendedor, int monto) {
        this.id = id;
        this.id_comprador = id_comprador;
        this.id_vendedor = id_vendedor;
        this.monto = monto;
    }

    //le hago Override para poder agregar la definición por id
    @Override
    public int compareTo(Transaccion otro) {
        if(this.monto!=otro.monto){         // Si son distintos
            if (this.monto > otro.monto) { // devuelvo 1 si el monto1>monto2
                return 1;
            }else {                       // devuelvo -1 si monto2>monto1
                return -1;
            }
        } else {                               // Si los montos son iguales
            if (this.id > otro.id) {          // devuelvo 1 si el id1>id2
                return 1;
            } else if (this.id < otro.id) {
                return -1;                      // devuelvo -1 si el id2>id1
            } else {
                return 0;                  // si es la misma transacción devuelvo 0
            }
        }
    }

    @Override
    public boolean equals(Object otro){

        if (otro == null || otro.getClass() != this.getClass()) {
            return false;
        }

        Transaccion otraTransaccion = (Transaccion) otro;
        return this.id == otraTransaccion.id && this.id_comprador == otraTransaccion.id_comprador && this.id_vendedor == otraTransaccion.id_vendedor && this.monto == otraTransaccion.monto;
    }

    public int monto() {
        return monto;
    }

    public int id_comprador() {
        return id_comprador;
    }
    
    public int id_vendedor() {
        return id_vendedor;
    }

    public void setIndiceOriginal(int i) {
        this.indiceOriginal = i;
    }

    public int indiceOriginal() {
        return indiceOriginal;
    }


}