import random as rand
import numpy as np

class MAX_SAT:
    """
    Representación de una fórmula válida para el problema MAX-SAT.

    Atributos:
        n     Número de variables en la fórmula.
        m     Número de cláusulas en la fórmula.
        F     Fórmula lógica.
    """

    def __init__(self, num_variables, num_clausulas):
        self.n = num_variables
        self.m = num_clausulas
        # Crea una matriz vacía de dimensiones m x n que representa la fórmula F
        self.F = np.zeros((self.m, self.n), dtype=int)
        # Rellena la matriz con valores aleatorios
        self.crear_ejemplar()

    def crear_ejemplar(self):

        """
        Actualiza los valores de la matriz F. Genera cláusulas aleatorias de 3 a 5 variables cada una.
        """

        i = 0
        for fila in self.F:

            # Repetimos hasta que no haya filas repetidas
            while True:

                # La variable c representa el número de variables en cada cláusula.
                # 3 <= |c_i| <= 5
                c = rand.randint(3,5)

                # Candidato a ser cláusula de la fila i
                tmp = np.zeros((n,), dtype=int)

                # ci son las posiciones de las variables que están en la cláusula
                ci = rand.sample(range(0, self.n), c)

                # Actualizar variables del candidato
                for val in ci:
                    r = rand.uniform(0,1)
                    if r <= 0.5:
                        tmp[val] = 1
                    else:
                        tmp[val] = -1

                # Revisa si la cláusula no existe en la fórmula
                if not any((self.F[:] == tmp).all(1)):
                    # Agrega la cláusula a la fórmula
                    self.F[i] = tmp
                    break

            i += 1

class Individuo:
    """
    Individuo de una población de un algoritmo genético.
    Representa una asignación de verdad para un conjunto de variables.

    Atributos:
        genes     Valores de verdad de las variables x_1 hasta x_n.
    """

    def __init__(self, ejemplar, genes=[]):
        self.ejemplar = ejemplar
        if genes == []:
            self.genes = np.zeros((ejemplar.n,), dtype=int)
            self.individuo_aleatorio()
        else:
            self.genes = genes
        self.fitness = self.fitness()

    def __eq__(self, other):

        """
        Devuelve True si dos individuos son iguales.
        Dos individuos son iguales si tienen los mismos genes o asignaciones de verdad.
        other           el otro individuo
        """

        return np.array_equal(self.genes, other.genes)


    def __lt__(self, other):

        """
        Devuelve True si la aptitud del un individuo es menor a la de otro.
        other           el otro individuo
        """

        return self.fitness < other.fitness

    def __le__(self, other):

        """
        Devuelve True si la aptitud del un individuo es menor o igual a la de otro.
        other           el otro individuo
        """

        return self.fitness <= other.fitness

    def __gt__(self, other):

        """
        Devuelve True si la aptitud del un individuo es mayor a la de otro.
        other           el otro individuo
        """

        return self.fitness > other.fitness

    def __ge__(self, other):

        """
        Devuelve True si la aptitud del un individuo es mayor o igual a la de otro.
        other           el otro individuo
        """

        return self.fitness >= other.fitness

    def individuo_aleatorio(self):

        """
        Genera una asignación de verdad aleatoria.
        La probabilidad de asignación del valor de verdad es de 0.5.
        """

        for i in range(0, self.genes.size - 1):
            r = rand.uniform(0,1)
            if r < 0.55:
                self.genes[i] = 0
            else:
                self.genes[i] = 1

    def fitness(self):

        """
        Función de evaluación para un individuo de la población.
        La aptitud de cada individuo estará determinada por el número total de
        cláusulas que satisface la asignación de verdad que codifica.
        """

        valor = 0

        for clausula in self.ejemplar.F:
            for j, variable in enumerate(clausula):
                if variable == 1:
                    if self.genes[j] == 1:
                        valor += 1
                        break
                if variable == -1:
                    if self.genes[j] == 0:
                        valor += 1
                        break

        return valor

