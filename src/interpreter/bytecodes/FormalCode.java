package interpreter.bytecodes;

import interpreter.debugger.DebugVirtualMachine;
import interpreter.bytecodes.ByteCode;
import interpreter.*;
import java.util.*;

public class FormalCode extends ByteCode {
    
    private String arg, offset;
    
    @Override
    public void execute(VirtualMachine vm) {
        DebugVirtualMachine dvm = (DebugVirtualMachine) vm;
        dvm.enterSymbol(arg, dvm.getRunStackSize() - Integer.parseInt(offset));
    }

    @Override
    public void init(Vector<String> args){
        arg = args.get(0);
        offset = args.get(1);
    }

    @Override
    public String toString() {
        return "FORMAL " + arg + " " + offset;
    }
    
    public String getOffset(){
        return offset;
    }
    
}
