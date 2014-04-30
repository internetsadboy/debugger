package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.*;

public class PopCode extends ByteCode {
    
    String n;
    
    @Override
    public void execute(VirtualMachine vm) {    
        vm.pop(Integer.parseInt(n));
    }
    
    @Override
    public void init(Vector<String> args){
        n = args.get(0);
    }
    @Override
    public String toString(){
        return ("POP" +  " " + n);
    }
    
    public int getOffset(){
        return Integer.parseInt(n);
    }
}
