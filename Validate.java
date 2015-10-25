import java.util.*;
import java.io.*;

public class Validate {

   private String opCode;
   private String registers = "";
   private String[] reg = {};

   public Validate(String ins) {
      opCode = ins.trim();
      if(ins.contains("$")){
          opCode = ins.substring(0, ins.indexOf("$")).trim();
          registers = ins.substring(ins.indexOf("$"), ins.length()).trim();
          reg = registers.split(",");
      }
   }

   public boolean CheckSyntax() {
       if(Instruction.getCode(opCode) == null) {
         return false;
      }
      else {
         Instruction.OpCode o = Instruction.getCode(opCode);

         //Check if it has enough arguments
         if(reg.length != o.params) {
            return false;
         }
         //If destination and source are not registers
         else if(o.params >= 2 && (!reg[0].contains("$") || !reg[1].contains("$"))) {
            return false;
         }
         //If add, addu, and, or, sltu, sub and the 3rd value is not a register
         else if ((o == Instruction.OpCode.ADD ||
                     o == Instruction.OpCode.ADDU ||
                     o == Instruction.OpCode.AND ||
                     o == Instruction.OpCode.OR ||
                     o == Instruction.OpCode.SLTU ||
                     o == Instruction.OpCode.SUB ) && !reg[2].contains("$"))
               {
                  return false;
               }
               //If sll, sra, srl, addi, addiu, andi, ori, sltiu, lui
               else if ((o == Instruction.OpCode.SLL ||
                         o == Instruction.OpCode.SRA ||
                         o == Instruction.OpCode.SRL ||
                         o == Instruction.OpCode.ADDI ||
                         o == Instruction.OpCode.ADDIU ||
                         o == Instruction.OpCode.ORI ||
                         o == Instruction.OpCode.SLTIU ||
                         o == Instruction.OpCode.LUI)
                        && reg[o.params - 1].matches("[^0-9]")){
                  return false;
               }
      }
      return true;
   }
}
