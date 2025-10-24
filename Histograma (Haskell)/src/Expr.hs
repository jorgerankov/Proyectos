module Expr
  ( Expr (..),
    recrExpr,
    foldExpr,
    eval,
    armarHistograma,
    evalHistograma,
    mostrar,
  )
where

import Util ( infinitoNegativo, infinitoPositivo )
import Generador
import Histograma

-- | Expresiones aritméticas con rangos
data Expr = Const Float
          | Rango Float Float
          | Suma Expr Expr
          | Resta Expr Expr
          | Mult Expr Expr
          | Div Expr Expr
  deriving (Show, Eq)


-- | @recrExpr fConst fRango fSuma fResta fMult fDiv expr@ procesa @expr@ utilizando el esquema de recursión primitiva,
-- donde @fConst@, @fRango@, @fSuma@, @fResta@, @fMult@ y @fDiv@ son las funciones específicas para procesar cada
-- constructor de Expr. Todas estas funciones reciben los mismos parámetros que el constructor con el agregado de
-- un parámetro de tipo Expr, que es la propia expresión que está siendo evaluada (esto es en general de importancia
-- para las funciones @fSuma@, @fResta@, @fMult@ y @fDiv@)
{-
    IDEA:   Se implementa el esquema de recursión primitiva para el cual se requieren n funciones (una por cada constructor
            disponible en el tipo Expr). Estas funciones toman no solo los atributos de dicho constructor, sino que además
            toman el propio Expr que está siendo evaluado, dado que la recursión primitiva permite "ver" el resto de la
            información en cada punto de procesamiento.

            La recursión primitiva nos deja saber las subestructuras que tenemos en un principio, más los resultados 
            recursivos. Esto es importante para saber de qué tipo de expresión son las subexpresiones, cosa que nos 
            puede servir, por ejemplo, en la función 'mostrar'.

            Observamos que Const y Rango sólo deberían tomar los elementos de tipo Float, ya que tenemos toda la información
            en los Float desde un inicio. Por ejemplo, pasarle la expresión completa a Const 2.0 es redundante.
-}
recrExpr :: (Float -> a) -> (Float -> Float -> a)
            -> (a -> a -> Expr -> Expr -> a) -> (a -> a -> Expr -> Expr -> a)
            -> (a -> a -> Expr -> Expr -> a) -> (a -> a -> Expr -> Expr -> a)
            -> Expr -> a
recrExpr fConst fRango fSuma fResta fMult fDiv expr =
    case expr of
        Const f             -> fConst f
        Rango s e           -> fRango s e
        Suma expr1 expr2    -> fSuma  (rec expr1) (rec expr2) expr1 expr2
        Resta expr1 expr2   -> fResta (rec expr1) (rec expr2) expr1 expr2
        Mult expr1 expr2    -> fMult  (rec expr1) (rec expr2) expr1 expr2
        Div expr1 expr2     -> fDiv   (rec expr1) (rec expr2) expr1 expr2
    where
        rec = recrExpr fConst fRango fSuma fResta fMult fDiv

{-
    IDEA:   Se implementa el esquema de recursión estructural para el cual se requieren n funciones (una por cada constructor
            disponible en el tipo Expr). Estas funciones toman los atributos de dicho constructor
-}
foldExpr :: (Float -> a) -> (Float -> Float -> a) -> (a -> a -> a) -> (a -> a -> a) -> (a -> a -> a) -> (a -> a -> a) -> Expr -> a
foldExpr fConst fRango fSuma fResta fMult fDiv expr =
    case expr of
        Const f             -> fConst f
        Rango s e           -> fRango s e
        Suma expr1 expr2    -> fSuma  (rec expr1) (rec expr2)
        Resta expr1 expr2   -> fResta (rec expr1) (rec expr2)
        Mult expr1 expr2    -> fMult  (rec expr1) (rec expr2)
        Div expr1 expr2     -> fDiv   (rec expr1) (rec expr2)
    where
        rec e = foldExpr fConst fRango fSuma fResta fMult fDiv e


