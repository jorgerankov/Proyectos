#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

// Función que cuenta directamente los 1s en el rango
int contarUnos(long long n, long long inicio, long long fin) {
    // Si el número es 0 o 1, simplemente verificamos si está en el rango
    if (n <= 1) {
        return (n == 1 && inicio <= 1 && fin >= 1) ? 1 : 0;
    }
    
    // Calculamos la longitud de la representación para este número
    long long longitud = 0;
    long long temp = n;
    
    // Calculamos la longitud de la secuencia expandida
    while (temp > 1) {
        temp = 2 * (temp / 2) + (temp % 2);
        longitud = 2 * longitud + 1;
    }
    longitud = 2 * longitud + 1;
    
    // Si el rango está completamente fuera de la secuencia, retornamos 0
    if (fin < 1 || inicio > longitud) {
        return 0;
    }
    
    // Si n es par, la secuencia es [n/2, 0, n/2]
    if (n % 2 == 0) {
        long long mitad = n / 2;
        long long tercioLongitud = (longitud - 1) / 2;
        
        int result = 0;
        
        // Contamos los 1s en la primera parte
        if (inicio <= tercioLongitud) {
            result += contarUnos(mitad, inicio, min(fin, tercioLongitud));
        }
        
        // Contamos los 1s en la tercera parte
        if (fin > tercioLongitud + 1) {
            result += contarUnos(mitad, max(1LL, inicio - tercioLongitud - 1), fin - tercioLongitud - 1);
        }
        
        return result;
    } 
    // Si n es impar, la secuencia es [n/2, 1, n/2]
    else {
        long long mitad = n / 2;
        long long tercioLongitud = (longitud - 1) / 2;
        
        int result = 0;
        
        // Contamos los 1s en la primera parte
        if (inicio <= tercioLongitud) {
            result += contarUnos(mitad, inicio, min(fin, tercioLongitud));
        }
        
        // Verificamos si el 1 del medio está en el rango
        if (inicio <= tercioLongitud + 1 && fin >= tercioLongitud + 1) {
            result++;
        }
        
        // Contamos los 1s en la tercera parte
        if (fin > tercioLongitud + 1) {
            result += contarUnos(mitad, max(1LL, inicio - tercioLongitud - 1), fin - tercioLongitud - 1);
        }
        
        return result;
    }
}

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);
    
    long long n, inicio, fin;
    cin >> n >> inicio >> fin;
    
    cout << contarUnos(n, inicio, fin) << endl;
    
    return 0;
}