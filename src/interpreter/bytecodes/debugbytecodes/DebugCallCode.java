package interpreter.bytecodes.debugbytecodes;

import interpreter.debugger.DebugVirtualMachine;
import interpreter.bytecodes.CallCode;
import interpreter.*;
import java.util.*;

public class DebugCallCode extends CallCode {
    
    @Override
    public void execute(VirtualMachine vm){
        DebugVirtualMachine dvm = (DebugVirtualMachine) vm;
        super.execute(dvm);
        dvm.newFER();
    }
    
    @Override
    public String toString(){
        return super.toString();
    }
    
}
