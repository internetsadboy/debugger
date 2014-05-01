package interpreter.debugger;

import interpreter.debugger.ui.*;
import interpreter.bytecodes.*;
import interpreter.*;
import java.util.*;
import java.io.*;

public class DebugVirtualMachine extends VirtualMachine {

    private Stack<FunctionEnvironmentRecord> FERstack;   
    private FunctionEnvironmentRecord fer;
    private Stack<Integer> startFunc;
    private Vector<Source> lineCode;
    private Stack<Integer> endFunc;
    private Vector<String> trace;
    private boolean isIntrinsic;
    private boolean stepOver;
    private boolean stepOut;
    private boolean stepIn;
    private boolean isTraceOn;
    private int nextEnvSize;
    private Source srcLine;
    private DebugUI ui;

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
        isIntrinsic = false;
        isRunning = true;
        stepOut = false;
        stepIn = false;
        stepOver = false;
        isTraceOn = false;
        nextEnvSize = -1;
        pc = 0;
    }

    @Override
    public void executeProgram() {
        while (isRunning) {

            ByteCode code = program.getCode(pc);

            if(code instanceof FunctionCode) {
                
            }

            // check bp
            if (code instanceof LineCode) {

                LineCode tempLine = (LineCode) code;
                
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
                    if (code instanceof FunctionCode) {
                        code.execute(this);
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
            }

            // check step out
            if(stepOut && (nextEnvSize == FERstack.size())) {
                System.out.println("step out");
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
              System.out.println("step in");
              
              // if fer same && new line
              // if fer +1 or new line 
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
              System.out.println("step over");
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

}