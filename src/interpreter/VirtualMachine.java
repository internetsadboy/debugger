package interpreter;

import java.util.*;
import interpreter.bytecodes.*;

public class VirtualMachine {

    protected Program program;
    protected RunTimeStack runStack;
    protected Stack<Integer> returnAddr;
    protected int pc;
    protected boolean isRunning;
    protected boolean isDumping;

    public VirtualMachine(Program prog) {
        program = prog;
    }

    public void executeProgram() {
        pc = 0;
        runStack = new RunTimeStack();
        returnAddr = new Stack<>();
        isRunning = true;
        isDumping = false;
        while (isRunning) {
            ByteCode code = program.getCode(pc);
            code.execute(this);
            if (!(code instanceof DumpCode)) {
                if (isDumping) {
                    System.out.println(code.toString());
                    runStack.dump();
                }
            }
            pc++;
        }
    }

    public void setFrame(int position) {
        runStack.newFrameAt(position);
    }

    public void setDumpOn(boolean isOn) {
        isDumping = isOn;
    }

    public void changeAddr(int target) {
        returnAddr.push(pc);
        pc = target;
    }

    public void bop(String Op) {
        int secondOp = runStack.pop();
        int firstOp = runStack.pop();

        switch (Op) {
            case "+":
                runStack.push(firstOp + secondOp);
                break;
            case "-":
                runStack.push(firstOp - secondOp);
                break;
            case "*":
                runStack.push(firstOp * secondOp);
                break;
            case "/":
                runStack.push(firstOp / secondOp);
                break;
            case "==":
                if (firstOp == secondOp) {
                    runStack.push(1);
                } else {
                    runStack.push(0);
                }
                break;
            case "!=":
                if (firstOp != secondOp) {
                    runStack.push(1);
                } else {
                    runStack.push(0);
                }
                break;
            case "<":
                if (firstOp < secondOp) {
                    runStack.push(1);
                } else {
                    runStack.push(0);
                }
                break;
            case "<=":
                if (firstOp <= secondOp) {
                    runStack.push(1);
                } else {
                    runStack.push(0);
                }
                break;
            case ">":
                if (firstOp > secondOp) {
                    runStack.push(1);
                } else {
                    runStack.push(0);
                }
                break;
            case ">=":
                if (firstOp >= secondOp) {
                    runStack.push(1);
                } else {
                    runStack.push(0);
                }
                break;
            case "|":
                if ((firstOp == 1) || (secondOp == 1)) {
                    runStack.push(1);
                } else {
                    runStack.push(0);
                }
                break;
            case "&":
                if ((firstOp == 1) && (secondOp == 1)) {
                    runStack.push(1);
                } else {
                    runStack.push(0);
                }
                break;
            default:
                System.out.println("Error (Not Recognized Op) : " + Op);
                break;
        }
    }

    public void falseBranch(int target) {
        if (runStack.pop() == 0) {
            pc = target;
        }
    }

    public void goTo(int target) {
        pc = target;
    }

    public void halt() {
        isRunning = false;
    }

    public void pushLit(int lit) {
        runStack.push(lit);
    }

    public void load(int offset) {
        runStack.load(offset);
    }

    public void pop(int amount) {
        for (int i = 0; i < amount; i++) {
            runStack.pop();
        }
    }
    
    public void read(int value){
        runStack.push(value);
    }
    
    public int store(int offset){
        return runStack.store(offset);
    }
    
    public int write(){
        return (runStack.peek());
    }
    
    public int popReturnAddr(){
        return returnAddr.pop();
    }
    
    public void setPC(int addr){
        pc = addr;
    }
    
    public void popFrame(){
        runStack.popFrame();
    }
    
    public int peekRunStack(){
        return runStack.peek();
    }
    
    public Vector<Integer> getArgs(){
        return runStack.getArgs();
    }

    public int getRunStackSize(){
        return runStack.runStack.size();
    }
}
