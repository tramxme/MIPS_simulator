import java.util.*;
import java.io.*;

public class Validate {

   private String opCode;
   private String registers;
   private String[] reg;
   private Instructions InsTable = new Instructions();

   public Validate(String ins) {
      if(ins.contains("$")){
          opCode = ins.substring(0, ins.indexOf("$"));
          registers = ins.substring(ins.indexOf("$"), ins.length());
          reg = registers.split(",");
      }
   }

   public int CheckSyntax() {

      //If destination and source are not registers
      if(!reg[0].contains("$") || !reg[1].contains("$")) {
         return 0;
      }

      //Check register
      if (InsTable.opCodes.get(opCode.trim()) != null) {
         if (InsTable.opCodes.get(opCode.trim()) & 0x20 == 0x20 && !reg[2].contains("$")) {
            return 0;
         }

      if (InsTable.opCodes.get(opCode.trim()) & 0x08 == 0x08 && Integer.parseInt(reg[2])) {
            return 0;
         }
      }

      //Check Immediate

      return 1;
   }
}
