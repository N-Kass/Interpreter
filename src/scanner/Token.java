package scanner;

public class Token {
	TokenType type;
	Object value;
	
	public Token(TokenType type, Object value) {
		this.type = type;
		this.value = value;
	}
	
	public Token(TokenType type) {
		this(type, null);
	}

	public TokenType getType() {
		return this.type;
	}

	public Object getValue() {
		return this.value;
	}
}