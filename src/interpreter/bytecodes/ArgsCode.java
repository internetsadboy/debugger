package interpreter.bytecodes;

import java.util.*;
import interpreter.*;

public class ArgsCode extends ByteCode{
    
    String n;
    
    @Override
    public void execute(VirtualMachine vm) {
        vm.setFrame(Integer.parseInt(n));
    }

    @Override
    public void init(Vector<String> args) {
        n = args.get(0);
    }
    
    @Override
    public String toString(){
        return ("ARGS "+n);
    }
    
}