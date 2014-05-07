package interpreter;

import java.io.*;
import interpreter.debugger.*;
import interpreter.debugger.ui.DebugUI;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <pre>
 *
 *     Interpreter class runs the interpreter:
 *     1. Perform all initializations
 *     2. Load the bytecodes from file
 *     3. Run the virtual machine
 *
 * </pre>
 */
public class Interpreter {

    static ByteCodeLoader bcl;

    public Interpreter(String codeFile, boolean debug) {
        try {
            if (!debug) {
                CodeTable.init();
            } else {
                CodeTable.debugInit();
            }
            bcl = new ByteCodeLoader(codeFile);
        } catch (IOException e) {
            System.out.println("**** " + e);
        }
    }

    void run() {
        try {
            Program program = bcl.loadCodes();
            VirtualMachine vm = new VirtualMachine(program);
            vm.executeProgram();
        } catch (Exception e) {
            System.out.println("**** " + e);
        }
    }

    public static void main(String args[]) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        if (args.length == 0) {
            System.out.println("*** Incorrect usage, try: java interpreter.Interpreter <file>");
            System.exit(1);
        }
        if (args.length == 1) {
            (new Interpreter(args[0], false)).run();
        } 
        if(args.length == 2 && args[0].equals("-d")){
            (new Debugger(new Interpreter(args[1] + ".x.cod", true))).debugRun(args[1] + ".x", bcl);
        }
       
    }
}
