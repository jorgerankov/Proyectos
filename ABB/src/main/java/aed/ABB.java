package aed;

public class ABB<T extends Comparable<T>> implements Conjunto<T> {
    private Nodo _raiz;
    private int _cardinal;
    private int _altura;

    private class Nodo {
        T valor;
        Nodo izq;
        Nodo der;
        Nodo padre;

        Nodo (T v) {
            valor = v;
            izq = null;
            der = null;
            padre = null;
        }
    }

    public ABB() {
        _raiz = null;
        _cardinal = 0;
        _altura = 0;
    }

    public int cardinal() {
        return _cardinal;
    }

    public T minimo(){
        if (_raiz == null) {
            return null;
        }
        T min = _raiz.valor;
        Nodo actual = _raiz;
        while (actual != null) {
            if (actual.valor.compareTo(min) < 0) {
                min = actual.valor;
            }
            actual = actual.izq;
        }
        return min;
    }

    public T maximo(){
        if (_raiz == null) {
            return null;
        }
        T max = _raiz.valor;
        Nodo actual = _raiz;
        while (actual != null) {
            if (actual.valor.compareTo(max) > 0){
                max = actual.valor;
            }
            actual = actual.der;
        }
        return max;
    }

    public void insertar(T elem) {
        _raiz = insertarAux(_raiz, elem, null);
    }
    private Nodo insertarAux(Nodo raiz, T e, Nodo padre) {
        if (raiz == null) {
            Nodo nuevo = new Nodo(e);
            nuevo.padre = padre;
            _cardinal++;
            return nuevo;
        } else if (e.compareTo(raiz.valor) < 0) {
            raiz.izq = insertarAux(raiz.izq, e, raiz);
        } else if (e.compareTo(raiz.valor) > 0) {
            raiz.der = insertarAux(raiz.der, e, raiz);
        }
        // Si ya existe, no se inserta nada
        return raiz;
    }

    public boolean pertenece(T elem) {
        Nodo actual = _raiz;
        while (actual != null) {
            int comp = elem.compareTo(actual.valor);
            if (comp == 0) {
                return true;
            } else if (comp < 0) {
                actual = actual.izq;
            } else {
                actual = actual.der;
            }
        }
        return false;
    }
    
    public void eliminar(T elem){
        _raiz = eliminarAux(_raiz, elem);
    } 
    private Nodo eliminarAux(Nodo raiz, T elem){
        Nodo actual = raiz;

        if (actual == null) {
            return actual;
        } else if (elem.compareTo(actual.valor) < 0) {
            actual.izq = eliminarAux(actual.izq, elem);
        } else if (elem.compareTo(actual.valor) > 0) {
            actual.der = eliminarAux(actual.der, elem);
        } else { // Nodo encontrado
            if(actual.izq == null && actual.der == null) {
                _cardinal--;
                actual = null;
            } else if(actual.der != null) { // busco sucesor (hijo)
                actual.valor = hijo(actual);
                actual.der = eliminarAux(actual.der, actual.valor);
            } else { // busco predecesor (padre)
                actual.valor = padre(actual);
                actual.izq = eliminarAux(actual.izq, actual.valor);
            }
        }
        return actual;
    }
    private T hijo(Nodo raiz) { // Busco menor valor de la rama der
        raiz = raiz.der;
        while (raiz.izq != null) {
            raiz = raiz.izq;
        }
        return raiz.valor;
    }
    private T padre(Nodo raiz) { // Busco mayor valor de la rama izq
        raiz = raiz.izq;
        while (raiz.der != null) {
            raiz = raiz.der;
        }
        return raiz.valor;
    }

    public String toString() {
        String str = toStrAux(_raiz);
        return "{" + str + "}";
    }
    private String toStrAux(Nodo raiz) {
        if (raiz == null) {
            return "";
        }
        StringBuilder res = new StringBuilder();
        String izqStr = toStrAux(raiz.izq);
        if (!izqStr.isEmpty()) {
            res.append(izqStr).append(",");
        }
        res.append(raiz.valor.toString());
        String derStr = toStrAux(raiz.der);
        if (!derStr.isEmpty()) {
            res.append(",").append(derStr);
        }
        return res.toString();
    }

    private class ABB_Iterador implements Iterador<T> {
        private Nodo _actual = _raiz;
        
        public ABB_Iterador() {
            _actual = _raiz;
            if (_actual != null) {
                while (_actual.izq != null) {
                    _actual = _actual.izq;
                }
            }
        }

        @Override
        public boolean haySiguiente() {      
            return _actual != null;
        }
        
        @Override
        public T siguiente() {
            
            if (!haySiguiente()) {
                return null;
            }

            T valor = _actual.valor;

            if (_actual.der != null){
                _actual = _actual.der;
                while (_actual.izq != null){
                    _actual = _actual.izq;
                }
            } else {
                Nodo padre = _actual.padre;
                while (padre != null && _actual == padre.der) {
                    _actual = padre;
                    padre = padre.padre;
                }
                _actual = padre;
            }
            return valor;
        }
    }

    public Iterador<T> iterador() {
        return new ABB_Iterador();
    }

}
