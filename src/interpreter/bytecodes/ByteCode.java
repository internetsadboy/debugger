package interpreter.bytecodes;

import java.util.*;
import interpreter.*;

public abstract class ByteCode {
    public abstract void execute(VirtualMachine vm);
    public abstract void init(Vector<String> args);
}
