/*
**********************************************************
	Reverse Polish Notation Processor
	(C)1999 Valer BOCAN <vbocan@dataman.ro>
	
	Date of creation:       Monday,    June 7, 1999
	Date of last update:	Wednesday, June 9, 1999
**********************************************************	
*/

import java.util.*;

public class RPNProc {

    final int _SWAP = 1;
    final int _ROT  = 2;
    final int _DROP = 3;
    final int _OVER = 4;
    final int _DUP  = 5;
   
	private Hashtable vfreq;
	private String RPNString;
    private Stack RPNStack;
    int tempVar;
    
    LinkedList Operations;

        // -- Constructor --------------------------------
	public RPNProc() {
		vfreq = new Hashtable();
        RPNStack = new Stack();
        Operations = new LinkedList();
        tempVar = 'a';
	}

	// -- Process ----------------------------------------
	public String Process(String InpStr, String IniStack) throws Exception {
        String token1 = new String();        // Token 1
        String token2 = new String();        // Token 2

		RPNString = InpStr;
        
        // Load stack
        for(int i=0; i<IniStack.length(); i++) {
            String temp = new String();
            temp += (char)IniStack.charAt(i);
            RPNStack.push(temp);
        }
        
        BuildFreq();
        
        StringTokenizer st = new StringTokenizer(RPNString, " ", false);
        while(st.hasMoreTokens()) {
            String tst = st.nextToken();
            if(!isOperator(tst)) {
                token1 = token2;
                token2 = tst;
            } else {
                if(isNumber(token1)) {
                    RPNStack.add(token1);
                    //System.out.println(token1);
                    Operations.add(token1);
                }
                if(isNumber(token2)) {
                    RPNStack.add(token2);
                    //System.out.println(token2);
                    Operations.add(token2);
                }
                
                ArrangeTokens(token1, token2);
                
                String tok1 = peekRPNStack(1);
                String tok2 = peekRPNStack(0);
                boolean stkchanged = false;

                if(GetFreq(tok1) > 1) {
                    execRPNStack(_OVER);
                    DecrFreq(tok1);
                    stkchanged = true;
                }
                if(GetFreq(tok2) > 1) {
                    if(!stkchanged) execRPNStack(_DUP);
                               else execRPNStack(_OVER);
                    DecrFreq(tok2);
                }
                
                ArrangeTokens(token1, token2);
                
                ProcessOperator(token1, token2, tst);
                if((RPNString.trim()).length() <= 1) break;
                st = new StringTokenizer(RPNString, " ", false);
            }
        }
        
        // Display operations
        String OutStr = new String();
        for(int i=0; i<Operations.size(); i++) OutStr += Operations.get(i) + " ";
        return OutStr;
	}
    
    // -- ArrangeTokens --------------------------------
    private void ArrangeTokens(String token1, String token2) throws Exception {
        boolean inplace1 = false;
        boolean inplace2 = false;
                
        int count;
        for(count = 0; count < 4; count++) {
            if(inplace1 && inplace2) break;
            // Verify if tokens are in-place
                    
            if(peekRPNStack(1).equals("")) {
                execRPNStack(_DUP);
                DecrFreq(peekRPNStack(0));
            }
            
            if(peekRPNStack(1).equals(token1)) inplace1 = true;
                else {
                    inplace1 = inplace2 = false;
                    if(peekRPNStack(2).equals(token1)) execRPNStack(_ROT);
                    else if(peekRPNStack(0).equals(token1)) execRPNStack(_SWAP);
                     }

            if(peekRPNStack(0).equals(token2)) inplace2 = true;
                else {
                    inplace2 = inplace1 = false;
                    if(peekRPNStack(2).equals(token2)) execRPNStack(_ROT);
                    else if(peekRPNStack(1).equals(token2)) execRPNStack(_SWAP);
                     }
        }
        
        if(count >= 4) throw new Exception();
    }
    
