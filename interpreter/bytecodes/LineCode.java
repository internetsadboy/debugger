package interpreter.bytecodes;

import interpreter.debugger.DebugVirtualMachine;
import interpreter.bytecodes.*;
import interpreter.*;
import java.util.*;

public class LineCode extends ByteCode {
   
    int n;
    
    @Override
    public void execute(VirtualMachine vm) {
        DebugVirtualMachine dvm = (DebugVirtualMachine) vm;
        dvm.setCrrntLine(n);
    }

    @Override
    public void init(Vector<String> args) {
        n = Integer.valueOf(args.get(0));
    }
    
    public int getLineNum(){
        return n;
    }
    
    @Override
    public String toString() {
        return "LINE " + n;
    }
}
