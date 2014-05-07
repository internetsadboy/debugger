package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.*;

public class FalseBranchCode extends ByteCode{
    String label;
    Integer targetAddr;
    
    @Override
    public void execute(VirtualMachine vm) {
        vm.falseBranch(targetAddr);
    }

    @Override
    public void init(Vector<String> args) {
        label = args.get(0);
    }
    
    public String getLabel(){
        return label;
    }
    
    public void setAddr(Integer addr){
        targetAddr = addr;
    }
    
    @Override
    public String toString(){
        return ("FALSEBRANCH" + " " + label);
    }
}