-- | Evaluar expresiones dado un generador de números aleatorios
{-
    IDEA:   Se procesa el árbol representado por la expresión, de forma que para cada nodo se arma una función
        (currificada) que resuelve la operación, pero a la cual le queda el parámetro generador pendiente
        (evaluación parcial). Hay 3 tipos de función generada, asociada a los tipos de nodos de la
        expresión:
            - Const: retorna un número constante.
            - Rango: retorna dameUno con el rango, pero que no queda evaluada por faltar el generador.
            - Operador binario: para +, -, * y / que toma dos operandos (left y right) y aplica, pasando el
              generador por el primer operando y el generador actualizado al segundo.
        Notar que en el caso fDiv, se pasa una función específica y no el operador (/). Esto es para lograr
        que la evaluación del caso Div sea total. Esta función observa el divisor y si es 0, entonces
        satura el resultado en +/- infinito (según el signo del dividendo).
-}
eval :: Expr -> G Float
eval expr = foldExpr fConst fRango fSuma fResta fMult fDiv expr
    where
        -- las funciones directas en uso
        fConst f = _const f
        fRango s e = dameUno (s, e)
        fSuma l r = _binOper (+) l r
        fResta l r = _binOper (-) l r
        fMult l r = _binOper (*) l r
        fDiv l r = _binOper _operDiv l r
        -- auxiliares para Const y Div
        _const f gen = (f, gen)
        _operDiv l r = if r == 0 then if l < 0 then infinitoNegativo else infinitoPositivo else l / r
        -- procesamos operaciones binarias
        _binOper :: (Float -> Float -> Float) -> G Float -> G Float -> G Float
        _binOper op opLeft opDer gen = (op valorIzq valorDer, genDer)
            where
                (valorIzq, genIzq)  = opLeft gen
                (valorDer, genDer) = opDer genIzq




-- | @armarHistograma m n f g@ arma un histograma con @m@ casilleros
-- a partir del resultado de tomar @n@ muestras de @f@ usando el generador @g@.
{-
    IDEA:   Se evalúan dos funciones (muestra y rango95), que retornarán datos parciales necesarios
            para crear el histograma.
            
            La función muestra permite obtener una muestra de n números.
            Esta muestra es luego pasada a la función rango95, que calcula el rango de 95% de confianza (lower y upper)
            que permitirá definir los casilleros del histograma.
-}
armarHistograma :: Int -> Int -> G Float -> G Histograma
armarHistograma m n f g = (histograma m (rangoInicio, rangoFin) muestras, g_modificado)
    where
        (muestras, g_modificado) = muestra f n g
        (rangoInicio, rangoFin) = rango95 muestras



-- | @evalHistograma m n e g@ evalúa la expresión @e@ usando el generador @g@ @n@ veces
-- devuelve un histograma con @m@ casilleros y rango calculado con @rango95@ para abarcar el 95% de confianza de los valores.
-- @n@ debe ser mayor que 0.
{-
    IDEA:   Esta función es en general un simple wrapper a armarHistograma, solo que recibe una expresión en lugar de
            f y dicha expresión se convierte a función realizando una evaluación parcial
-}
evalHistograma :: Int -> Int -> Expr -> G Histograma
evalHistograma m n expr = armarHistograma m n (eval expr)


-- Podemos armar Histogramas que muestren las n evaluaciones en m casilleros.
-- >>> evalHistograma 11 10 (Suma (Rango 1 5) (Rango 100 105)) (genNormalConSemilla 0)
-- (Histograma 102.005486 0.6733038 [1,0,0,0,1,3,1,2,0,0,1,1,0],<Gen>)

-- >>> evalHistograma 11 10000 (Suma (Rango 1 5) (Rango 100 105)) (genNormalConSemilla 0)
-- (Histograma 102.273895 0.5878462 [239,288,522,810,1110,1389,1394,1295,1076,793,520,310,254],<Gen>)

