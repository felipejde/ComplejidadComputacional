import java.util.Random;

public class Programa2{
	public static void main(String[] args) {

		Random rnd = new Random();

		int tam = rnd.nextInt(5) + 5; //numero aleatorio entre 0 y 10 

		Cubierta ejemplar = new Cubierta(tam);

		ejemplar.mostrar();
	}
}