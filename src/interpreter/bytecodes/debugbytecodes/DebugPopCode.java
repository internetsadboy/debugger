package interpreter.bytecodes.debugbytecodes;

import interpreter.*;
import interpreter.bytecodes.PopCode;
import interpreter.debugger.DebugVirtualMachine;

public class DebugPopCode extends PopCode {
    
    @Override
    public void execute(VirtualMachine vm) {
        DebugVirtualMachine dvm = (DebugVirtualMachine) vm;
        super.execute(dvm);
        dvm.popSymbol(getOffset());
    }
}
