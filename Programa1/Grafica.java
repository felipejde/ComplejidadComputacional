import java.util.*;

/* El propósito de esta clase es representar gráficas no dirigídas */
public class Grafica {

  private List<Edge> edges;
  private List<Vertex> vertices;

  /* Constructor de gráficas. */
  public Grafica(List<Integer> vList) {
    vertices = new LinkedList<>();
    for(Integer v: vList) {
      vertices.add(new Vertex(v));
    }
    edges = new LinkedList<>();
  }

  /* Clase para representar aristas */
  private class Edge {

    public Vertex u;
    public Vertex v;

    /* Constructor de aristas */
    public Edge(int u, int v) {
      this.u = new Vertex(u);
      this.v = new Vertex(v);
    }

    /*
    * Verifica si dos aristas son iguales
    */
    @Override
    public boolean equals(Object other){
      if (other == null) return false;
      if (other == this) return true;
      if (!(other instanceof Edge)) return false;
      Edge e = (Edge)other;
      if(u.element == e.u.element && v.element == e.v.element)
        return true;
      if(u.element == e.v.element && u.element == e.u.element)
        return true;
      return false;
    }
  }

  /* Clase para representar vértices de una gráfica */
  private class Vertex {

    public int element;

    /* Constructor de vértices */
    public Vertex(int element) {
      this.element = element;
    }
  }

  /**
  * Verifica si una gráfica está contenida en otra
  * @param g la gráfica a verificar si es subgráfica
  * @return la verificación
  */
  // Aquí se hace todo el trabajo de la fase de verificación
  public boolean contains(Grafica g) {
    for(Vertex v : g.vertices) {
      if(!vertices.contains(v))
        return false;
    }
    for(Edge e : g.edges) {
      if(!edges.contains(e))
        return false;
    }
    return true;
  }

  /**
  * Agrega una arista a la gráfica
  * @param u el elemento del primer vértice
  * @param v el elemento del segundo vértice
  */
  public void addEdge(int u, int v) {
    edges.add(new Edge(u, v));
  }

  /**
  * Verifica si dados dos vértices de la gráfica, existe una arista entre ellos
  * @param u el elemento del primer vértice
  * @param v el elemento del segundo vértice
  * @return si existe o no la arista
  */
  public boolean existsEdge(int u, int v) {
    Edge e = new Edge(u, v);
    Edge e2 = new Edge(v, u);
    if(edges.contains(e) || edges.contains(e2)) {
      return true;
    }
    return false;
  }

  @Override
  public String toString() {

    String verticesSet = "V = {";
    String edgesSet = "E = {";

    StringJoiner joiner = new StringJoiner(",");
    for (Vertex v : vertices) {
      joiner.add("" + v.element);
    }
    verticesSet += joiner.toString() + "}\n";

    joiner = new StringJoiner(",");
    for (Edge e : edges) {
      joiner.add("(" + e.u.element + "," + e.v.element + ")");
    }
    edgesSet += joiner.toString() + "}";

    return verticesSet + edgesSet;
  }
}
