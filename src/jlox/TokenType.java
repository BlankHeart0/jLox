package jlox;

enum TokenType{
    LEFT_PAREN,RIGHT_PAREN, //( )
    LEFT_BRACE,RIGHT_BRACE, //{ }
    COMMA,DOT,SEMICOLON, //, . ;
    PLUS,MINUS,STAR,SLASH, //+ - * /

    BANG,BANG_EQUAL, //! !=
    EQUAL,EQUAL_EQUAL, //= ==
    GREATER,GREATER_EQUAL, //> >=
    LESS,LESS_EQUAL, //< <=

    //Literals
    IDENTIFIER,STRING,NUMBER,

    //Keywords
    IF,ELSE,FOR,WHILE,AND,OR,FALSE,TRUE,
    FUN,RETURN,PRINT,SUPER,
    VAR,NIL,EOF,
    CLASS,THIS

}

class Token{
    final TokenType type;
    final String lexeme;
    final Object literal;
    final int line;

    Token(TokenType type,String lexeme,Object literal,int line)
    {
        this.type=type;
        this.lexeme=lexeme;
        this.literal=literal;
        this.line=line;
    }

    public String toString()
    {
        return type+" "+lexeme+" "+literal;
    }

}