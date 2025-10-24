from collections import deque

def pasos_minimos(n, m):
    """
    Devuelvo la menor cantidad de pasos para ir de n a m
    Operaciones permitidas: n * 2 o n - 1
    
    Args:
        n: número inicial (>= 1)
        m: número objetivo (>= 1)
    """
    # Caso trivial
    if n == m:
        return 0
    
    # BFS: cola de (valor_actual, pasos_dados)
    cola = deque([(n, 0)])
    visitados = {n}  # Conjunto para evitar procesar el mismo número dos veces => Parecido a usar DP
    
    while len(cola) > 0:
        actual, pasos = cola.popleft()
        
        # Generar los dos vecinos posibles
        vecinos = [
            actual * 2,      # Multiplicar por 2
            actual - 1       # Restar 1
        ]
        
        for vecino in vecinos:
            # Validar que el vecino sea válido (>= 1)
            if vecino < 1:
                continue
            
            # Si encontramos el objetivo
            if vecino == m:
                return pasos + 1
            
            # Si no lo hemos visitado, agregarlo a la cola
            if vecino not in visitados:
                visitados.add(vecino)
                cola.append((vecino, pasos + 1))
    
    # No debería llegar aquí si n y m son válidos
    return -1


# Ejemplos de uso
def test_pasos_minimos():
    print("Pruebas de pasos_minimos:")
    print("="*50)
    
    # Ejemplo 1: 8 -> 12
    n, m = 8, 12
    resultado = pasos_minimos(n, m)
    print(f"{n} -> {m}: {resultado} pasos")
    # Camino: 8 -> 7 -> 6 -> 12 (3 pasos)
    
    # Ejemplo 2: 1 -> 10
    n, m = 1, 10
    resultado = pasos_minimos(n, m)
    print(f"{n} -> {m}: {resultado} pasos")
    # Camino: 1 -> 2 -> 4 -> 8 -> 16 -> 15 -> 14 -> 13 -> 12 -> 11 -> 10
    
    # Ejemplo 3: 5 -> 5
    n, m = 5, 5
    resultado = pasos_minimos(n, m)
    print(f"{n} -> {m}: {resultado} pasos")
    
    # Ejemplo 4: 10 -> 1
    n, m = 10, 1
    resultado = pasos_minimos(n, m)
    print(f"{n} -> {m}: {resultado} pasos")
    # Camino: 10 -> 9 -> 8 -> 7 -> 6 -> 5 -> 4 -> 3 -> 2 -> 1 (9 pasos)

test_pasos_minimos()