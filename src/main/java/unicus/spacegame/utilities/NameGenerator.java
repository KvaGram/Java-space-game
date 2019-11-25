package unicus.spacegame.utilities;

import java.util.Random;

public class NameGenerator {
	static String vowels = "aeiouy";
	static String consonants = "bcdfghjklmnprstvwxz"; //Q is fake and gay so we skip it
	public static void main(String[] args) {
		
		//Short Urbit pattern
		System.out.println(makeWord("CVCCVC"));
		//Long Urbit pattern
		System.out.println(makeWord("CVCCVC-CVCCVC"));
		//Apostrophe pattern
		System.out.println(makeWord("C'CVVCC"));
		//Lars Erik pattern
		System.out.println(makeWord("CVCC VCVC"));
		
	}
	public static char getCons(Random r) {
		return (consonants.charAt(r.nextInt(consonants.length())));
	}
	public static char getVow(Random r) {
		return (vowels.charAt(r.nextInt(vowels.length())));
	}
	public static String makeWord(String pattern){
		return makeWord(pattern, new Random());
	}
	public static String makeWord(String pattern, long seed){
		return makeWord(pattern, new Random(seed));
	}
	public static String makeWord(String pattern, Random r) {
		char[] word = new char[pattern.length()];
		for (int i=0; i<pattern.length(); i++) {
			char j = pattern.charAt(i);
			if (j == 'C') {word[i] = getCons(r);}
			else if (j == 'V') {word[i] = getVow(r);}
			else {word[i] = pattern.charAt(i);}
		}
		if (pattern.length() > 0) {word[0] = Character.toUpperCase(word[0]);}
		String result = new String(word);
		return result;
	}
}