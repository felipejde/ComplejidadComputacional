import java.util.Random;

public class Programa1{
	public static void main(String[] args) {

		Random rnd = new Random();

		int tam = rnd.nextInt(5) + 5; //numero aleatorio entre 0 y 10
		int k = rnd.nextInt(tam-1) + 1; //numero aleatorio entre 1 y tam
		boolean prueba = false; //Probar el algoritmo no deterministico forzando la parte adivinadora a generar un ejemplar en particular

		Cubierta ejemplar = new Cubierta(k,tam,prueba);

		ejemplar.mostrar(prueba);
	}
}