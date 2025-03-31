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

        while(!tokens.get(currentToken).getValue().equals("}"))
            RULE_METHODS();	
        System.out.println("- }");
	}

	public void RULE_METHODS(){
		System.out.println("-- RULE_METHODS");	
        RULE_TYPE();

		if(!tokens.get(currentToken).getType().equals("IDENTIFIER"))
            error(2);

        currentToken++;
        System.out.println("-- IDENTIFIER");
		
        if(!tokens.get(currentToken).getValue().equals("("))
            error(2);

        currentToken++;
        System.out.println("-- (");

		RULE_PARAMS();

		if(!tokens.get(currentToken).getValue().equals(")"))
            error(2);
        
        currentToken++;
        System.out.println("-- )");

		if(!tokens.get(currentToken).getValue().equals("{"))
            error(2);

        currentToken++;
        System.out.println("-- {");


		while(!tokens.get(currentToken).getValue().equals("}")){ 
		    System.out.println(tokens.get(currentToken));
            RULE_BODY();
        }

        currentToken++;
        System.out.println("-- }");
	}
	
	public void RULE_BODY() {
		System.out.println("-- RULE_BODY"); 

        if(tokens.get(currentToken).getValue().equals("}")){
			System.out.println("--- }");
			return;
		}

        if(!(tokens.get(currentToken).getType().equals("KEYWORD") || tokens.get(currentToken).getType().equals("IDENTIFIER"))){
			error(3);
		}

        //return, control statements and data types are keywords
        if(tokens.get(currentToken).getType().equals("KEYWORD")){
            
            boolean isControlStatement = true;

            switch(tokens.get(currentToken).getValue()){
                case "if":
                    RULE_IF();
                    currentToken++;
                    break;
                case "while":
                    RULE_WHILE();
                    currentToken++;
                    break;
                case "for":
                    RULE_FOR();
                    currentToken++;
                    break;
                case "switch":
                    RULE_SWITCH();
                    currentToken++;
                    break;
                case "do":
                    RULE_DO_WHILE();
                    currentToken++;
                    break;
                default:
                    isControlStatement = false;
            }

            if(isControlStatement)
                return;

            if(tokens.get(currentToken).getValue().equals("return"))
                RULE_RETURN();
            else
                RULE_VARIABLE();
            
        }
        else{
            if(tokens.get(currentToken + 1).getValue().equals("="))
                RULE_ASSIGNMENT();
            else
                RULE_CALL_METHOD();
        }

        if(!tokens.get(currentToken).getValue().equals(";"))
            error(3);
        currentToken++;
    }

    public void RULE_FOR(){
        System.out.println("-- RULE_FOR");
		if(tokens.get(currentToken).getValue().equals("for"))
			currentToken++;
		else
			error(4);
		if(tokens.get(currentToken).getValue().equals("("))
			currentToken++;
		else
			error(4);

		RULE_VARIABLE();
		RULE_EXPRESSION();
		currentToken++;
		RULE_ASSIGNMENT();

		if(tokens.get(currentToken).getValue().equals(")"))
			currentToken++;
		else
			error(4);

		if(tokens.get(currentToken).getValue().equals("{"))
			currentToken++;
		else
			error(4);

		System.out.println("-- {");

		while(!tokens.get(currentToken).getValue().equals("}")){
			System.out.println(tokens.get(currentToken));
			System.out.println("FOR BODY");
			RULE_BODY();
		}
		System.out.println("-- }");
		System.out.println("-- END FOR");
    }

    public void RULE_DO_WHILE(){
        error(5);
    }

    public void RULE_SWITCH(){
		System.out.println("-- RULE SWITCH");
		if(tokens.get(currentToken).getValue().equals("switch"))
			currentToken++;
		else
        	error(6);

		if(tokens.get(currentToken).getValue().equals("("))
			currentToken++;
		else
			error(6);

		RULE_EXPRESSION();

		if(tokens.get(currentToken).getValue().equals(")"))
			currentToken++;
		else
			error(6);

		if(tokens.get(currentToken).getValue().equals("{")){
			System.out.println("--- {");
			currentToken++;
		}
		else
			error(6);

		while (!tokens.get(currentToken).getValue().equals("default") && !tokens.get(currentToken).getValue().equals("}"))
			SWITCHCASE();

		if(tokens.get(currentToken).getValue().equals("}")){
			System.out.println("--- }");
		}
		else if (tokens.get(currentToken).getValue().equals("default")){
			System.out.println("--- DEFAULT");
			currentToken++;
			if(tokens.get(currentToken).getValue().equals(":"))
				currentToken++;
			else
				error(6);

			RULE_BODY();
		} else
			error(6);
		System.out.println("-- END SWITCH");
    }

	private void SWITCHCASE(){
		System.out.println("--- SWITCHCASE");

		if(tokens.get(currentToken).getValue().equals("case"))
			currentToken++;
		else
			error(6);

		RULE_C();
		if(tokens.get(currentToken).getValue().equals(":"))
			currentToken++;
		else
			error(6);

		while (!tokens.get(currentToken).getValue().equals("break"))
			RULE_BODY();

		if(tokens.get(currentToken).getValue().equals("break")){
			System.out.println("---- BREAK");
			currentToken++;
		}

		if(tokens.get(currentToken).getValue().equals(";"))
			currentToken++;
		else
			error(6);
	}

	public void RULE_TYPE(){
		System.out.println("---- RULE_TYPE");

        if(!tokens.get(currentToken).getType().equals("KEYWORD"))
            error(7);

        switch(tokens.get(currentToken).getValue().toLowerCase()){
            case "int":
			    System.out.println("-- INTEGER");
                break;

            case "boolean":
		    	System.out.println("-- BOOLEAN");
                break;

            case "float":
                System.out.println("-- FLOAT");
                break;

            case "void":
			    System.out.println("-- VOID");
                break;

            case "char":
			    System.out.println("-- CHAR");
                break;
            case "string":
			    System.out.println("-- STRING");
                break;

            default:
                error(7);
        }

        currentToken++;
    }

	public void RULE_PARAMS(){
		System.out.println("----- RULE_PARAMS");

        //NO PARAMS
        if(tokens.get(currentToken).getValue().equals(")"))
            return;

        //WITH PARAMS
		RULE_TYPE();
		if(tokens.get(currentToken).getType().equals("IDENTIFIER")){
			currentToken++;
			System.out.println("---- IDENTIFIER");
		}
		else{
			error(8);
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
				error(8);
			}
		}
	}

	public void RULE_ASSIGNMENT(){
		System.out.println("---- RULE_ASSIGNMENT");
		if(tokens.get(currentToken).getType().equals("IDENTIFIER")){
			currentToken++;
			System.out.println("---- IDENTIFIER");
		}
		else
			error(9);
		if(tokens.get(currentToken).getValue().equals("=")){
			currentToken++;
			System.out.println("---- =");
		} else{
			error(9);
		}
		RULE_EXPRESSION();
	}
	
	public void RULE_EXPRESSION() {
		System.out.println("--- RULE_EXPRESSION");
		RULE_X();
		if(tokens.get(currentToken).getValue().equals("|")){
			int counter = 0;
			while(tokens.get(currentToken).getValue().equals("|") && counter < 2){
				currentToken++;
				counter++;
				System.out.println("--- |");
			}
			RULE_X();
		}
	}
	
	public void RULE_X() {
		System.out.println("---- RULE_X");
		RULE_Y();
		if(tokens.get(currentToken).getValue().equals("&")){
			int counter = 0;
			while(tokens.get(currentToken).getValue().equals("&") && counter < 2){
				currentToken++;
				counter++;
				System.out.println("--- &");
			}
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
		) {
			currentToken++;
			System.out.println("------ relational operator");
			RULE_E();
		}

		if(tokens.get(currentToken).getValue().equals("=")){
			int counter = 0;
			while(tokens.get(currentToken).getValue().equals("=") && counter < 2){
				currentToken++;
				counter++;
			}
			if(counter == 2){
				System.out.println("--- relational operator");
				RULE_E();
			}
		}

		if(tokens.get(currentToken).getValue().equals("!")){
			currentToken++;
			if(tokens.get(currentToken).getValue().equals("=")){
				currentToken++;
				System.out.println("--- relational operator");
				RULE_E();
			}
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
			if(tokens.get(currentToken + 1).getValue().equals("(")){
				RULE_CALL_METHOD();
			}
			else{
				currentToken++;
				System.out.println("---------- IDENTIFIER");
			}
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
				error(10);
			}
		} else {
			error(10);
		}
	}

	public void RULE_VARIABLE(){
		System.out.println("------ RULE_VARIABLE");
		RULE_TYPE();
		if(tokens.get(currentToken).getType().equals("IDENTIFIER")){
			currentToken++;
		}
        
        if(tokens.get(currentToken).getValue().equals(";")){
            return;
        }

        if(!tokens.get(currentToken).getValue().equals("="))
            error(11);

        currentToken++;
        RULE_EXPRESSION();
        currentToken++;
	}

	public void RULE_WHILE(){
		System.out.println("------ RULE_WHILE");
		if(tokens.get(currentToken).getValue().equals("while")){
			currentToken++;
			System.out.println("------ WHILE");
		}
		else return;
		if(tokens.get(currentToken).getValue().equals("(")){
			currentToken++;
			System.out.println("---- (");
		}
		else {
			error(12);
		}
		RULE_EXPRESSION();
		if(tokens.get(currentToken).getValue().equals(")")){
			currentToken++;
			System.out.println("---- )");
		}
		else {
			error(12);
		}
		if(tokens.get(currentToken).getValue().equals("{")){
			currentToken++;
			System.out.println("---- {");
		}
		else {
			RULE_BODY();
		}

		while(!tokens.get(currentToken).getValue().equals("}")){
			RULE_BODY();
		}
		if(tokens.get(currentToken).getValue().equals("}")){
			System.out.println("---- }");
		}
	}

	public void RULE_IF(){
		System.out.println("------ RULE_IF");
		if(tokens.get(currentToken).getValue().equals("if")){
			currentToken++;
			System.out.println("------ IF");
		}
		else error(13);

		if(tokens.get(currentToken).getValue().equals("(")){
			currentToken++;
			System.out.println("---- (");
		}
		else {
			error(13);
		}
		RULE_EXPRESSION();
		if(tokens.get(currentToken).getValue().equals(")")){
			currentToken++;
			System.out.println("---- )");
		}
		else {
			error(13);
		}

		if(tokens.get(currentToken).getValue().equals("{")){
			currentToken++;
			System.out.println("---- {");

			while(!tokens.get(currentToken).getValue().equals("}")) {
				RULE_BODY();
			}

			if(tokens.get(currentToken).getValue().equals("}")){
				currentToken++;
				System.out.println("---- }");
			}
			else {
				error(13);
			}
		} else if(!tokens.get(currentToken).getValue().equals("else")){
			RULE_BODY();
		}
		System.out.println("------ END IF");
		if(tokens.get(currentToken).getValue().equals("else")){
			currentToken++;
			System.out.println("---- ELSE");
			if(tokens.get(currentToken).getValue().equals("if")){
				RULE_IF();
				return;
			}

			if(tokens.get(currentToken).getValue().equals("{")){
				currentToken++;
				System.out.println("---- {");

				while (!tokens.get(currentToken).getValue().equals("}")) {
					RULE_BODY();
				}

				if(tokens.get(currentToken).getValue().equals("}")){
					System.out.println("---- }");
				}
				else {
					error(13);
				}
				System.out.println("------ END } ELSE");
			}
			else {
				RULE_BODY();
				currentToken--;
				System.out.println("------ END ELSE");
			}
		}
	}

	public void RULE_RETURN(){
		System.out.println("------ RULE_RETURN");
		if(tokens.get(currentToken).getValue().equals("return")){
			currentToken++;
			System.out.println("------ RETURN");
		}
		else return;

		if(tokens.get(currentToken).getValue().equals(";")) return;

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
			error(14);
		}
		if(!tokens.get(currentToken).getValue().equals(")"))
			RULE_PARAM_VALUES();
		if(tokens.get(currentToken).getValue().equals(")")){
			currentToken++;
			System.out.println("---- )");
		}
		else {
			error(14);
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

