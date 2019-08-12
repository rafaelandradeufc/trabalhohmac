package trabalhohmac;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {

	private static MessageDigest md;

	static {
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
	}

	private static String convertStringToBinary(String m) {

		String aux = "", result_final = "";

		for (int x = 0; x < m.length(); x++) {

			aux = Integer.toBinaryString(m.charAt(x));
			int length = aux.length();

			if (aux.length() < 8) {
				for (int j = 0; j < 8 - length; j++) {
					aux = "0" + aux;
				}
			}
			result_final += aux;

		}

		return result_final;
	}

	private static String xorIpadOrOpad(String key, int bits_amount, String pad) {

		String aux = "";
		String aux_key = "", aux_ipad = "", key_final = "", ipad_final = "";
		String xorIpadToKey = "";

		for (int i = 0; i < bits_amount / 8; i++) {
			aux += pad;
		}

		for (int i = 0; i < pad.length(); i++) {

			aux_ipad = aux.substring(i, i + 8);
			aux_key = convertStringToBinary(key).substring(i, i + 8);

			ipad_final += (char) Integer.parseInt(aux_ipad, 2);
			key_final += (char) Integer.parseInt(aux_key, 2);

		}

		xorIpadToKey = xor(ipad_final, key_final);

		return xorIpadToKey;
	}

	private static String xor(String m1, String m2) {

		String message_final = "";
		String aux_1 = "", aux_2 = "", aux = "";

		for (int i = 0; i < m1.length(); i++) {
			aux_1 = Integer.toBinaryString(m1.charAt(i));
			aux_2 = Integer.toBinaryString(m2.charAt(i));

			int lenght_1 = aux_1.length();
			int lenght_2 = aux_2.length();

			if (aux_1.length() < 8) {
				for (int j = 0; j < 8 - lenght_1; j++) {
					aux_1 = "0" + aux_1;
				}
			}

			if (aux_2.length() < 8) {
				for (int j = 0; j < 8 - lenght_2; j++) {
					aux_2 = "0" + aux_2;
				}
			}

			aux = "";

			for (int k = 0; k < 8; k++) {
				aux += aux_1.charAt(k) ^ aux_2.charAt(k);
			}

			message_final += (char) Integer.parseInt(aux, 2);
		}

		return message_final;

	}

	private static char[] hexCodes(byte[] text) {
		char[] hexOutput = new char[text.length * 2];
		String hexString;
		for (int i = 0; i < text.length; i++) {
			hexString = "00" + Integer.toHexString(text[i]);
			hexString.toUpperCase().getChars(hexString.length() - 2, hexString.length(), hexOutput, i * 2);
		}
		return hexOutput;
	}

	public static String hashMD5(String message) {
		if (md != null) {
			return new String(hexCodes(md.digest(message.getBytes())));
		}
		return null;
	}

	public static String hmac(String message, String key) {

		String result_ipad = "";
		String result_opad = "";
		String ipad = "00110110";
		String opad = "01011100";
		String result_function_hash = "";
		String concat_keyxoripad_with_msg = "";
		String concat_keyxoropad_with_result_function_hash = "";
		String result_final = "";
		int bits_amount = 128;

		result_ipad = xorIpadOrOpad(key, bits_amount, ipad);

		concat_keyxoripad_with_msg = result_ipad + message;

		result_function_hash = hashMD5(concat_keyxoripad_with_msg);

		result_opad = xorIpadOrOpad(key, bits_amount, opad);

		concat_keyxoropad_with_result_function_hash = result_opad + result_function_hash;

		result_final = hashMD5(concat_keyxoropad_with_result_function_hash);

		return result_final;

	}

}
