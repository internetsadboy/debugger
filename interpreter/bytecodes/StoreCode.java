package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.*;

public class StoreCode extends ByteCode{
    private String n;
    private String id;
    private int value;
    
    @Override
    public void execute(VirtualMachine vm) {
        value = vm.store(Integer.parseInt(n));    
        
    }

    @Override
    public void init(Vector<String> args) {
        n = args.get(0);
        id = args.get(1);
    }
    
    @Override
    public String toString() {
        return "STORE " + Integer.parseInt(n) + " " + id + "   " + id + " = " + value;
    }
}