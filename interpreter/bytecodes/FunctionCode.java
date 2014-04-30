package interpreter.bytecodes;

import interpreter.debugger.DebugVirtualMachine;
import interpreter.bytecodes.ByteCode;
import interpreter.VirtualMachine;
import java.util.*;

public class FunctionCode extends ByteCode {
 
 private String name, start, end;

 @Override
    public void execute(VirtualMachine vm) {
        DebugVirtualMachine dvm = (DebugVirtualMachine) vm;
        dvm.setFunc(name, getStart(), getEnd());
    }

    @Override
    public void init(Vector<String> args){
        name = args.get(0);
        start = args.get(1);
        end = args.get(2);
    }

    @Override
    public String toString() {
        return "FUNCTION "+name+" "+getStart()+" "+getEnd();
    }

    public int getStart() {
        return Integer.parseInt(start);
    }

    public int getEnd() {
        return Integer.parseInt(end);
    }
}
