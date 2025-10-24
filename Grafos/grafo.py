class Nodo:
    def __init__(self, vertice, peso):
        self.vertice = vertice
        self.peso = peso
        self.siguiente = None

class Grafo:
    def __init__(self, num_vertices):
        self.num_vertices = num_vertices
        self.adyacencia = [None] * num_vertices
    
    def agregar_arista(self, origen, destino, peso):
        """Agregar arista dirigida"""
        nuevo_nodo = Nodo(destino, peso)
        nuevo_nodo.siguiente = self.adyacencia[origen]
        self.adyacencia[origen] = nuevo_nodo
    
    def agregar_arista_bidireccional(self, u, v, peso):
        """Agregar arista no dirigida (bidireccional)"""
        self.agregar_arista(u, v, peso)
        self.agregar_arista(v, u, peso)
    
    def mostrar(self):
        for i in range(self.num_vertices):
            print(f"Vértice {i}:", end=" ")
            actual = self.adyacencia[i]
            while actual:
                print(f"-> [{actual.vertice}, peso={actual.peso}]", end=" ")
                actual = actual.siguiente
            print()