import java.util.*;

public class Contenedor {

  ArrayList<Float> objetos;

  /**
  * Crea un nuevo contenedor vacio
  */
  public Contenedor() {
    objetos = new ArrayList<>();
  }

  /**
  * Agrega un nuevo objeto al contenedor
  * @param w el peso del objeto
  */
  public void agregaObjeto(float w) {
    objetos.add(w);
  }

  /**
  * Obtiene la suma de pesos de todos los objetos dentro del contenedor
  * @return el peso total del contenedor
  */
  public float obtenerPesoTotal() {
    float suma = 0;
    for(float w : objetos)
      suma += w;
    return suma;
  }

  @Override
  public String toString(){
    return objetos.toString();
  }
}
