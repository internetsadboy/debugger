package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.*;

public class LoadCode extends ByteCode{
    String n;
    String id;
    
    @Override
    public void execute(VirtualMachine vm) {
        vm.load(Integer.parseInt(n));
    }

    @Override
    public void init(Vector<String> args) {
        n = args.get(0);
        id = args.get(1);
    }
    
    @Override
    public String toString(){
        return ("LOAD" + " " + n + " " + id);
    }
}
