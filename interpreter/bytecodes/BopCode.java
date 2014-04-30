package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.*;

public class BopCode extends ByteCode{
    String arg;
    
    @Override
    public void execute(VirtualMachine vm) {  
        vm.bop(arg);
    }

    @Override
    public void init(Vector<String> args) {
        arg = args.get(0);
    }
    
    @Override
    public String toString(){
        return ("BOP" + " " + arg);
    }
}