    // -- ProcessOperator ------------------------------
    private void ProcessOperator(String token1, String token2, String oper) {
        LinkedList tokens = new LinkedList();

        StringTokenizer st = new StringTokenizer(RPNString, " ", false);
        while(st.hasMoreTokens()) tokens.add(st.nextToken());
        st = null;
        
        for(int i=tokens.size()-1; i>=0; i--) {
            if(((String)tokens.get(i)).equals(oper)) {
                if( ((String)tokens.get(i-1)).equals(token2) &&
                    ((String)tokens.get(i-2)).equals(token1) ) {
                        tokens.remove(i);
                      
                        String x = new String();
                        x += (char)getVar();
                        tokens.add(i, x);
                        
                        tokens.remove(i-1);
                        tokens.remove(i-2);
                        
                        execRPNStack(_DROP);
                        execRPNStack(_DROP);
                        RPNStack.add(x);
                        
                        break;
                    }
            }
        }
        
        RPNString = "";
        for(int i=0; i<tokens.size(); i++) RPNString += (String)tokens.get(i) + " ";
        
        // Avoid SWAP before comutable operations
        try {
            if(oper.equals("+") || oper.equals("*")) 
                if(Operations.getLast().equals("SWAP")) Operations.removeLast();
        } catch(NoSuchElementException e) {}
        
        Operations.add(oper);
    }

    // -- BuildFreq ----------------------------------
	private void BuildFreq() {
		StringTokenizer st = new StringTokenizer(RPNString, " ", false);
		while(st.hasMoreTokens()) {
			String tst = st.nextToken();
			if(isVariable(tst)) {
				int value;
				Object obj = vfreq.get(tst);
				if(obj == null) value = 0;
          				   else value = ((Integer)obj).intValue();
				value++;
				vfreq.put(tst, new Integer(value));
			}
		}
	}
    
    // -- GetFreq ------------------------------------
    private int GetFreq(String token) {
        int value;
        Object obj = vfreq.get(token);
        if(obj == null) value = 0;
            else value = ((Integer)obj).intValue();
        return value;
    }
    
    // -- DecrFreq ------------------------------------
    private void DecrFreq(String token) {
        int value;
        Object obj = vfreq.get(token);
        if(obj == null) value = 0;
            else value = ((Integer)obj).intValue();
        value--;
        vfreq.put(token, new Integer(value));
    }
	
	// -- isOperator ---------------------------------
	private boolean isOperator(String Operator) {
		return ((Operator.equals("^")) ||
		       (Operator.equals("*")) ||
		       (Operator.equals("/")) ||
               (Operator.equals("+")) ||
               (Operator.equals("-")));
	}

	// -- isVariable ---------------------------------
	private boolean isVariable(String Variable) {
        if(Variable.length() > 1) return false;
            else return (Variable.compareTo("A") >= 0 && Variable.compareTo("Z") <= 0);
	}
    
    // -- isNumber -----------------------------------
    private boolean isNumber(String Variable) {
            boolean isn = true;
            for(int i=0; i<Variable.length(); i++) {
                isn = isn && (Variable.charAt(i) >= '0' && Variable.charAt(i) <= '9');
            }
            
            return isn;
    }

    // -- getVar -------------------------------------
    private int getVar() {
        return tempVar++;
    }
  
    // -- peekRPNStack -------------------------------
    private String peekRPNStack(int pos) {
        if(pos >= RPNStack.size()) return "";
        else return (String)RPNStack.get(RPNStack.size() - pos - 1);
    }
    
    // -- execRPNStack -------------------------------
    private void execRPNStack(int opcode) {
        Object ob1 = null, ob2 = null, ob3 = null;
        /*
        System.out.print("Stack: ");
        for(int k=0; k<RPNStack.size(); k++)  System.out.print(peekRPNStack(k) + " ");
        System.out.println();
        */
        try {
        switch(opcode) {
            case _SWAP: 
               ob1 = RPNStack.pop();
               ob2 = RPNStack.pop();
               RPNStack.push(ob1);
               RPNStack.push(ob2);
               Operations.add("SWAP");
               break;
           case _ROT:
               ob1 = RPNStack.pop();
               ob2 = RPNStack.pop();
               ob3 = RPNStack.pop();
               RPNStack.push(ob2);
               RPNStack.push(ob1);
               RPNStack.push(ob3);
               Operations.add("ROT");
               break;
           case _DROP:
               RPNStack.pop();
               break;
           case _OVER:
               ob1 = RPNStack.pop();
               ob2 = RPNStack.pop();
               RPNStack.push(ob2);
               RPNStack.push(ob1);
               RPNStack.push(ob2);
               Operations.add("OVER");
               break;
           case _DUP:
               ob1 = RPNStack.pop();
               RPNStack.push(ob1);
               RPNStack.push(ob1);
               Operations.add("DUP");
               break;
        }
    } catch(EmptyStackException e) {}
    }
}