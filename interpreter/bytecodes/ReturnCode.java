package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.*;

public class ReturnCode extends ByteCode{
    String label;
    protected int val;

    @Override
    public void execute(VirtualMachine vm) {  
        vm.setPC(vm.popReturnAddr());
        vm.popFrame();
        val = vm.peekRunStack();
    }

    @Override
    public void init(Vector<String> args) {
        if(args.size() > 0)
            label = args.get(0);
        else
            label = null;
    }
    
    @Override
    public String toString(){
        return ("RETURN" + " " + label);
    }
}
