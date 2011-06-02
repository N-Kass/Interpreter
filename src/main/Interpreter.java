package main;

import scanner.Scanner;

public class Interpreter {
	public static void main(String[] args) {
		Scanner scanner = new Scanner("set(\"fact\" func([n] { /* If n is smaller than 2, return 1. Else, return n * fact(n - 1) */ ret(if(lt(n 2) func([] { ret(1) }) func([] { ret(mul(n sub(n 1))) }))) })) print(fact(20))");
		try {
			scanner.scan();
			
			System.out.println("Tokens: ");
			for(int i = 0; i < scanner.getTokens().size(); i++) {
				System.out.println("Type: '" + scanner.getTokens().get(i).getType().toString() + 
						"'. Value: '" + scanner.getTokens().get(i).getValue() + "'.");
			}
		} catch (SyntaxException e) {
			System.out.println(e);
		}
	}
}