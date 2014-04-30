package interpreter.bytecodes.debugbytecodes;

import interpreter.*;
import interpreter.bytecodes.LitCode;
import interpreter.debugger.DebugVirtualMachine;

public class DebugLitCode extends LitCode {
    @Override
    public void execute(VirtualMachine vm){
        DebugVirtualMachine dvm = (DebugVirtualMachine) vm;
        super.execute(dvm);
        if(getAddSym()){
            int offset = dvm.getRunStackSize();
            dvm.enterSymbol(getID(), offset); 
        }
    }
}
