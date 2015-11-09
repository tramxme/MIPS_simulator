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
      else if (ins.contains(".byte") || ins.contains(".word") || ins.contains("jal") || ins.contains("j")){
         ins.trim();
         reg = ins.split("\\s+");
         opCode = reg[1];
         opCode.trim();

         registers = reg[2];
         registers.trim();
         //System.out.println("Ins " + ins + " opCode " + opCode + " registers: " + registers);
      }
   }

   public boolean CheckSyntax() {
      //System.out.println(opCode);
      if(opCode.equals(".byte") || opCode.equals(".word")){
         //System.out.println(opCode);
         try{
            immd = Integer.parseInt(registers);
            return true;
         }
         catch(Exception e){
            if(registers.contains("0x")){
               registers = registers.replace("0x","");
               System.out.println(registers);
               try{
                  immd = Integer.parseInt(registers, 16);
                  return true;
               }catch(Exception f){
                  return false;
               }
            }
            else {
               return false;
            }
         }
      }

      if(Instruction.getCode(opCode) == null) {
         return false;
      }
      else {
         o = Instruction.getCode(opCode);

         //Check special case for LW and SW
         if(o == Instruction.OpCode.LW || o == Instruction.OpCode.SW) {
            //If the 2nd parameter is not a register, need to check if it's in the label
            if (!reg[1].contains("$")){
               return Label.labelTable.containsKey(reg[1].trim());
            }
            //If the 2nd parameter contains a register and an immediate
            else {
               String[] params = reg[1].split("(");
               String immd = params[0].trim();
               String register = params[1].trim();

               if (immd.length() > 0) {
                  try {
                     Integer in = Integer.parseInt(immd);
                  }
                  catch(Exception ex) {
                     return Label.labelTable.containsKey(immd);
                  }
                  return true;
               }
            }
         }

         //Check special case for JType
         if(o == Instruction.OpCode.JAL || o == Instruction.OpCode.J) {
            //If the argument is an address or a label
            try {
               Integer num = Integer.parseInt(registers);
            }
            catch (Exception e) {
               return Label.labelTable.containsKey(registers);
            }
            return true;
         }

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

         if (o.type == Instruction.Type.IType){
            String num = reg[o.params - 1].trim();
            try {
               immd = Integer.parseInt(num);
            }
            catch(Exception e) {
               if(registers.contains("0x")){
                  registers = registers.replace("0x","");
                  try {
                     immd = Integer.parseInt(registers, 16);
                  }
                  catch (Exception f){
                     return false;
                  }
               }
               else {
                  try {
                     immd = Label.labelTable.get(num);
                  }
                  catch (Exception g){
                     return false;
                  }
               }
            }

            //Immediates are limited to 16 bits ( -32768 -> 32767)
            if(immd < -32768 || immd > 32767){
               return false;
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
