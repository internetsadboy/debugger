package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.*;

public class ReadCode extends ByteCode{
    @Override
    public void execute(VirtualMachine vm) {   
        Scanner scn = new Scanner(System.in);
        System.out.print("Enter an integer: ");
        vm.read(scn.nextInt());        
    }

    @Override
    public void init(Vector<String> args) {

    }
    
    @Override
    public String toString(){
        return ("READ");
    }
}