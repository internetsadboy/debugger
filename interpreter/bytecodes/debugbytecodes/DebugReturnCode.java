package interpreter.bytecodes.debugbytecodes;

import interpreter.bytecodes.ReturnCode;
import interpreter.*;
import interpreter.debugger.DebugVirtualMachine;

public class DebugReturnCode extends ReturnCode{
  @Override
  public void execute(VirtualMachine vm) {
    DebugVirtualMachine dvm = (DebugVirtualMachine) vm;
    if(dvm.isTraceOn()) {
      String name = "";
      for (int i = 0; i < dvm.getFERsize(); i++) {
          name += "  ";
      }
      name += "exit: " + dvm.getFuncName() + ": " + val;
      dvm.addTrace(name);
    }
    super.execute(dvm);
    dvm.endFER();
  }
}
