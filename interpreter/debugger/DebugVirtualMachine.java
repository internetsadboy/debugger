package interpreter.debugger;

import interpreter.bytecodes.debugbytecodes.*;
import interpreter.debugger.ui.*;
import interpreter.bytecodes.*;
import interpreter.*;
import java.util.*;
import java.io.*;

// every time we see main or factorial -> push name to stack
// check fer size to see if there is a new fxn call 
// old call(s) -> static
// update current line for most recent call
// if fer decreases pop curr fxn
// if fer increases push curr fxn

public class DebugVirtualMachine extends VirtualMachine {

    private Stack<FunctionEnvironmentRecord> FERstack;   
    private FunctionEnvironmentRecord fer;
    private Stack<Integer> callStackLines;
    private Stack<String> callStack;
    private Stack<Integer> startFunc;
    private Vector<Source> lineCode;
    private Stack<Integer> endFunc;
    private Vector<String> trace;
    private boolean isIntrinsic;
    private boolean stepOver;
    private boolean stepOut;
    private boolean stepIn;
    private boolean isTraceOn;
    private boolean printCallStack;
    private int nextEnvSize;
    private Source srcLine;
    private DebugUI ui;
    private int currLineNum;

    public DebugVirtualMachine(Program program) {
        super(program);
        lineCode = new Vector<>();
        FERstack = new Stack<>();
        startFunc = new Stack<>();
        endFunc = new Stack<>();
        trace = new Vector<String>();
        fer = new FunctionEnvironmentRecord();
        runStack = new RunTimeStack();
        returnAddr = new Stack();
        callStack = new Stack<String>();
        callStackLines = new Stack<Integer>();
        isIntrinsic = false;
        isRunning = true;
        stepOut = false;
        stepIn = false;
        stepOver = false;
        isTraceOn = false;
        printCallStack = false;
        nextEnvSize = -1;
        currLineNum = -1;
        pc = 0;
    }

    @Override
    public void executeProgram() {
        while (isRunning) {

            ByteCode code = program.getCode(pc);
            
            // check bp
            if (code instanceof LineCode) {

                LineCode tempLine = (LineCode) code;
                currLineNum = tempLine.getLineNum();
                
                // check if bp set
                if (tempLine.getLineNum() > 0 && lineCode.get(tempLine.getLineNum() - 1).getIsBreakpoint()) {
                    
                    // clear all step flags (since we encounter a bp)
                    stepOut = false;
                    stepIn = false;
                    stepOver = false;

                    // execute each bc
                    code.execute(this);
                    pc++;                   
                    code = program.getCode(pc);
                    
                    // exec function
                    // update new func lines
                    if (code instanceof FunctionCode) {
                        FunctionCode temp = (FunctionCode) code;
                        code.execute(this);
                        startFunc.push(temp.getStart()); 
                        endFunc.push(temp.getEnd());  
                        pc++;
                        code = program.getCode(pc);
                        // exec potential formal
                        while (code instanceof FormalCode) {
                          code.execute(this);
                          pc++;
                          code = program.getCode(pc);
                        }
                    }
                    // return control to the debugger
                    break;
                }
            }
            // record start and end lines of cur fxn
            if (code instanceof FunctionCode) {
                FunctionCode temp = (FunctionCode) code;
                startFunc.push(temp.getStart());
                endFunc.push(temp.getEnd());
                
                    String funcName = temp.getName();
                    if(!funcName.equals("Read") && !funcName.equals("Write")) {
                      //System.out.println(funcName+": "+currLineNum);   
                      //callStack.push(funcName); // add func to callStack 
                    }
            }

            // check step out
            if(stepOut && (nextEnvSize == FERstack.size())) {
                stepOut = false;
                nextEnvSize = -1;
                break;
            }

            // check step in
            if(stepIn) {
              // instrinsic fxn, don't display source  
              if(code instanceof ReadCode) {
                System.out.println("**** READ ****");
                code.execute(this);
                pc++;
                isIntrinsic = true;
                break;
              }
              // instrinsic fxn, don't display source
              if(code instanceof WriteCode) {
                System.out.println("**** WRITE ****");
                code.execute(this);
                pc++;
                isIntrinsic = true;
                break;
              }
              // if fer same && new line
              // else if fer +1 or new line 
              if((nextEnvSize-1 == FERstack.size()) && (code instanceof LineCode)) {
                code.execute(this);
                pc++;   
                stepIn = false;
                nextEnvSize = -1;
                break; 
              } else if(nextEnvSize == FERstack.size()) {
                for (int i = 0; i < 2; i++) {
                    code.execute(this);
                    pc++;
                    code = program.getCode(pc);
                }
                while(code instanceof FormalCode) {
                    code.execute(this);
                    pc++;
                    code = program.getCode(pc);
                }
                stepIn = false;
                nextEnvSize = -1;
                break; 
              }
              
            }
            // check step over
            if(stepOver) {
              // if set, fer +0, && new line
              if((nextEnvSize == FERstack.size()) && (code instanceof LineCode)) {
                stepOver = false;
                nextEnvSize = -1;
                break;
              }
            }
            // else good old curr bc execute(vm)
            code.execute(this);
            pc++;
        }
    }

