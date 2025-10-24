// Bautista Marsico - 1001/24
// Tiziana Falbo - 863/23
// Facundo Herrera - 1175/22
// Jorge Rankov - 714/23


package aed;

//----------------------------------------------------------------------------------------------------------------------

public class Berretacoin {
    private Usuario[] usuariosTotales;
    private Bloque bloqueActual;
    private Bloque bloquePrevio;
    private int idBloqueActual;

    Heap heapUsers;

//----------------------------------------------------------------------------------------------------------------------

    // O(P)
    public Berretacoin(int n_usuarios){
        usuariosTotales = new Usuario[n_usuarios + 1];
        usuariosTotales[0] = null;                  // id "reservado" de creación
        heapUsers = new Heap();
        for (int i = 1; i <= n_usuarios; i++) {
            usuariosTotales[i] = new Usuario(i, 0); // Generar arreglo de usuario ---> O(P)
        }
        heapUsers.generarHeapify(usuariosTotales);        // Generar heapify ord. monto ----> O(P)
        bloquePrevio=null;
        idBloqueActual = 0;
    } //------------------------------------------------> O(P)


//----------------------------------------------------------------------------------------------------------------------

    //O(Nb*logP)
    public void agregarBloque(Transaccion[] transacciones){
        bloquePrevio = bloqueActual;
        bloqueActual = new Bloque(transacciones);  // Yo creo un bloque con N transacciones -----> O(Nb)

        if(transacciones.length==0){
            bloqueActual = bloquePrevio;
            return;
        }
        if(!esBloqueValido(transacciones[0], idBloqueActual)){
            bloqueActual = bloquePrevio;
            return;
        }

        for (int i = 0; i < transacciones.length; i++) {
            Transaccion transaccionActual = transacciones[i];
            int comprador = transaccionActual.id_comprador();
            int vendedor = transaccionActual.id_vendedor();
            int monto = transaccionActual.monto();

            if(i>0 && transaccionActual.id<= transacciones[i-1].id){ // Id de transacciones ordenados crecientemente
                bloqueActual=bloquePrevio;
                return;
            }

             if(!esTransaccionValida(transaccionActual, i)){
             bloqueActual=bloquePrevio;
             return;
             }

            if (comprador != 0) {
                usuariosTotales[comprador].actualizarMonto(usuariosTotales[comprador].obtenerMonto() - monto); //-->O(1)
            }
            usuariosTotales[vendedor].actualizarMonto(usuariosTotales[vendedor].obtenerMonto() + monto);      //-->O(1)

                if (comprador != 0) {
                    heapUsers.actualizar(usuariosTotales[comprador]);                                      // --> O(logP)
                }
                heapUsers.actualizar(usuariosTotales[vendedor]);                                          // --> O(logP)
            }
    // tengo ---> O(logP) para cada transferencia que cargo O(Nb)
        if (bloquePrevio != null) {
            idBloqueActual++;
        }
    } // Entonces tengo ---------------------------------------------> O(Nb*logP)

// ---------------------------------------------------------------------------------------------------------------------

    // O(1)
    public Transaccion txMayorValorUltimoBloque(){
        if(bloqueActual == null){
            return null;
        }
        return (Transaccion) bloqueActual.obtenerHeap().devolverMaximo();  // Acá llamo al heap del bloque y al método devolverMaximo
                                                            // como es un heapMax, devolver bloque retorna la raíz heap
                                                           // Y esto es una operación directa a heapTransaccion[0]
    } // Entonces --------------------------> O(1)


// ---------------------------------------------------------------------------------------------------------------------

    //O(Nb)
    public Transaccion[] txUltimoBloque(){
        if (bloqueActual == null || bloqueActual.obtenerTransacciones() == null) {
            return new Transaccion[0];
        }
        //obtenerTransacciones me retorna una copia de la lista enlazada lo cuál es O(Nb)
        return bloqueActual.obtenerTransacciones();
    } // ------------------------------> O(Nb)

// ---------------------------------------------------------------------------------------------------------------------

    // O(1)
    public int maximoTenedor(){
        return ((Usuario) heapUsers.devolverMaximo()).id; // Accedo a la raíz del heapUsuarios y devuelvo su id
    } // Esto es un acceso directo a una posición entonces ------------------> O(1)


// ---------------------------------------------------------------------------------------------------------------------

