package scanner;

import java.util.ArrayList;
import main.SyntaxException;

public class Scanner {
	private String code;
	private int pointer;
	private ArrayList<Token> tokens;

	public Scanner(String code) {
		this.code = code;
		this.tokens = new ArrayList<Token>();
	}

	public ArrayList<Token> getTokens() {
		return this.tokens;
	}

	/**
	 * Scans string for tokens
	 * 
	 * Scans for numbers (1.5, 1, 0, 0.5, .5, etc.), strings ("Hello!"), code
	 * literals ({ somecode() }), array/list start and end ([ and ]) removes
	 * comments
	 * 
	 * @return ArrayList List of tokens
	 * @throws SyntaxException
	 */
	public void scan() throws SyntaxException {
		// Loop over all code and extract tokens
		char c;
		for(this.pointer = 0; this.pointer < this.code.length(); this.pointer++) {
			this.skipWhitespace();
			
			c = this.code.charAt(this.pointer);
			if(Character.isDigit(c) || c == '-') {
				this.parseFloat();
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
			} else
			if(c == '(') {
				this.tokens.add(new Token(TokenType.OpenParen));
			} else
			if(c == ')') {
				this.tokens.add(new Token(TokenType.CloseParen));
			} else
			if(c == '/')
			{
				if (this.code.charAt(this.pointer + 1) == '*') {
					this.skipComment(1);
				}
				else if (this.code.charAt(this.pointer + 1) == '/') {
					this.skipComment(0);
				}
			}
			else {
				this.parseLiteral();
			}
		}
	}

	private void skipComment(int type) throws SyntaxException {
		// If no comment or invalid argument, return
		if ((this.code.charAt(this.pointer) != '/')
				|| ((this.code.charAt(this.pointer + 1) != '*') 
				&& (this.code.charAt(this.pointer + 1) != '/'))
				|| ((type != 0) && (type != 1))) {
			return;
		}
		
		int pos = this.code.indexOf(type == 0 ? "*/" : "\n", this.pointer + 2);
		if (pos == -1) {
			throw new SyntaxException(this.pointer, "Unclosed comment.");
		}
		this.pointer = pos + 1;
	}

	/**
	 * Parse functions and variables
	 */
	private void parseLiteral() {
		String lit = "";
		while (Character.isLetterOrDigit(this.code.charAt(this.pointer))) {
			lit += this.code.charAt(this.pointer);
			this.pointer++;
		}
		this.pointer--;

		this.tokens.add(new Token(TokenType.Literal, lit));
	}

	private void parseCode() throws SyntaxException {
		if (this.code.charAt(this.pointer) != '{') {
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
		while ((this.pointer < this.code.length()) && (depth > 0)) {
			c = this.code.charAt(this.pointer);
			if (c == '}') {
				depth--;
			} else if (c == '{') {
				depth++;
			}

			if (depth > 0) {
				code += this.code.charAt(this.pointer);
				this.pointer++;
			}
		}

		// If no matching '}' found.
		if (this.pointer == this.code.length()) {
			throw new SyntaxException(startPos, "Code literal not closed.");
		}

		this.tokens.add(new Token(TokenType.Code, code));
	}

	/**
	 * Parse string literal
	 * 
	 * @throws SyntaxException
	 *             If no closing '"' is found.
	 */
	private void parseString() throws SyntaxException {
		if (this.code.charAt(this.pointer) != '"') {
			return;
		}
		this.pointer++;

		String str = "";
		/*
		 * While '"' not found keep adding. Stop if reached end of string.
		 */
		int startPos = this.pointer;
		while ((this.pointer < this.code.length())
				&& ((this.code.charAt(this.pointer) != '"'))) {
			str += this.code.charAt(this.pointer);

			this.pointer++;
		}

		// If no matching '"' found.
		if (this.pointer == this.code.length()) {
			throw new SyntaxException(startPos, "String literal not closed.");
		}

		this.tokens.add(new Token(TokenType.String, str));
	}

	private void parseFloat() {
		if (!Character.isDigit(this.code.charAt(this.pointer))
				&& this.code.charAt(this.pointer) != '-') {
			return;
		}

		String number = "";

		if (this.code.charAt(this.pointer) == '-') {
			number += "-";
			this.pointer++;
		}

		while (Character.isDigit(this.code.charAt(this.pointer))) {
			number += this.code.charAt(this.pointer);
			this.pointer++;
		}

		if (this.code.charAt(this.pointer) == '.') {
			this.pointer++;
			number += ".";
			while (Character.isDigit(this.code.charAt(this.pointer))) {
				number += this.code.charAt(this.pointer);
				this.pointer++;
			}
		}
		this.pointer--;

		try {
			float fNumber = Float.parseFloat(number);
			this.tokens.add(new Token(TokenType.Number, fNumber));
		} catch (NumberFormatException e) {
		}
	}

	/**
	 * Moves pointer to skip whitespace
	 */
	private void skipWhitespace() {
		while (Character.isWhitespace(this.code.charAt(this.pointer))) {
			this.pointer++;
		}
	}
}