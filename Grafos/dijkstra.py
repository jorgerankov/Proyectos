import heapq

class Dijkstra:
    """Template general de Dijkstra para grafos con pesos positivos"""
    
    def __init__(self, grafo):
        """
        grafo: objeto con atributo 'adyacencia' (lista de nodos enlazados)
               y atributo 'num_vertices'
        """
        self.grafo = grafo
    
    # ============================================
    # 1. DIJKSTRA BÁSICO - Camino más corto
    # ============================================
    def camino_mas_corto(self, inicio, fin):
        """
        Encuentra el camino de menor peso desde 'inicio' hasta 'fin'
        Retorna: (camino, peso_total) o (None, inf) si no existe
        """
        distancias = [float('inf')] * self.grafo.num_vertices
        distancias[inicio] = 0
        padre = [-1] * self.grafo.num_vertices
        visitados = [False] * self.grafo.num_vertices
        
        # Cola de prioridad: (distancia_acumulada, vértice)
        pq = [(0, inicio)]
        
        while pq:
            dist_actual, u = heapq.heappop(pq)
            
            # Si ya visitamos este vértice, skip
            if visitados[u]:
                continue
            
            visitados[u] = True
            
            # Si llegamos al destino, podemos terminar
            if u == fin:
                break
            
            # Explorar todas las adyacencias
            nodo = self.grafo.adyacencia[u]
            while nodo:
                v = nodo.vertice
                peso = nodo.peso
                
                # Relajación de arista
                if not visitados[v] and distancias[u] + peso < distancias[v]:
                    distancias[v] = distancias[u] + peso
                    padre[v] = u
                    heapq.heappush(pq, (distancias[v], v))
                
                nodo = nodo.siguiente
        
        # Si no hay camino
        if distancias[fin] == float('inf'):
            return None, float('inf')
        
        # Reconstruir camino
        camino = self._reconstruir_camino(padre, inicio, fin)
        return camino, distancias[fin]
    
    # ============================================
    # 2. DIJKSTRA - Desde un origen a TODOS
    # ============================================
    def desde_origen_a_todos(self, inicio):
        """
        Calcula las distancias mínimas desde 'inicio' a TODOS los vértices
        Retorna: (distancias, padres)
        """
        distancias = [float('inf')] * self.grafo.num_vertices
        distancias[inicio] = 0
        padre = [-1] * self.grafo.num_vertices
        visitados = [False] * self.grafo.num_vertices
        
        pq = [(0, inicio)]
        
        while pq:
            dist_actual, u = heapq.heappop(pq)
            
            if visitados[u]:
                continue
            
            visitados[u] = True
            
            # Explorar adyacencias
            nodo = self.grafo.adyacencia[u]
            while nodo:
                v = nodo.vertice
                peso = nodo.peso
                
                if not visitados[v] and distancias[u] + peso < distancias[v]:
                    distancias[v] = distancias[u] + peso
                    padre[v] = u
                    heapq.heappush(pq, (distancias[v], v))
                
                nodo = nodo.siguiente
        
        return distancias, padre
    
    # ============================================
    # 3. DIJKSTRA - Camino con detalle de aristas
    # ============================================
    def camino_con_aristas(self, inicio, fin):
        """
        Retorna el camino con información detallada de cada arista
        Formato: [(vertice_origen, vertice_destino, peso), ...]
        """
        distancias = [float('inf')] * self.grafo.num_vertices
        distancias[inicio] = 0
        padre = [-1] * self.grafo.num_vertices
        peso_arista = {}  # Guardar el peso de cada arista usada
        visitados = [False] * self.grafo.num_vertices
        
        pq = [(0, inicio)]
        
        while pq:
            dist_actual, u = heapq.heappop(pq)
            
            if visitados[u]:
                continue
            
            visitados[u] = True
            
            if u == fin:
                break
            
            nodo = self.grafo.adyacencia[u]
            while nodo:
                v = nodo.vertice
                peso = nodo.peso
                
                if not visitados[v] and distancias[u] + peso < distancias[v]:
                    distancias[v] = distancias[u] + peso
                    padre[v] = u
                    peso_arista[v] = peso  # Guardar peso de la arista
                    heapq.heappush(pq, (distancias[v], v))
                
                nodo = nodo.siguiente
        
        if distancias[fin] == float('inf'):
            return None, float('inf')
        
        # Reconstruir con información de aristas
        aristas = []
        actual = fin
        while padre[actual] != -1:
            aristas.append((padre[actual], actual, peso_arista[actual]))
            actual = padre[actual]
        
        aristas.reverse()
        return aristas, distancias[fin]
    
    # ============================================
    # 4. DIJKSTRA - K caminos más cortos
    # ============================================
    def k_caminos_mas_cortos(self, inicio, fin, k):
        """
        Encuentra los K caminos más cortos (puede haber caminos con mismo peso)
        Retorna: lista de (camino, peso)
        """
        # Cola: (distancia, vertice, camino_recorrido)
        pq = [(0, inicio, [inicio])]
        caminos_encontrados = []
        conteo_llegadas = {}
        
        while pq and len(caminos_encontrados) < k:
            dist, u, camino = heapq.heappop(pq)
            
            # Si llegamos al destino
            if u == fin:
                caminos_encontrados.append((list(camino), dist))
                continue
            
            # Limitar cuántas veces visitamos cada vértice
            if u in conteo_llegadas:
                conteo_llegadas[u] += 1
                if conteo_llegadas[u] > k:
                    continue
            else:
                conteo_llegadas[u] = 1
            
            # Explorar adyacencias
            nodo = self.grafo.adyacencia[u]
            while nodo:
                v = nodo.vertice
                if v not in camino:  # Evitar ciclos
                    nuevo_camino = camino + [v]
                    nueva_dist = dist + nodo.peso
                    heapq.heappush(pq, (nueva_dist, v, nuevo_camino))
                nodo = nodo.siguiente
        
        return caminos_encontrados
    
    # ============================================
    # 5. DIJKSTRA - Con restricciones
    # ============================================
    def camino_con_restriccion(self, inicio, fin, vertices_obligatorios=None, vertices_prohibidos=None):
        """
        Encuentra camino más corto con restricciones:
        - vertices_obligatorios: lista de vértices que DEBE pasar
        - vertices_prohibidos: lista de vértices que NO puede pasar
        """
        if vertices_prohibidos is None:
            vertices_prohibidos = set()
        
        if vertices_obligatorios is None:
            # Dijkstra normal evitando prohibidos
            return self._dijkstra_evitando(inicio, fin, vertices_prohibidos)
        
        # Si hay vértices obligatorios, resolver como TSP simplificado
        # (esto es más complejo, aquí una versión básica)
        return self._dijkstra_con_obligatorios(inicio, fin, vertices_obligatorios, vertices_prohibidos)
    
    def _dijkstra_evitando(self, inicio, fin, prohibidos):
        """Dijkstra evitando ciertos vértices"""
        distancias = [float('inf')] * self.grafo.num_vertices
        distancias[inicio] = 0
        padre = [-1] * self.grafo.num_vertices
        visitados = [False] * self.grafo.num_vertices
        
        pq = [(0, inicio)]
        
        while pq:
            dist_actual, u = heapq.heappop(pq)
            
            if visitados[u]:
                continue
            
            visitados[u] = True
            
            if u == fin:
                break
            
            nodo = self.grafo.adyacencia[u]
            while nodo:
                v = nodo.vertice
                peso = nodo.peso
                
                # Saltar vértices prohibidos (excepto el destino)
                if v in prohibidos and v != fin:
                    nodo = nodo.siguiente
                    continue
                
                if not visitados[v] and distancias[u] + peso < distancias[v]:
                    distancias[v] = distancias[u] + peso
                    padre[v] = u
                    heapq.heappush(pq, (distancias[v], v))
                
                nodo = nodo.siguiente
        
        if distancias[fin] == float('inf'):
            return None, float('inf')
        
        camino = self._reconstruir_camino(padre, inicio, fin)
        return camino, distancias[fin]
    
    # ============================================
    # 6. DIJKSTRA - Visualización paso a paso
    # ============================================
    def camino_con_debug(self, inicio, fin):
        """Muestra el proceso paso a paso de Dijkstra"""
        distancias = [float('inf')] * self.grafo.num_vertices
        distancias[inicio] = 0
        padre = [-1] * self.grafo.num_vertices
        visitados = [False] * self.grafo.num_vertices
        
        pq = [(0, inicio)]
        paso = 0
        
        print(f"\nIniciando Dijkstra desde {inicio} hasta {fin}")
        print("="*60)
        
        while pq:
            dist_actual, u = heapq.heappop(pq)
            
            if visitados[u]:
                continue
            
            visitados[u] = True
            paso += 1
            
            print(f"\nPaso {paso}: Visitando vértice {u} (distancia acumulada: {dist_actual})")
            print(f"  Distancias actuales: {distancias}")
            
            if u == fin:
                print(f"\n¡Llegamos al destino {fin}!")
                break
            
            nodo = self.grafo.adyacencia[u]
            print(f"  Explorando adyacencias de {u}:")
            
            while nodo:
                v = nodo.vertice
                peso = nodo.peso
                
                if not visitados[v]:
                    nueva_dist = distancias[u] + peso
                    if nueva_dist < distancias[v]:
                        print(f"    {u} -> {v} (peso={peso}): "
                              f"Actualizar distancia de {distancias[v]} a {nueva_dist}")
                        distancias[v] = nueva_dist
                        padre[v] = u
                        heapq.heappush(pq, (distancias[v], v))
                    else:
                        print(f"    {u} -> {v} (peso={peso}): "
                              f"No mejora (actual={distancias[v]}, nueva={nueva_dist})")
                
                nodo = nodo.siguiente
        
        if distancias[fin] == float('inf'):
            print(f"\nNo existe camino de {inicio} a {fin}")
            return None, float('inf')
        
        camino = self._reconstruir_camino(padre, inicio, fin)
        print(f"\nCamino final: {' -> '.join(map(str, camino))}")
        print(f"Peso total: {distancias[fin]}")
        print("="*60)
        
        return camino, distancias[fin]
    
    # ============================================
    # FUNCIONES AUXILIARES
    # ============================================
    def _reconstruir_camino(self, padre, inicio, fin):
        """Reconstruye el camino desde el array de padres"""
        camino = []
        actual = fin
        
        while actual != -1:
            camino.append(actual)
            actual = padre[actual]
        
        camino.reverse()
        return camino
    
    def _dijkstra_con_obligatorios(self, inicio, fin, obligatorios, prohibidos):
        """Versión simplificada con vértices obligatorios"""
        # Aproximación: encontrar camino que pase por todos los obligatorios
        # Esto es NP-completo en general, aquí una heurística simple
        
        vertices_a_visitar = [inicio] + list(obligatorios) + [fin]
        camino_total = []
        peso_total = 0
        
        for i in range(len(vertices_a_visitar) - 1):
            origen = vertices_a_visitar[i]
            destino = vertices_a_visitar[i + 1]
            
            camino_parcial, peso_parcial = self._dijkstra_evitando(origen, destino, prohibidos)
            
            if camino_parcial is None:
                return None, float('inf')
            
            if camino_total:
                camino_total.extend(camino_parcial[1:])  # Evitar duplicar vértice
            else:
                camino_total = camino_parcial
            
            peso_total += peso_parcial
        
        return camino_total, peso_total