class Poblacion:
    """
    Conjunto de Individuos.

    Atributos:
        individuos     Un arreglo de Individuos.
    """

    def __init__(self, tamanio, ejemplar):
        self.individuos = np.empty((tamanio,), dtype=Individuo)
        for i in range(0, self.individuos.size):
            self.individuos[i] = Individuo(ejemplar)
        # Los individuos siempre están ordenados ascendentemente de acuerdo a su aptitud
        self.individuos = np.sort(self.individuos)

    def total_fitness(self):

        """
        Devuelve la suma de las aptitudes de todos los individuos de la población
        """

        total = 0
        for individuo in self.individuos:
            total += individuo.fitness

        return total

    def seleccion(self):

        """
        Devuelve dos padres para la reproducción
        """

        ### Método de ruleta para seleccionar un padre de entre los individuos ###

        # Suma de las aptitudes de todos los individuos de la población
        suma_fitnesses = self.total_fitness()
        # Número aleatorio n tal que 0 <= n <= suma_fitnesses
        random_n = rand.uniform(0, suma_fitnesses)

        # Punto de inicio de la ruleta
        suma_parcial = random_n
        # Arreglo de individuos ordenado de manera descendiente por su aptitud
        individuos_ordenados_desc = np.flip(self.individuos)

        # Sumar aptitudes de los individuos y devolver al individuo que exceda
        # de la suma total
        for ind in individuos_ordenados_desc:
            suma_parcial += ind.fitness
            if suma_parcial > suma_fitnesses:
                return ind

        # Si el inicio de la ruleta es 0 llega aquí
        return individuos_ordenados_desc[-1]
        ########################################################################

    def cruce(self, padre1, padre2):
        """
        Cruza dos individuos y genero dos hijos.

        padre1          El primer individuo
        padre2          El segundo individuo
        """

        return self.partially_mapped_crossover(padre1, padre2)

    def partially_mapped_crossover(self, padre1, padre2):

        """
        Devuelve un cruce simple de dos puntos.

        padre1          El primer individuo
        padre2          El segundo individuo

        Nota:
        No tiene sentido implementar este cruce, ya que es equivalente al cruce
        de dos puntos para el problema de MAX-SAT. Los individuos solo están
        definidos por 0s y 1s, por lo que no existen 'mapeos'.
        """

        puntos = self.crear_puntos_cruce(padre1.genes)
        p1 = puntos[0]
        p2 = puntos[1]

        genes1 = np.zeros(padre2.genes.shape, dtype=int)
        hijo1 = self.cruce_dos_puntos(padre1, padre2, p1, p2, genes1)

        genes2 = np.zeros(padre1.genes.shape, dtype=int)
        hijo2 = self.cruce_dos_puntos(padre2, padre1, p1, p2, genes2)

        return (hijo1,hijo2)

    def crear_puntos_cruce(self, arreglo):

        """
        Genera aletoriamente dos puntos de cruce para un arreglo.

        arreglo         El arreglo del que queremos obtener los puntos
        """

        punto_cruce1 = rand.randint(1,len(arreglo) - 1)
        punto_cruce2 = rand.randint(1,len(arreglo) - 1)

        if punto_cruce1 > punto_cruce2:
            punto_cruce1, punto_cruce2 = punto_cruce2, punto_cruce1

        if punto_cruce1 == punto_cruce2:
            if punto_cruce1 > len(arreglo) / 2:
                punto_cruce1 = rand.randint(1, punto_cruce1 -1)
            else:
                punto_cruce2 = rand.randint(punto_cruce1 + 1, len(arreglo) - 1)

        return (punto_cruce1, punto_cruce2)


    def cruce_dos_puntos(self, padre1, padre2, p1, p2, genes):

        """
        Cruza dos individuos tomando en cuenta dos puntos de cruce y devuelve
        un hijo.
        padre1          El primer individuo
        padre2          El primer individuo
        p1              El índice del primer punto de cruce
        p2              El índice del segundo punto de cruce
        genes           El arreglo de cromosomas para el hijo
        """

        genes[0:p1] = padre1.genes[0:p1]
        genes[p1:p2] = padre2.genes[p1:p2]
        genes[p2:] = padre1.genes[p2:]
        return Individuo(padre1.ejemplar, genes)

    def order_crossover(self, padre1, padre2):

        """
        Nota:
        Situación similar con partially_mapped_crossover. Se omite la implemen-
        tación, ya que los cromosomas no son elementos que tengan que ser distintos
        como en la representación de TSP.
        """

        return partially_mapped_crossover(padre1, padre2)

    def mutacion(self, hijos):
        """
        Aplica una de dos mutaciones a los individuos
        hijos           individuos resultantes del proceso de reproducción
        """

        ind_mut = rand.uniform(0,1)

        if ind_mut <= 0.5:
            self.displacement_mutation(hijos)
        else:
            self.exchange_mutation(hijos)

    def displacement(self, individuo):

        """
        Auxiliar de displacement_mutation
        """

        tam_secuencia = 3
        inicio_sec = rand.randint(0, len(individuo.genes) - tam_secuencia)

        secuencia = np.copy(individuo.genes[inicio_sec:inicio_sec + tam_secuencia])

        indice_cambio = rand.randint(0, len(individuo.genes) - tam_secuencia)
        while indice_cambio >= inicio_sec and indice_cambio <= inicio_sec + tam_secuencia:
            indice_cambio = rand.randint(0, len(individuo.genes) - tam_secuencia)

        secuencia2 = np.copy(individuo.genes[indice_cambio:indice_cambio + tam_secuencia])

        individuo.genes[inicio_sec:inicio_sec + tam_secuencia] = secuencia2
        individuo.genes[indice_cambio:indice_cambio + tam_secuencia] = secuencia


    def displacement_mutation(self, hijos):

        """
        Intercambia dos secuencias de valores en los individuos
        hijos           individuos resultantes del proceso de reproducción
        """

        hijo1 = hijos[0]
        hijo2 = hijos[1]

        self.displacement(hijo1)
        self.displacement(hijo2)

    def exchange(self, individuo):
        """
        Auxiliar de exchange_mutation
        """

        i1 = rand.randint(0, len(individuo.genes) - 1)
        i2 = rand.randint(0, len(individuo.genes) - 1)
        while i1 == i2:
            i2 = rand.randint(0, len(individuo.genes) - 1)

        individuo.genes[i1], individuo.genes[i2] = individuo.genes[i2], individuo.genes[i1]


    def exchange_mutation(self, hijos):
        """
        Intercambia dos valores aleatorios de los individuos
        hijos           individuos resultantes del proceso de reproducción
        """

        hijo1 = hijos[0]
        hijo2 = hijos[1]

        self.exchange(hijo1)
        self.exchange(hijo1)

    def reemplazo(self, hijos):
        """
        Reemplaza los individuos de menor aptitud en la poblacion por los hijos.
        hijos           individuos resultantes del proceso de reproducción
        """

        self.individuos[0] = hijos[0]
        self.individuos[1] = hijos[1]
        self.individuos = np.sort(self.individuos)


