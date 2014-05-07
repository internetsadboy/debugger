package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.*;

public class LitCode extends ByteCode{
    
    String n;
    String id;
    boolean add = false;
    
    @Override
    public void execute(VirtualMachine vm) {  
        vm.pushLit(Integer.parseInt(n));
    }

    @Override
    public void init(Vector<String> args) {
        n = args.get(0);
        if(args.size() > 1){
            id = args.get(1);
            add = true;
        }
    }
    
    @Override
    public String toString(){
        return ("LIT" + " " + n + " " + id);
    }
    
    public String getID(){
        return id;
    }
    
    public boolean getAddSym(){
        return add;
    }
}