    // O(1)
    public int montoMedioUltimoBloque(){
        if (bloqueActual == null || bloqueActual.cantidadTransferencias() == 0){
            return 0;
        }
        // Accedo de forma directa al bloque (O(1)) y llamo a dos atributos que ya están calculados
        return (bloqueActual.montoTotal())/(bloqueActual.cantidadTransferencias()); // División de dos enteros
    } //-----------------------------------------------------------------> O(1)



// ---------------------------------------------------------------------------------------------------------------------

    // O(logNb + logP)
    public void hackearTx(){
        if (bloqueActual == null || bloqueActual.obtenerTransacciones().length == 0) return;

        Transaccion hackeada = (Transaccion) bloqueActual.obtenerHeap().devolverMaximo(); // Acceso directo ----> O(1)
        if (hackeada == null) return;

        bloqueActual.hackearBloque();           // Acá MODIFICO el bloque (lista enlazada) y el heapTransacciones
                                               // * MODIFICAR heapTransacciones: Acá me "robo" la transacción máxima
                                              // Al ser un heapMax, tengo que eliminar la raíz
                                             // Para esto intercambio raíz con última posición, elimino, y reordeno
                                            // desde la raíz. Todo esto tiene un coste -------> O(logNb)
        //-------------------------------------------------------------------------------------------------------------
                                          // ** MODIFICAR bloque: dentro de la clase Bloque trabajo bloqueActual como
                                         // una lista enlazada con un handle. Me permite acceder con un puntero (O(1))
                                        // al nodo y eliminarlo (con un método de eliminación para lista enlazada con
                                       // handle, que recibe como parámetro directamente la referencia del handle) O(1)
                                      // Es O(1) esto porque no lo tengo que buscar, en todo momento indico dónde está
                                     // Todo este proceso de modificación del bloque es ------> O(1)

        int vendedor = hackeada.id_vendedor();
        int comprador = hackeada.id_comprador();
        int monto = hackeada.monto();

        // Revertir montos (porque la transacción fue eliminada)
        usuariosTotales[vendedor].actualizarMonto(usuariosTotales[vendedor].obtenerMonto() - monto); // Actualizo el
                                                                                                    // ArrayUsuarios
                                                                                                   // --> O(1)
        heapUsers.actualizar(usuariosTotales[vendedor]); // Actualizo el heap de usuarios indicandole el usuario
                                                        // El método actualizar recibe un usuario y apunta al índice
                                                       // En todo momento tengo guardado en el usuario su ubicación en
                                                      // el heap. Por eso en el método actualizar uso el reordenamiento
                                                     // indicandole directamente el índice guardado el usuario del heap
                                                    // * Indicar qué usuario actualizar -> O(1)
                                                   // ** Reordenar el heap -----------------------> O(logP)

        if (comprador != 0) {
            usuariosTotales[comprador].actualizarMonto(usuariosTotales[comprador].obtenerMonto() + monto);
            heapUsers.actualizar(usuariosTotales[comprador]); // Lo mismo con el comprador
        }
        // ENTONCES: Los procesos de eliminación no son más que accesos directos -> O(1)
        // y los reordenamientos de los heap miden: *  heapTransacciones: O(logNb)
        //                                          ** heapUsuarios: O(logP)

    }//--------------> O(logNb + logP)


//---------------------------------------------------------------------------------------------------------------------

    public Boolean esTransaccionValida(Transaccion transaccion, int indice){
        if (indice!=0 && transaccion.id_comprador() == 0) {
            return false;
        }
        if (transaccion.id_comprador() != 0 && usuariosTotales[transaccion.id_comprador()].obtenerMonto() < usuariosTotales[transaccion.id_comprador()].obtenerMonto()) {
            return false;
        }
        if(transaccion.id_comprador() == transaccion.id_vendedor()){
            return false;
        }
        if(transaccion.monto() <= 0){
            return false;

        }
        return true;
    }
    public Boolean esBloqueValido(Transaccion transaccion, int indice){
        if(idBloqueActual<2999 && transaccion.id_comprador()!=0){
            return false;
        }
        if (idBloqueActual>2999 && transaccion.id_comprador()==0){
            return false;
        }
        return true;
    }

//---------------------------------------------------------------------------------------------------------------------
}