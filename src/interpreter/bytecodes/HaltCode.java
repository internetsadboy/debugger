package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.*;

public class HaltCode extends ByteCode{
    @Override
    public void execute(VirtualMachine vm) { 
        vm.halt();
    }
    
    @Override
    public void init(Vector<String> args) {}
    
    @Override
    public String toString(){
        return ("HALT");
    }
}
