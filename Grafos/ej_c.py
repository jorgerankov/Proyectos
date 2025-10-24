"""
Puedo hacer una matriz de adyacencia tal que en la posicion [i][j] guardo k (valor 3 del input) si tiene conexion
o guardo 0 si no tiene conexion
"""
class Nodo:
    def __init__(self, vertice, peso):
        self.vertice = vertice  # Vértice destino
        self.peso = peso        # Peso de la arista
        self.siguiente = None   # Apuntador al siguiente nodo

class ListaAdyacencia:
    def __init__(self, num_vertices):
        # Crear un arreglo de cabezas de listas (una por cada vértice)
        self.num_vertices = num_vertices
        self.adyacencia = [None] * num_vertices
    
    def agregar_arista(self, origen, destino, peso):
        # Crear nuevo nodo
        nuevo_nodo = Nodo(destino, peso)
        
        # Insertar al inicio de la lista del vértice origen
        nuevo_nodo.siguiente = self.adyacencia[origen]
        self.adyacencia[origen] = nuevo_nodo
    
    def mostrar(self):
        for i in range(self.num_vertices):
            print(f"Nodo {i+1}:", end=" ")
            actual = self.adyacencia[i]
            while actual:
                print(f"-> [{actual.vertice  + 1}, peso={actual.peso}]", end=" ")
                actual = actual.siguiente
            print()

# Ejemplo de uso
def viajeIntergalactico():
    pares_conexiones = [[0, 1, 2], [0, 2, 3], [0, 3, 9], [1, 2, 5], [1, 3, 6], [2, 3, 3]]
    
    
    # Crear lista de adyacencia para 4 vértices
    grafo = ListaAdyacencia(4)
    
    # Agregar todas las aristas
    for origen, destino, peso in pares_conexiones:
        grafo.agregar_arista(origen, destino, peso)
    
    # Mostrar el grafo
    grafo.mostrar()

viajeIntergalactico()