-- | Mostrar las expresiones, pero evitando algunos paréntesis innecesarios.
-- En particular queremos evitar paréntesis en sumas y productos anidados.
{-
    IDEA:   La resolución utiliza recrExpr debido a que es necesario poder evaluar el nodo y sus hijos para decidir
            cuándo se debe o no presentar paréntesis (ej.: padre + e hijo + => no se usan paréntesis alrededor del hijo).
            Esto implica la necesidad de utilizar recursión primitiva, de ahí recrExpr.
            Respecto de la resolución, se consideran 3 tipos de casos:
                - Const: en donde simplemente se muestra el número de punto flotante.
                - Range: en donde se concatenan los dos valores de punto flotante junto con "~".
                - Operadores binarios (Resta, Div, Suma, Mult): estos se resuelven todos de la misma forma, evaluando
                    los casos en donde debería utilizar paréntesis y los que no (evitando los casos que están dentro de las listas). 
            
            La función necesitaParentesis (Izq o Der) nos permite evaluar en todos los casos de operadores binarios
            cuándo es necesario colocar paréntesis, dependiendo del operador que estemos evaluando, y si la asociatividad 
            a izquierda ya agrupa los valores correctamente (es decir, no necesitan paréntesis), o si las operaciones que siguen
            van al análisis derecho, tal que la asociatividad izquierda no las agrupa correctamente (precisan del análisis de uso
            del paréntesis).
-}
mostrar :: Expr -> String
mostrar = recrExpr fConst fRango fSuma fResta fMult fDiv
    where
        -- casos base - constante y rango => Se convierten a String
        fConst f                = show f
        fRango s e              = show s ++ "~" ++ show e
        -- casos recursivos - operadores binarios
        fSuma strIzq strDer exprIzq exprDer =
            maybeParen (necesitaParentesisIzq exprIzq) strIzq ++ " + " ++
            maybeParen (necesitaParentesisDer exprDer) strDer
            where
                necesitaParentesisIzq e = notElem (constructor e) [CEConst, CERango, CESuma]
                necesitaParentesisDer e = notElem (constructor e) [CEConst, CERango, CESuma]

        fResta strIzq strDer exprIzq exprDer =
            maybeParen (necesitaParentesisIzq exprIzq) strIzq ++ " - " ++
            maybeParen (necesitaParentesisDer exprDer) strDer
            where
                necesitaParentesisIzq e = notElem (constructor e) [CEConst, CERango, CESuma, CEResta]
                necesitaParentesisDer e = notElem (constructor e) [CEConst, CERango]

        fMult strIzq strDer exprIzq exprDer =
            maybeParen (necesitaParentesisIzq exprIzq) strIzq ++ " *" ++
            maybeParen (necesitaParentesisDer exprDer) strDer
            where
                necesitaParentesisIzq e = notElem (constructor e) [CEConst, CERango, CEMult]
                necesitaParentesisDer e = notElem (constructor e) [CEConst, CERango, CEMult]

        fDiv strIzq strDer exprIzq exprDer =
            maybeParen (necesitaParentesisIzq exprIzq) strIzq ++ " *" ++
            maybeParen (necesitaParentesisDer exprDer) strDer
            where
                necesitaParentesisIzq e = notElem (constructor e) [CEConst, CERango, CEDiv, CEMult]
                necesitaParentesisDer e = notElem (constructor e) [CEConst, CERango]


{- 
Los valores que están dentro de los [] en necesitaParentesisX son los constructores
que NO necesitan paréntesis en esa posición.
CEConst y CERango siempre están permitidos (nunca necesitan paréntesis)

Explicacion general de necesitaParentesisX aplicada en CEResta:
    Lado izquierdo [CESuma, CEResta]:   La asociatividad a izquierda ya las agrupa 
                                        correctamente => No necesitan paréntesis

    Lado derecho [CEConst, CERango]:    Las operaciones que siguen van al análisis derecho,
                                        la asociatividad izquierda no las agrupa correctamente
                                        => Necesitan parentesis
-}



data ConstructorExpr = CEConst | CERango | CESuma | CEResta | CEMult | CEDiv
  deriving (Show, Eq)

-- | Indica qué constructor fue usado para crear la expresión.
constructor :: Expr -> ConstructorExpr
constructor (Const _) = CEConst
constructor (Rango _ _) = CERango
constructor (Suma _ _) = CESuma
constructor (Resta _ _) = CEResta
constructor (Mult _ _) = CEMult
constructor (Div _ _) = CEDiv

-- | Agrega paréntesis antes y después del string si el Bool es True.
maybeParen :: Bool -> String -> String
maybeParen True s = "(" ++ s ++ ")"
maybeParen False s = s
