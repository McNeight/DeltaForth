/*
**********************************************************
	Reverse Polish Notation Parser
	(C)1999 Valer BOCAN <vbocan@dataman.ro>
	
	Date of creation:       Wednesday, June 2, 1999
	Date of last update:	Thursday,  June 3, 1999
**********************************************************	
*/

/*
----------------------------------------------------------
    Algorithm:
    
Precedence order table:
Token    EOL    (    )    ^    *    /    +    -
Input     0    100   0    6    3    3    1    1
Stack    -1     0    99   5    4    4    2    2


push(EOL)
do
    read token from expression
    switch(token)
        case operand:
            output token
        case ):
            pop all entries from stack until ( is reached - pop it off stack
        default:
            while InputPrecedence(token) <= StackPrecedence(TopOfStack)
                output top and pop stack
            if token is not EOL
                push token
while(token != EOL)
pop all entries from stack
----------------------------------------------------------
*/

import java.util.*;

public class RPNParser {
	
	// RPN Stack
	Stack RPNStack;
	
	// -- Constructor ------------------------------------
	public RPNParser() {
		RPNStack = new Stack();
	}
	
	// -- Parse ------------------------------------------
	public String Parse(String InpString) throws Exception {
        
        // Check parantheses
        int pars = 0;
        for(int i=0; i<InpString.length(); i++) {
            if(InpString.charAt(i) == '(') pars++;
            if(InpString.charAt(i) == ')') pars--;
        }
        if(pars != 0) throw new Exception("Incorrect use of parentheses.");
        
        String retstr = new String();
		StringTokenizer st = new StringTokenizer(InpString, "()^*/+-", true);
	
        RPNStack.push("");
		while(st.hasMoreTokens()) {
			String token = st.nextToken().trim();
			if(!isOperator(token)) {
			    retstr += token + " ";
			} else 
				if(token.equals(")")) {
					String stk;
					while(!(stk = (String)RPNStack.pop()).equals("(")) retstr += stk + " ";
				} else {
					while(InputPrecedence(token) <= StackPrecedence()) retstr += (String)RPNStack.pop() + " ";
                                        RPNStack.push(token);
				}
		}
        for(int i=0; i<RPNStack.size(); i++) retstr += (String)RPNStack.pop() + " ";
        
		return retstr.trim();
	}
	
	// -- isOperator -------------------------------------
	private boolean isOperator(String Operator) {
		return ((Operator.equals("(")) ||
		       (Operator.equals(")")) ||
		       (Operator.equals("^")) ||
		       (Operator.equals("*")) ||
		       (Operator.equals("/")) ||
                       (Operator.equals("+")) ||
                       (Operator.equals("-"))
               );
	}

	// -- InputPrecedence --------------------------------
	private int InputPrecedence(String token) {
		int prec = 0;
		if(token.equals("(")) prec = 100;
		else if(token.equals(")")) prec = 0;
		else if(token.equals("^")) prec = 6;
		else if(token.equals("*")) prec = 3;
		else if(token.equals("/")) prec = 3;
		else if(token.equals("+")) prec = 1;
		else if(token.equals("-")) prec = 1;
		
		return prec;
	}
	
	// -- StackPrecedence --------------------------------
	private int StackPrecedence() {
		int prec = -1;
		String top = (String)RPNStack.peek();
		if(top.equals("(")) prec = 0;
		else if(top.equals(")")) prec = 99;
		else if(top.equals("^")) prec = 5;
		else if(top.equals("*")) prec = 4;
		else if(top.equals("/")) prec = 4;
		else if(top.equals("+")) prec = 2;
		else if(top.equals("-")) prec = 2;
		
		return prec;
	}
}