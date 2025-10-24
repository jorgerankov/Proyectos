def resolver_memo_simple(palabras, costos):
    memo = {}
    
    def dp(i, anterior):
        if i == len(palabras):
            return 0
        if (i, anterior) in memo:
            return memo[(i, anterior)]
        
        normal = palabras[i]
        invertida = palabras[i][::-1]
        
        res = float('inf')
        
        # Probar sin invertir
        if anterior <= normal:
            res = min(res, dp(i + 1, normal))
        
        # Probar invirtiendo
        if anterior <= invertida:
            res = min(res, costos[i] + dp(i + 1, invertida))
        
        memo[(i, anterior)] = res
        return res
    
    resultado = dp(0, "")
    return resultado if resultado != float('inf') else -1
    resultado = min(dp[n-1][0], dp[n-1][1])
    return resultado if resultado != float('inf') else -1



# Como hacerlo recursivo?
# Tomar los 2 primeros elementos y compararlos entre si, sumar el valor que me den
# Y acumular ese resultado con la nueva llamada entre el segundo valor y el proximo valor a analizar
# por cada palabra tengo un unico valor que va a ser res += valor en caso de tener q invertir la palabra
# en caso de q ningun match coincida -> -1 
# Como analizar los casos donde palabra_i[0] == palabra_j[0]? Veo segundo elemento de ambos o invierto las palabras?
# Probar invirtiendo la primer palabra, si da ok seguir con evaluar palabra_j[0] < palabra_k[0]
# y hacer res += valor_primera_palabra
# Sino, invierto palabra_j[0] y evaluo. Y asi sucesivamente  