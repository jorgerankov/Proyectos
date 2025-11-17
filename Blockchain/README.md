# Blockchain

## Descripción
Implementación educativa de tecnología blockchain con estructura de bloques, cadenas y validación criptográfica.

## Tecnologías Utilizadas
- **JavaScript**: Lógica principal y minería
- **HTML/CSS**: Interfaz de usuario
- **Criptografía SHA-256**: Hashing de bloques
- **Conceptos**: Proof of Work, validación de cadena

## Requisitos Previos
- Node.js (versión 14 o superior)
- npm
- Conocimiento básico de blockchain

## Instalación

1. Clona el repositorio:
```bash
git clone https://github.com/jorgerankov/Proyectos.git
cd Proyectos/Blockchain
```

2. Instala las dependencias:
```bash
npm install
```

3. Ejecuta la aplicación:
```bash
npm start
```

O abre `index.html` si es un proyecto frontend puro.

## Uso

### Crear un blockchain:
```javascript
const blockchain = new Blockchain();
```

### Agregar transacciones:
```javascript
blockchain.addTransaction({from: "Alice", to: "Bob", amount: 10});
```

### Minar un bloque:
```javascript
blockchain.minePendingTransactions("Miner");
```

### Validar la cadena:
```javascript
console.log(blockchain.isChainValid());
```

## Estructura del Proyecto
- `/src/blockchain.js` - Clase principal Blockchain
- `/src/block.js` - Clase Block
- `/index.html` - Interfaz de demostración
- `/css/styles.css` - Estilos

## Características Principales
- Creación de bloques con hash SHA-256
- Validación de integridad de cadena
- Proof of Work (minería)
- Detección de manipulación

## Contribuciones
Las contribuciones son bienvenidas.
