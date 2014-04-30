package interpreter.bytecodes.debugbytecodes;

import interpreter.bytecodes.ReturnCode;
import interpreter.*;
import interpreter.debugger.DebugVirtualMachine;

public class DebugReturnCode extends ReturnCode{
    @Override
    public void execute(VirtualMachine vm) {
        DebugVirtualMachine dvm = (DebugVirtualMachine) vm;
        super.execute(dvm);
        dvm.endFER();
    }
}
