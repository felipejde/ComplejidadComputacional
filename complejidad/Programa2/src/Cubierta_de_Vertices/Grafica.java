import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para gráficas. Una gráfica es un conjunto de vértices y aristas, tales
 * que las aristas son un subconjunto del producto cruz de los vértices.
 */
public class Grafica<T> implements Coleccion<T> {

    /* Clase privada para iteradores de gráficas. */
    private class Iterador implements Iterator<T> {

        /* Iterador auxiliar. */
        private Iterator<Grafica<T>.Vertice> iterador;

        /* Construye un nuevo iterador, auxiliándose de la lista de vértices. */
        public Iterador(Grafica<T> grafica) {
            iterador = grafica.vertices.iterator();
        }

        /* Nos dice si hay un siguiente elemento. */
        public boolean hasNext() {
            return iterador.hasNext();
        }

        /* Regresa el siguiente elemento. */
        public T next() {
            return iterador.next().elemento;
        }

        /* No lo implementamos: siempre lanza una excepción. */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* Vertices para gráficas; implementan la interfaz ComparableIndexable y
     * VerticeGrafica */
    private class Vertice implements VerticeGrafica<T> {

        /* Iterador para las vecinos del vértice. */
        private class IteradorVecinos implements Iterator<VerticeGrafica<T>> {

            /* Iterador auxiliar. */
            private Iterator<Grafica<T>.Vertice> iterador;

            /* Construye un nuevo iterador, auxiliándose de la lista de
             * vecinos. */
            public IteradorVecinos(Iterator<Grafica<T>.Vertice> iterador) {
                this.iterador = iterador;
            }

            /* Nos dice si hay un siguiente vecino. */
            public boolean hasNext() {
                return iterador.hasNext();
            }

            /* Regresa el siguiente vecino. La audición es inevitable. */
            public VerticeGrafica<T> next() {
                return (VerticeGrafica<T>) iterador.next();
            }

            /* No lo implementamos: siempre lanza una excepción. */
            public void remove() {
                throw new UnsupportedOperationException();
            }
        }

        /* El elemento del vértice. */
        public T elemento;
        /* El color del vértice. */
        public Color color;
        /* La distancia del vértice. */
        public double distancia;
        /* El índice del vértice. */
        public int indice;
        /* Lista de vecinos. */
        public Lista<Grafica<T>.Vertice> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            this.elemento=elemento;
            color = Color.NINGUNO;
            vecinos = new Lista<Grafica<T>.Vertice>();
        }

        /* Regresa el elemento del vértice. */
        public T getElemento() {
            return elemento;
        }

        /* Regresa el grado del vértice. */
        public int getGrado() {
            return vecinos.getLongitud();
        }

        /* Regresa el color del vértice. */
        public Color getColor() {
            return color;
        }

        /* Define el color del vértice. */
        public void setColor(Color color) {
            this.color=color;
        }

        /* Regresa un iterador para los vecinos. */
        public Iterator<VerticeGrafica<T>> iterator() {
            return new IteradorVecinos(vecinos.iterator());
        }

        /* Define el índice del vértice. */
        public void setIndice(int indice) {
            this.indice=indice;
        }

        /* Regresa el índice del vértice. */
        public int getIndice() {
            return indice;
        }
    }

    /* Vértices. */
    private Lista<Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        vertices = new Lista<Vertice>();
        aristas = 0;
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    public int getElementos() {
        return vertices.getLongitud();
    }

    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        return aristas;
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento ya había sido agregado a
     *         la gráfica.
     */
    @Override public void agrega(T elemento) {
        if(contiene(elemento))
            throw new IllegalArgumentException();
        vertices.agregaFinal(new Vertice(elemento));
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b) {
        if(a.equals(b))
            throw new IllegalArgumentException();
        if(sonVecinos(a,b))
            throw new IllegalArgumentException();
        if(contiene(a)&&contiene(b)){
            Vertice v = busca(a);
            Vertice u = busca(b);
            v.vecinos.agregaFinal(u);
            u.vecinos.agregaFinal(v);
            aristas++;
            return;
        }
        throw new NoSuchElementException();
    }

    public Vertice busca(T a){
        if(a==null)
            return null;
        for (Vertice v: vertices)
            if(v.elemento.equals(a))
                return v;
        return null;
    }

    /**
     * Desconecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica y estar conectados entre ellos.
     * @param a el primer elemento a desconectar.
     * @param b el segundo elemento a desconectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public void desconecta(T a, T b) {
        Vertice v = busca(a);
        Vertice u = busca(b);
        if(contiene(a)&&contiene(b)){
            if(v.vecinos.contiene(u)){
                v.vecinos.elimina(u);
                u.vecinos.elimina(v);
                aristas--;
                return;
            }
            throw new IllegalArgumentException();
        }
        throw new NoSuchElementException();
    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <tt>true</tt> si el elemento está contenido en la gráfica,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        for(Vertice v : vertices)
            if(v.elemento.equals(elemento))
                return true;
        return false;
    }

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que estar contenido
     * en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está contenido en la
     *         gráfica.
     */
    @Override public void elimina(T elemento) {
        Vertice v = busca(elemento);
        if(v==null)
            throw new NoSuchElementException();
        vertices.elimina(v);
        for(Vertice u : v.vecinos){
            u.vecinos.elimina(v);
            aristas--;
        }
    }

    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los elementos
     * deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <tt>true</tt> si a y b son vecinos, <tt>false</tt> en otro caso.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public boolean sonVecinos(T a, T b) {
        if(!contiene(a)||!contiene(b))
            throw new NoSuchElementException();
        Vertice v = busca(a);
        Vertice u = busca(b);
        if(v.vecinos.contiene(u))
            return true;
        return false;
    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {
        Vertice v = busca(elemento);
        if(v==null)
            throw new NoSuchElementException();
        return v;
    }

    /**
     * Metodo especificamente agregado para checar el color de los
     * Vertices, COMPLEJIDAD COMPUTACIONAL 2018-1.
     */
    public boolean revisaTodoVertice() {
        for ( Vertice v : vertices)
            if(v.getColor() != Color.ROJO)
                return false;
        return true;
    }

    /**
     * Regresa la lista de adyacencias de la grafica
     * La grafica implementada no es dirigida, por lo tanto
     * la arista (u,v) aparecera dos veces como (v,u)
     */
    public Lista<String> listaDeAdyacencias(){
        Lista<String> ad = new Lista<String>();
        for(Vertice v:vertices){
            for(Vertice u : v.vecinos){
                ad.agrega("("+v.getElemento()+","+u.getElemento()+")");
            }
        }

        return ad;
    }

    /**
     * Regresa la lista de los elementos
     * de la lista
     */
    public String toStringElementos(){
        Lista<String> el = new Lista<String>();
        for(Vertice v:vertices){
            el.agrega(""+v.getElemento());
        }

        return el.toString();
    }

    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador(this);
    }
}