if __name__ == "__main__":

    # Generar aletoriamente el número de variables
    # 100 <= n
    n = rand.randint(100, 10000)

    # Generar aletoriamente el número de cláusulas
    # 50 <= m <= 60
    m = rand.randint(50,60)

    num_iteraciones = 10000
    i = 0
    total_fitness = 0

    ejemplar = MAX_SAT(n, m)

    print("Numero de variables:", n)
    print("Número de cláusulas:", m)
    poblacion = Poblacion(8, ejemplar)

    print("Mejor aptitud inicial:", poblacion.individuos[-1].fitness)

    mejor_aptitud = poblacion.individuos[-1].fitness
    intentos_sin_mejora = 0
    generaciones = 0

    while i < num_iteraciones:

        if intentos_sin_mejora == 1000:
            break

        padre1 = poblacion.seleccion()
        padre2 = poblacion.seleccion()
        while padre1 == padre2:
            padre2 = poblacion.seleccion()

        hijos = poblacion.cruce(padre1, padre2)

        indice_mutacion = rand.uniform(0,1)
        if indice_mutacion <= 0.2:
            poblacion.mutacion(hijos)

        poblacion.reemplazo(hijos)
        i += 1

        generaciones += 1

        mejor = poblacion.individuos[-1]
        if mejor.fitness > mejor_aptitud:
            mejor_aptitud = mejor.fitness
            intentos_sin_mejora = 0
        else:
            intentos_sin_mejora += 1

    print()
    print("Total de generaciones de individuos:", generaciones)
    print("Mejor aptitud final:",poblacion.individuos[-1].fitness)
