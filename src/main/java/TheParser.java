import java.util.Vector;

public class TheParser {
	
	private Vector<TheToken> tokens;
	private int currentToken;
	
	public TheParser(Vector<TheToken> tokens) {
		this.tokens = tokens;
		currentToken = 0;
	}
	
	public void run() {
		RULE_PROGRAM();
	}
	
	private void RULE_PROGRAM() {
		System.out.println("- RULE_PROGRAM");

        if(!tokens.get(currentToken).getType().equals("KEYWORD"))
            error(1);

        
        if(!tokens.get(currentToken).getValue().equals("class"))
            error(1);
        System.out.println("- CLASS");

        currentToken++;

        if(!tokens.get(currentToken).getType().equals("IDENTIFIER"))
            error(1);
        System.out.println("- IDENTIFIER");

        currentToken++;

        if(!tokens.get(currentToken).getValue().equals("{"))
            error(1);
        System.out.println("- {");

        currentToken++;

        RULE_METHODS();
 
        if(!tokens.get(currentToken).getValue().equals("}"))
            error(1);	
        System.out.println("- }");
	}

	public void RULE_METHODS(){
		System.out.println("-- RULE_METHODS");
		RULE_TYPE();
		if(tokens.get(currentToken).getType().equals("IDENTIFIER")){
			currentToken++;
			System.out.println("-- IDENTIFIER");
		} else{
			error(2);
		}
		if(tokens.get(currentToken).getValue().equals("(")){
			currentToken++;
			System.out.println("-- (");
		} else{
			error(2);
		}
		RULE_PARAMS();
		if(tokens.get(currentToken).getValue().equals(")")){
			currentToken++;
			System.out.println("-- )");
		} else{
			error(2);
		}
		if(tokens.get(currentToken).getValue().equals("{")){
			currentToken++;
			System.out.println("-- {");
		} else{
			error(2);
		}
		RULE_BODY();
		if(tokens.get(currentToken).getValue().equals("}")){
			currentToken++;
			System.out.println("-- }");
		} else{
			error(2);
		}
	}
	
	public void RULE_BODY() {
		System.out.println("-- RULE_BODY");
		var nextToken = currentToken + 1;
		if(tokens.get(nextToken).getValue().equals("if")){
			RULE_IF();
		} else if(tokens.get(nextToken).getValue().equals("while")){
			RULE_WHILE();
		} else if(tokens.get(nextToken).getValue().equals("for")){
			RULE_FOR();
		} else if(tokens.get(nextToken).getValue().equals("do")){
			RULE_DO_WHILE();
		} else if(tokens.get(nextToken).getValue().equals("switch")){
			RULE_SWITCH();
		}else {
			if(tokens.get(nextToken).getValue().equals("return")){
				RULE_RETURN();
			} else if(tokens.get(nextToken).getType().equals("IDENTIFIER")){
				nextToken += 1;
				if(tokens.get(nextToken).getValue().equals("("))
					RULE_CALL_METHOD();
				else
					RULE_ASSIGNMENT();
			} //MISSING CONDITION FOR VARIABLE
			if(tokens.get(currentToken).getValue().equals(";")){
				currentToken++;
				System.out.println("-- ;");
			}
		}
	}

    public void RULE_FOR(){
        error(1);
    }

    public void RULE_DO_WHILE(){
        error(1);
    }

    public void RULE_SWITCH(){
        error(1);
    }

	public void RULE_TYPE(){
		System.out.println("---- RULE_TYPE");
		if(tokens.get(currentToken).getType().equals("INTEGER")){
			currentToken++;
			System.out.println("-- INTEGER");
		} else if(tokens.get(currentToken).getType().equals("BOOLEAN")){
			currentToken++;
			System.out.println("-- BOOLEAN");
		} else if(tokens.get(currentToken).getType().equals("FLOAT")){
			currentToken++;
			System.out.println("-- FLOAT");
		} else if (tokens.get(currentToken).getType().equals("VOID")) {
			currentToken++;
			System.out.println("-- VOID");
		} else if(tokens.get(currentToken).getType().equals("CHAR")){
			currentToken++;
			System.out.println("-- CHAR");
		} else if (tokens.get(currentToken).getType().equals("STRING")){
			currentToken++;
			System.out.println("-- STRING");
		} else{
			error(4);
		}
	}

