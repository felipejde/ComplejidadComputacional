import java.util.*;

/* Clase para representar posibles soluciones al problema del clan */
public class Clan {

  private Grafica original;
  private Grafica clan;

  public Clan(Grafica g, int verticesNum, int k) {

    original = g;

    // Fase de adivinación
    List<Integer> possibleVertices = new LinkedList<>();
    Random rng = new Random();
    while(possibleVertices.size() < k) {
      int n = 1 + rng.nextInt(verticesNum);
      if(!possibleVertices.contains(n)) {
        possibleVertices.add(n);
      }
    }
    clan = new Grafica(possibleVertices);
    for (int i = 0; i < k; i++) {
      for (int j = i + 1;j < k ; j++) {
          clan.addEdge(possibleVertices.get(i), possibleVertices.get(j));
      }
    }

    System.out.println(" El Candidato es el siguiente \n" + clan.toString());
  }

  /**
  * Determina si la solución propuesta es en realidad solución
  * @return si la solución es correcta
  */
  public boolean isSolution() {
    // Fase de verificación
    return original.contains(clan);
  }
}
