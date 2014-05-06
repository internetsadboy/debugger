package interpreter.debugger;

import interpreter.ByteCodeLoader;
import interpreter.debugger.ui.*;
import interpreter.Interpreter;
import interpreter.Program;
import java.io.IOException;

public class Debugger {

    private DebugUI ui;
    private DebugVirtualMachine dvm;
    private Interpreter interpreter;

    public Debugger(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    public void debugRun(String sourceFile, ByteCodeLoader bcl) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        Program prog = bcl.loadCodes();
        dvm = new DebugVirtualMachine(prog);
        dvm.loadSource(sourceFile);
        ui = new DebugUI(dvm);
        boolean firstInteraction = true;
        do {
            /*
            if(!dvm.isIntrinsic()) {
              if(firstInteraction) {
                ui.dumpSource();
                firstInteraction = false;
              } else {
                ui.displayFunc();        
              }
            } else {
              dvm.setIntrinsic(false);
            }*/
            ui.dumpSource();
            ui.userCommand();
        } while (dvm.getIsRunning());
    }
    
}