	public void RULE_PARAMS(){
		System.out.println("----- RULE_PARAMS");
		RULE_TYPE();
		if(tokens.get(currentToken).getType().equals("IDENTIFIER")){
			currentToken++;
			System.out.println("---- IDENTIFIER");
		}
		else{
			error(4);
		}
		while(tokens.get(currentToken).getValue().equals(",")){
			currentToken++;
			System.out.println("---- ,");

			RULE_TYPE();
			if(tokens.get(currentToken).getType().equals("IDENTIFIER")){
				currentToken++;
				System.out.println("---- IDENTIFIER");
			}
			else{
				error(4);
			}
		}
	}

	public void RULE_ASSIGNMENT(){
		System.out.println("---- RULE_ASSIGNMENT");
		if(tokens.get(currentToken).getType().equals("IDENTIFIER")){
			currentToken++;
			System.out.println("---- IDENTIFIER");
		}
		else return;
		if(tokens.get(currentToken).getValue().equals("=")){
			currentToken++;
			System.out.println("---- =");
		} else{
			error(4);
		}
		RULE_EXPRESSION();
	}
	
	public void RULE_EXPRESSION() {
		System.out.println("--- RULE_EXPRESSION");
		RULE_X();
		while (tokens.get(currentToken).getValue().equals("|") ||
				tokens.get(currentToken).getValue().equals("||")) {
			currentToken++;
			System.out.println("--- | or ||");
			RULE_X();
		}
	}
	
	public void RULE_X() {
		System.out.println("---- RULE_X");
		RULE_Y();
		while (tokens.get(currentToken).getValue().equals("&") ||
				tokens.get(currentToken).getValue().equals("&&")) {
			currentToken++;
			System.out.println("---- & or &&");
			RULE_Y();
		}
	}
	
	public void RULE_Y() {
		System.out.println("----- RULE_Y");
		while (tokens.get(currentToken).getValue().equals("!")) {
			currentToken++;
			System.out.println("----- !");
		}
		RULE_R();
	}
	
	public void RULE_R() {
		System.out.println("------ RULE_R");
		RULE_E();
		while (tokens.get(currentToken).getValue().equals("<")
			| tokens.get(currentToken).getValue().equals(">")
			| tokens.get(currentToken).getValue().equals("==")
			| tokens.get(currentToken).getValue().equals("!=")
		) {
			currentToken++;
			System.out.println("------ relational operator");
			RULE_E();
		}
	}
	
	public void RULE_E() {
		System.out.println("------- RULE_E");
		RULE_A();
		while (tokens.get(currentToken).getValue().equals("-")
			| tokens.get(currentToken).getValue().equals("+")
		) {
			currentToken++;
			System.out.println("------- + or -");
			RULE_A();
		}
		
	}
	
	public void RULE_A() {
		System.out.println("-------- RULE_A");
		RULE_B();
		while (tokens.get(currentToken).getValue().equals("/")
			| tokens.get(currentToken).getValue().equals("*")
		) {
			currentToken++;
			System.out.println("-------- * or /");
			RULE_B();
		}
		
	}
	
	public void RULE_B() {
		System.out.println("--------- RULE_B");
		if (tokens.get(currentToken).getValue().equals("-")) {
			currentToken++;
			System.out.println("--------- -");
		}
		RULE_C();
	}
	
