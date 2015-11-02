import java.util.*;
import java.io.*;

public class Validate {

   private Instruction.OpCode o;
   private String opCode;
   private String registers = "";
   private String[] reg = {};
   private Integer immd;

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
         o = Instruction.getCode(opCode);

         //Check if it has enough arguments
         if(reg.length != o.params) {
            return false;
         }

         //If destination and source are not registers
         if(o.params >= 2 && (!reg[0].contains("$") || !reg[1].contains("$"))) {
            return false;
         }

         //If add, addu, and, or, sltu, sub and the 3rd value is not a register
         if ((o == Instruction.OpCode.ADD || o == Instruction.OpCode.ADDU || o == Instruction.OpCode.AND || o == Instruction.OpCode.OR ||
                  o == Instruction.OpCode.SLTU || o == Instruction.OpCode.SUB) && !reg[2].contains("$"))
         {
            return false;
         }

         //If sll, sra, srl, addi, addiu, andi, ori, sltiu, lui
         if ((o == Instruction.OpCode.SLL || o == Instruction.OpCode.SRA || o == Instruction.OpCode.SRL || o == Instruction.OpCode.ADDI ||
                  o == Instruction.OpCode.ADDIU || o == Instruction.OpCode.ORI || o == Instruction.OpCode.SLTIU || o == Instruction.OpCode.LUI)){
            String num = reg[o.params - 1].trim();

            //TODO: match hex value and range and return immd in integer format.Do try and catch loop
            //TODO: lw

            if(!num.matches("[-+]?\\d*")){
               return false;
            }else{
               immd = Integer.parseInt(num);

               //Immediates are limited to 16 bits ( -32768 -> 32767)
               if(immd < -32768 || immd > 32767){
                  return false;
               }
            }
                  }
      }
      return true;
   }

   public Instruction.OpCode getOpCode() {
      return o;
   }

   public Integer getImmediate() {
      return immd;
   }
}
