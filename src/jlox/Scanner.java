package jlox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jlox.TokenType.*;

class Scanner {
    private final String source;
    private final List<Token>tokens=new ArrayList<>();

    private int start=0;
    private int current=0;
    private int line=1;

    private static final Map<String,TokenType>keywords;

    static {
        keywords=new HashMap<>();

        keywords.put("if",IF);
        keywords.put("else",ELSE);
        keywords.put("for",FOR);
        keywords.put("while",WHILE);

        keywords.put("and",AND);
        keywords.put("or",OR);
        keywords.put("false",FALSE);
        keywords.put("true",TRUE);

        keywords.put("fun",FUN);
        keywords.put("return",RETURN);
        keywords.put("print",PRINT);
        keywords.put("super",SUPER);

        keywords.put("var",VAR);
        keywords.put("nil",NIL);
        keywords.put("class",CLASS);
        keywords.put("this",THIS);
    }

    Scanner(String source){
        this.source=source;
    }

    //scan all tokens ,store them in tokens(list)
    List<Token>scanTokens(){
        while(!isAtEnd())
        {
            start = current;
            scanToken();
        }

        tokens.add(new Token(EOF,"",null,line));
        return tokens;
    }

    //scan one token ,it will be called by scanTokens
    private void scanToken(){
        char c=advance();
        switch(c){
            //one character
            case '(':addToken(LEFT_PAREN);break;
            case ')':addToken(RIGHT_PAREN);break;
            case '{':addToken(LEFT_BRACE);break;
            case '}':addToken(RIGHT_BRACE);break;
            case ',':addToken(COMMA);break;
            case '.':addToken(DOT);break;
            case '-':addToken(MINUS);break;
            case '+':addToken(PLUS);break;
            case ';':addToken(SEMICOLON);break;
            case '*':addToken(STAR);break;

            //two characters
            case '!':addToken(match('=')?BANG_EQUAL:BANG);break;
            case '=':addToken(match('=')?EQUAL_EQUAL:EQUAL);break;
            case '<':addToken(match('=')?LESS_EQUAL:LESS);break;
            case '>':addToken(match('=')?GREATER_EQUAL:GREATER);break;

            //annotation
            case '/':
                if(match('/')){
                    while (peek()!='\n'&&!isAtEnd())advance();
                }else {
                    addToken(SLASH);
                }
                break;

            //no meaning character
            case ' ':
            case '\r':
            case '\t':break;
            case '\n':line++;break;

            //string
            case '"':string();break;


            default:
                //number
                if(isDigit(c)){
                    number();
                //identifier and keyword
                }else if(isAlpha(c)) {
                    identifier();
                }else{
                    Lox.error(line, "Unexpected character.");
                }
                break;
        }
    }

    private boolean isDigit(char c){
        return c>='0'&&c<='9';
    }
    private boolean isAlpha(char c){
        return (c>='a'&&c<='z')||(c>='A'&&c<='Z')||c=='_';
    }
    private boolean isAlphaNumber(char c){
        return isAlpha(c)||isDigit(c);
    }

    private boolean isAtEnd(){
        return current>=source.length();
    }

    //let the current pointer go one step
    private char advance(){
        current++;
        return source.charAt(current-1);
    }

    //lookahead the next character
    private boolean match(char expected){
        if(isAtEnd())return false;
        if(source.charAt(current)!=expected)return false;
        current++;
        return true;
    }

    private char peek(){
        if(isAtEnd())return '\0';
        return source.charAt(current);
    }

    private char peekNext(){
        if(current+1>=source.length())return '\0';
        return source.charAt(current+1);
    }

    private void string(){
        while (peek()!='"'&&!isAtEnd()){
            if(peek()=='\n')line++;
            advance();
        }

        if(isAtEnd()){
            Lox.error(line,"Unterminated string.");
            return;
        }

        advance();
        String value=source.substring(start+1,current-1);
        addToken(STRING,value);
    }

    private void number(){
        while (isDigit(peek()))advance();
        if(peek()=='.'&&isDigit(peekNext())){
            advance();
            while(isDigit(peek()))advance();
        }
        addToken(NUMBER,Double.parseDouble(source.substring(start,current)));
    }

    private void identifier(){
        while (isAlphaNumber(peek()))advance();
        String text=source.substring(start,current);
        TokenType type=keywords.get(text);
        if(type==null)type=IDENTIFIER;
        addToken(type);
    }

    private void addToken(TokenType type,Object literal){
        String text=source.substring(start,current);
        tokens.add(new Token(type,text,literal,line));
    }

    private void addToken(TokenType type){
        addToken(type,null);
    }
}