	public void RULE_C() {
		System.out.println("---------- RULE_C");
		if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
			currentToken++;
			System.out.println("---------- IDENTIFIER");
		} else if (tokens.get(currentToken).getType().equals("INTEGER")) {
			currentToken++;
			System.out.println("---------- INTEGER");
		} else if (tokens.get(currentToken).getType().equals("OCTAL")) {
			currentToken++;
			System.out.println("---------- OCTAL");
		} else if (tokens.get(currentToken).getType().equals("HEXADECIMAL")) {
			currentToken++;
			System.out.println("---------- HEXADECIMAL");
		} else if (tokens.get(currentToken).getType().equals("BINARY")) {
			currentToken++;
			System.out.println("---------- BINARY");
		} else if (tokens.get(currentToken).getType().equals("TRUE")) {
			currentToken++;
			System.out.println("---------- TRUE");
		} else if (tokens.get(currentToken).getType().equals("FALSE")) {
			currentToken++;
			System.out.println("---------- FALSE");
		} else if (tokens.get(currentToken).getType().equals("STRING")) {
			currentToken++;
			System.out.println("---------- STRING");
		} else if (tokens.get(currentToken).getType().equals("CHAR")) {
			currentToken++;
			System.out.println("---------- CHAR");
		} else if (tokens.get(currentToken).getType().equals("FLOAT")) {
			currentToken++;
			System.out.println("---------- FLOAT");
		} else if (tokens.get(currentToken).getValue().equals("(")) {
			currentToken++;
			System.out.println("---------- (");
			RULE_EXPRESSION();
			if (tokens.get(currentToken).getValue().equals(")")) {
				currentToken++;
				System.out.println("---------- )");
			} else {
				error(4);
			}
		} else {
			error(5);
		}
	}

	public void RULE_VARIABLE(){
		System.out.println("------ RULE_VARIABLE");
		RULE_TYPE();
		if(tokens.get(currentToken).getType().equals("IDENTIFIER")){
			currentToken++;
			System.out.println("------ IDENTIFIER");
		}
		//HOW TO IMPLEMENT EXPRESSION
	}

	public void RULE_WHILE(){
		System.out.println("------ RULE_WHILE");
		if(tokens.get(currentToken).getType().equals("WHILE")){
			currentToken++;
			System.out.println("------ WHILE");
		}
		else return;
		if(tokens.get(currentToken).getValue().equals("(")){
			currentToken++;
			System.out.println("---- (");
		}
		else {
			error(4);
		}
		RULE_EXPRESSION();
		if(tokens.get(currentToken).getValue().equals(")")){
			currentToken++;
			System.out.println("---- )");
		}
		else {
			error(4);
		}
		if(tokens.get(currentToken).getValue().equals("{")){
			currentToken++;
			System.out.println("---- {");
		}
		else {
			error(4);
		}
		RULE_BODY();
		if(tokens.get(currentToken).getValue().equals("}")){
			currentToken++;
			System.out.println("---- }");
		}
		else {
			error(4);
		}
	}

	public void RULE_IF(){
		System.out.println("------ RULE_IF");
		if(tokens.get(currentToken).getType().equals("IF")){
			currentToken++;
			System.out.println("------ IF");
		}
		else return;

		if(tokens.get(currentToken).getValue().equals("(")){
			currentToken++;
			System.out.println("---- (");
		}
		else {
			error(4);
		}
		RULE_EXPRESSION();
		if(tokens.get(currentToken).getValue().equals(")")){
			currentToken++;
			System.out.println("---- )");
		}
		else {
			error(4);
		}
		if(tokens.get(currentToken).getValue().equals("{")){
			currentToken++;
			System.out.println("---- {");
		}
		else {
			error(4);
		}
		RULE_BODY();
		if(tokens.get(currentToken).getValue().equals("}")){
			currentToken++;
			System.out.println("---- }");
		}
		else {
			error(4);
		}

		if(tokens.get(currentToken).getType().equals("ELSE")){
			currentToken++;
			System.out.println("---- ELSE");
			if(tokens.get(currentToken).getValue().equals("{")){
				currentToken++;
				System.out.println("---- {");
			}
			else {
				error(4);
			}
			RULE_BODY();
			if(tokens.get(currentToken).getValue().equals("}")){
				currentToken++;
				System.out.println("---- }");
			}
			else {
				error(4);
			}
		}
	}

	public void RULE_RETURN(){
		System.out.println("------ RULE_RETURN");
		if(tokens.get(currentToken).getType().equals("RETURN")){
			currentToken++;
			System.out.println("------ RETURN");
		}
		else return;
		RULE_EXPRESSION();
	}

	public void RULE_CALL_METHOD(){
		System.out.println("------ RULE_CALL_METHOD");
		if(tokens.get(currentToken).getType().equals("IDENTIFIER")){
			currentToken++;
			System.out.println("------ IDENTIFIER");
		}
		else return;
		if(tokens.get(currentToken).getValue().equals("(")){
			currentToken++;
			System.out.println("---- (");
		}
		else {
			error(4);
		}
		RULE_PARAM_VALUES();
		if(tokens.get(currentToken).getValue().equals(")")){
			currentToken++;
			System.out.println("---- )");
		}
		else {
			error(4);
		}
	}

	public void RULE_PARAM_VALUES(){
		System.out.println("------ RULE_PARAM_VALUES");
		RULE_EXPRESSION();
		while (tokens.get(currentToken).getValue().equals(",")){
			currentToken++;
			System.out.println("------ ,");
			RULE_EXPRESSION();
		}
	}

	private void error(int error) {
		System.out.println("Error " + error +
			" at line " + tokens.get(currentToken));
		System.exit(1);
	}
	
}

