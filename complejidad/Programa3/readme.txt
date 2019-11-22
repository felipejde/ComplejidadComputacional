Programa 3
Complejidad Computacional
Juan Carlos Montero Santiago 
415122599

La representacion de la gráfica se hizo por medio de su matriz de adyacencias/peso
sin embargo, al estar completando la información en la matriz ocurrió una confunción 
y nos dimos cuenta muy tarde como para corregirlo :( la matriz esta completa, pero algunas
de las distancias entre las ciudades no es la correcta. Aun asi, creo que basta para mostrar que mi algoritmo 
genetico esta llevando a cabo la funcion de manera correcta y puede funcionar con cualquier matriz.

INSTRUCCIONES:
1.- Entrar desde la linea de comandos a la carpeta ./src/
2.- Compilar todos los archivos ejecutando javac *.java
3.- Ejecutar la clase principal Programa3 usando el comando
    java Programa3
4.- En cada ejecucion se lleva acabo el proceso de "evolucion" (Seleccion->Cruce/Mutacion->Reemplazo) 
    sobre una poblacion de 10000 individuos(Tours); en pantalla se muestra el mejor individuo de cada generación
    (puede que este se repita, es decir, que el mejor individuo de la generacion n sea el mismo
     que el de la generacion n-1; sin embargo, aunque esto suceda, el mejor individuo continua 
     tendiendo hacia un tour de distancia mas corta, como puede ser observado continuando la ejecucion del
     programa). Al finalizar se muestra el mejor individuo encontrado, su distancia (el valor del tour) y en cuantas
    generaciones fue obtenido.

NOTA:
Recomiendo que para que la salida del programa se pueda apreciar mejor, se use la terminal en pantalla completa y
el tamaño de la letra sea reducido, de manera que el tour de tamaño 51 sea mostrado claramente en una sola linea, junto con 
su distancia.
