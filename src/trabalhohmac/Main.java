package trabalhohmac;

public class Main {

	public static void main(String[] args) {

		String key = "cobol";
		String message = "Quero aprender essa linguagem";

		System.out.println(Utils.hmac(message, key));
		

	}

}
