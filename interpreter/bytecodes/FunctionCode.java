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
       // if(name.toLowerCase().equals("read") || name.toLowerCase().equals("write")) {
       //     dvm.setIntrinsic(true);
        //} else {
            dvm.setFunc(name, getStart(), getEnd());    
        //}
        
        if (dvm.isTraceOn()) {
            String dontTrace = name.toLowerCase();
            if(!dontTrace.equals("main") && !dontTrace.equals("read") && !dontTrace.equals("write")) {
                String function = "";
                for (int i = 0; i < dvm.getFERsize(); i++) {
                    function += "  ";
                } 
                function += name + "( ";
                int pc = dvm.getPC() + 1;
                ByteCode code = dvm.getCode(pc);
                while (code instanceof FormalCode) {
                    function += dvm.getValue(dvm.getRunStackSize() - Integer.parseInt(((FormalCode) code).getOffset())) + " ";
                    code = dvm.getCode(++pc);
                }
                dvm.addTrace(function + ")");
            } 
        }
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
