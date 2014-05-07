package interpreter;

import interpreter.bytecodes.*;
import java.util.*;
import java.io.*;

public class ByteCodeLoader {

    String srcFile = "";

    public ByteCodeLoader(String file) throws IOException {
        srcFile = System.getProperty("user.dir") + "/" + file;
    }

    public Program loadCodes() throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, FileNotFoundException {
        
        BufferedReader in = new BufferedReader(new FileReader(srcFile));
        Vector<String> bcInstance = new Vector<String>();
        Program program = new Program();
        ByteCode byteCode;
        int position = 0;

        do {
            String codeLine = in.readLine();
            Scanner tokenizer = new Scanner(codeLine);
            String code = tokenizer.next();
            String codeClass = CodeTable.get(code);
            // identify debug bcs
            if (codeClass.startsWith("Debug")) {
                byteCode = (ByteCode)(Class.forName("interpreter.bytecodes.debugbytecodes." + codeClass).newInstance());
            } else {
                byteCode = (ByteCode)(Class.forName("interpreter.bytecodes." + codeClass).newInstance());
            }
            while (tokenizer.hasNext()) {
                bcInstance.add(tokenizer.next());
            }
            byteCode.init(bcInstance);
            program.addByteCode(byteCode);
            if (codeClass.equals("LabelCode")) {
                program.hashAddress(bcInstance, position);
            }
            bcInstance.clear();
            position++;
        } while (in.ready());
        program.changeAddr();
        return program;
    }

}
