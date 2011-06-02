package scanner;

import java.util.ArrayList;
import main.SyntaxException;

public class Scanner {
	// Public for testing
	public String code;
	private int pointer;
	public ArrayList<Token> tokens;
	
	public Scanner(String code) {
		this.code = code;
		this.tokens = new ArrayList<Token>();
	}
	
	/**
	 * Scans string for tokens
	 * 
	 * Scans for numbers (1.5, 1, 0, 0.5, .5, etc.), strings ("Hello!"), 
	 * code literals ({ somecode() }), array/list start and end ([ and ])
	 * removes comments
	 * 
	 * @return ArrayList List of tokens
	 * @throws SyntaxException
	 */
	public void scan() throws SyntaxException {
		// Loop over all code and extract tokens
		char c;
		for(this.pointer = 0; this.pointer < this.code.length()-1; this.pointer++) {
			this.skipWhitespace();
			
			c = this.code.charAt(this.pointer);
			if((c >= '0') && (c <= '9')) {
				this.parseNumber();
			} else
			if(c == '"') {
				this.parseString();
			} else
			if(c == '{') {
				this.parseCode();
			} else
			if(c == '[') {
				this.tokens.add(new Token(TokenType.OpenSquareBracket));
			} else
			if(c == ']') {
				this.tokens.add(new Token(TokenType.CloseSquareBracket));
			}
			if((c == '/') && this.code.charAt(this.pointer+1) == '*') {
				this.skipComment();
			}
			else {
				this.parseLiteral();
			}
		}
	}
	
	private void skipComment() {
		
	}

	/**
	 * Parse functions and variables
	 */
	private void parseLiteral() {
		
	}
	
	private void parseCode() throws SyntaxException {
		if(this.code.charAt(this.pointer) != '{') {
			return;
		}
		this.pointer++;
		
		String code = "";
		/*
		 * Find matching '}' tag.
		 */
		int startPos = this.pointer;
		int depth = 1;
		char c;
		while((this.pointer < this.code.length()) && 
			  (depth > 0)) {
			c = this.code.charAt(this.pointer);
			if(c == '}') {
				depth--;
			} else
			if(c == '{') {
				depth++;
			}
			
			if(depth > 0) {
				code += this.code.charAt(this.pointer);
				this.pointer++;
			}
		}
		
		// If no matching '}' found.
		if(this.pointer == this.code.length()) {
			throw new SyntaxException(startPos, "Code literal not closed.");
		}
		
		this.tokens.add(new Token(TokenType.Code, code));
	}

	private void parseString() throws SyntaxException {
		if(this.code.charAt(this.pointer) != '"') {
			return;
		}
		this.pointer++;
		
		String str = "";
		/*
		 * While '"' not found keep adding. Stop if reached
		 * end of string.
		 */
		int startPos = this.pointer;
		while((this.pointer < this.code.length()) && 
			  ((this.code.charAt(this.pointer) != '"'))) {
			str += this.code.charAt(this.pointer);
			
			this.pointer++;
		}
		
		// If no matching '"' found.
		if(this.pointer == this.code.length()) {
			throw new SyntaxException(startPos, "String literal not closed.");
		}
		
		this.tokens.add(new Token(TokenType.String, str));
	}

	private void parseNumber() {
		
	}

	/**
	 * Moves pointer to skip whitespace
	 */
	private void skipWhitespace() {
		while(Character.isWhitespace(this.code.charAt(this.pointer))) {
			this.pointer++;
		}
	}
}