    public void loadSource(String sourceFile) throws FileNotFoundException, IOException {
      BufferedReader reader = (new BufferedReader(new FileReader(sourceFile)));
      String nextLine;
      while (reader.ready()) {
        srcLine = new Source();
        nextLine = reader.readLine();
        srcLine.setSourceLine(nextLine);
        lineCode.add(srcLine);
      }
    }

    public void setCrrntLine(int currentLine) {
        fer.setCurrentLine(currentLine);
    }

    public void setFunc(String name, int start, int end) {
        fer.setName(name);
        fer.setStartLine(start);
        fer.setEndLine(end);
    }

    public Vector<Source> getLineCode() {
        return lineCode;
    }

    public int getLine() {
        return fer.getCurrentLine();
    }

    public boolean setBrks(Vector<Integer> breaks) {
        for (Integer line : breaks) {
            if (!(program.isValidBrk(line))) {
                return false;
            }
            lineCode.get(line - 1).isBreakpoint(true);
        }
        return true;
    }

    public void clrBrks(Vector<Integer> breaks){
         for (Integer line : breaks) {
            lineCode.get(line - 1).isBreakpoint(false);
        }
    }

    public void setIsRunning(boolean running) {
        isRunning = running;
    }

    public boolean getIsRunning() {
        return isRunning;
    }

    public void newFER() {
        FERstack.push(fer);
        //System.out.println("push "+FERstack.size());
        fer = new FunctionEnvironmentRecord();
        fer.beginScope();
    }

    public void endFER() {
        fer = FERstack.pop();
        //System.out.println("pop  "+FERstack.size());
    }

    public void setTrace(boolean wtf) {
        isTraceOn = wtf;
    }

    public boolean isTraceOn() {
        return isTraceOn;
    }

    public Vector<String> getTrace() {
        return trace;
    }

    public int getFERsize() {
        return FERstack.size();
    }

    public String getFuncName() {
        return fer.getName();
    }

    public String getValue(int offset) {
        return runStack.getValue(offset);
    }

    public ByteCode getCode(int pc) {
        return program.getCode(pc);
    }

    public int getPC() {
        return pc;
    }

    public void clearTrace() {
        trace.clear();
    }

    public void addTrace(String function) {
        trace.add(function);
    }

    
    // stepOut:  fer - 1
    // stepIn:   fer + 1, || new line
    // stepOver: fer + 0, && new line
    public void setStep(boolean so, boolean si, boolean sv) {
      stepOut = so;
      stepIn = si;
      stepOver = sv;
      if(so) {
        nextEnvSize = FERstack.size() - 1;  
      }
      if(si) {
        nextEnvSize = FERstack.size() + 1;
      }
      if(sv) {
        nextEnvSize = FERstack.size();
      }
    }

    public boolean isIntrinsic() {
        return isIntrinsic;
    }

    public void setIntrinsic(boolean boo) {
      isIntrinsic = boo;
    }

    public Vector<Integer> displayFunc() {
        if (startFunc.size() == 0 && endFunc.size() == 0) {
            return null;
        }
        Vector<Integer> currentFunc = new Vector<Integer>();
        currentFunc.add(startFunc.peek());
        currentFunc.add(endFunc.peek());
        return currentFunc;
    }
    
    public String[][] displayVars(){
        String[][] variables = fer.getVar();
        for (int i = 0; i < variables.length; i++) {
            String offset = variables[i][1];
            variables[i][1] = (runStack.getValue(Integer.parseInt(offset)));
        }
        return variables;
    }

    public void enterSymbol(String var, int offset) {
        fer.enterID(var, offset);
    }

    public void popSymbol(int offset) {
        fer.popIds(offset);
    }

    public boolean getStepOut() {
        return stepOut;
    }

    public Vector<String> printStack(){
        Vector<String> callStack = new Vector<String>();
        String name = fer.getName();
        if(!name.equals("Read") && !name.equals("Write")) {
          callStack.add(fer.getName() + ": " + fer.getCurrentLine());    
        } 
        Object[] stack = FERstack.toArray();
        for (Object FER : stack) {
            FunctionEnvironmentRecord temp = (FunctionEnvironmentRecord) FER;
            String name2 = temp.getName();
            if(!name2.equals("Read") && !name2.equals("Write")) {
              callStack.add(name+ ": " + temp.getCurrentLine());
            }
        }
        return callStack;
    }

}