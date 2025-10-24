def borrar(cant_letras, palabra) -> int:
    if cant_letras != len(palabra) : 
        return -1;
    
    memo = {}

    def dp(l,r):
        if l > r: return 0;         # Si me quede sin letras
        if l == r: return 1;        # Si l y r son el mismo indice (un solo caracter)

        if (l, r) in memo:          # Chequeo si el par existe en mi memoria
            return memo[(l, r)]     # Si existe, lo retorno

        res = 1 + dp(l+1, r)        # Sino, borro el caracter en la pos l y resuelvo recursivamente el resto

        for k in range (l+1, r+1):
            if palabra[k] == palabra[l]:                    # Busco posiciones donde la letra k sea igual a l
                res = min(res, dp(l+1, k-1) + dp(k,r))      # Si la hay, intento borrar lo de en medio, 
                                                            # para juntar palabra[l] y palabra[k]
        memo[(l, r)] = res                                  # Guardo el menor costo en mi memo
        return res                                          # Devuelvo el costo

    return dp(0, len(palabra)-1)                            # Llamo recursivamente a toda la palabra

def main():
    cantidad_letras = int(input())
    palabra = input().strip()
    resultado = borrar(cantidad_letras, palabra)
    print(resultado)

if __name__ == "__main__":
    main()