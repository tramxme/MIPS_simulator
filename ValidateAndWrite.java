import java.util.*;
import java.io.*;

public class ValidateAndWrite{
   private OutputStream out = null;
   public Memory mem = new Memory();
   public int pcCount = 0;
   private String filePath;

   public ValidateAndWrite() {
      File f = new File("a.out");
      filePath = "a.out";
      if(f.exists() && !f.isDirectory()) {
         f.delete();
      }
      try {
         out = new FileOutputStream("a.out",true);
      } catch (FileNotFoundException e) {
         System.out.println("Unable to write to a.out\n");
         e.printStackTrace();
      }
   }


   public boolean CheckSyntax(String ins, int lineNum) {
      String opCode, label, register;
      String[] params;
      Instruction.OpCode o = null;
      int immd = 0, address, bytes = 0, sa = 0;
      Instruction.Register rs = null, rt = null, rd = null;
      ins = ins.trim();

      //.WORD & .BYTE - The data segment is at the bottom of the memory
      if(ins.contains(".byte") || ins.contains(".word")){
         String value = ins.split("\\s+")[1].trim();
         try{
            immd = Integer.parseInt(value);
            mem.addInstruction(immd, lineNum, true);
            System.out.printf("bytes 0x%8X\n", bytes);
            out.write(bytes);
            return true;
         }
         catch(Exception e){
            if(value.contains("0x")){
               value = value.replace("0x","");
               try{
                  immd = Integer.parseInt(value, 16);
                  mem.addInstruction(immd, lineNum, true);
                  System.out.printf("bytes 0x%8X\n", bytes);
                  out.write(bytes);
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
      else {
         ins = ins.replaceAll(",", " ");
         ins = ins.replaceAll("\\(", " ");
         ins = ins.replaceAll("\\)", " ");
         ins = ins.trim();
         params = ins.split("\\s+");

         opCode = params[0];
         o = Instruction.getCode(opCode);

         //System.out.println("opcode " + opCode);
         //System.out.println("params 1 " + params[1]);
         //System.out.println("params 2 " + params[2]);
         //System.out.println("params 3 " + params[3]);
         // System.out.println("rt " + rt.val);
         //System.out.printf("bytes 0x%8X", bytes);
         //System.out.print("here");

         if (!opCode.equals("move") && o == null){
            return false;
         }

         switch(opCode){
            case "move":
               //System.out.print(params[1] + " " + params[2]);
               rd = Instruction.getRegister(params[1].trim());
               rs = Instruction.Register.zero;
               rt = Instruction.getRegister(params[2].trim());

               if (rd == null || rs == null || rt == null){
                  return false;
               }
               bytes = rs.val << 21;
               bytes |= rt.val << 16;
               bytes |= rd.val << 11;
               bytes |= Instruction.OpCode.ADDU.hex;
               mem.addInstruction(bytes, lineNum, false);
               System.out.printf("bytes 0x%8X\n", bytes);
               try {
                  out.write(bytes);
               }
               catch (IOException e){
                  e.printStackTrace();
               }
               return true;


               //JUMP & JAL instruction - Check if the label is valid
            case "jal": case "j":
               label = params[1].trim();
               if(Label.hasLabel(label)){
                  //Get the address of the label
                  address = Label.getAddress(label);
                  int offset = address - lineNum;
                  System.out.printf("jump to : " + (offset * 4 + Memory.getCurrentPC()));

                  bytes = o.hex << 26;
                  bytes |= (0x03FFFFFF & (offset * 4 + Memory.getCurrentPC()));
                  mem.addInstruction(bytes, lineNum, false);
                  System.out.printf("bytes 0x%8X\n", bytes);
                  try {
                     out.write(bytes);
                     return true;
                  }
                  catch (IOException e){
                     e.printStackTrace();
                     return false;
                  }
               }
               else {
                  return false;
               }

            case "lw": case "sw":
               //Check if the first parameter is a valid register
               if (!params[1].contains("$")){
                  return false;
               }
               else {
                  register = params[1].trim();
                  rt = Instruction.getRegister(register);
                  if (rt == null){
                     return false;
                  }

                  //Check if the second parameter is a register or label or immediate
                  if (!params[2].contains("$")){
                     //System.out.println("label " + params[2]);
                     if (params[2].contains("Ox")){
                        params[2] = params[2].replace("0x", "");
                        immd = Integer.parseInt(params[2], 16);
                        rs = Instruction.getRegister(params[3].trim());
                     }

                     try {
                        immd = Integer.parseInt(params[2].trim());
                        rs = Instruction.getRegister(params[3].trim());
                     }
                     catch(Exception c){
                     }

                     //If it's a label, break it into 2 instructions
                     label = params[2].trim();
                     if(Label.hasLabel(label)){
                        address = Label.getAddress(label);


                        bytes = Instruction.OpCode.LUI.hex << 26;
                        bytes |= Instruction.Register.at.val << 16;
                        bytes |= (0xFFFF & ((address - lineNum) * 4 + Memory.getCurrentPC()));

                        mem.addInstruction(bytes, lineNum, false);
                        System.out.printf("bytes 0x%8X\n", bytes);
                        try {
                           out.write(bytes);
                        }
                        catch (IOException a){
                           a.printStackTrace();
                        }
                        bytes = o.hex << 26;
                        bytes |= Instruction.Register.at.val << 21;
                        bytes |= rt.val << 16;
                        mem.addInstruction(bytes, lineNum, false);
                        System.out.printf("bytes 0x%8X\n", bytes);

                        try {
                           out.write(bytes);
                        }
                        catch (IOException x){
                           x.printStackTrace();
                        }
                        return true;
                     }
                     else {
                        return false;
                     }
                  }
                  //If it contains register then, immediate is 0
                  else {
                     immd = 0;
                     rs = Instruction.getRegister(params[2].trim());
                  }

                  if (rs == null) {
                     return false;
                  }
                  else {
                     bytes = o.hex << 26;
                     bytes |= rs.val << 21;
                     bytes |= rt.val << 16;
                     bytes |= (0xFFFF & immd);
                     mem.addInstruction(bytes, lineNum, false);
                     System.out.printf("bytes 0x%8X\n", bytes);
                     try {
                        out.write(bytes);
                     }
                     catch (IOException e){
                        e.printStackTrace();
                     }
                  }
                  return true;
               }

               //SYSCALL
            case "syscall":
               bytes = o.hex;
               mem.addInstruction(bytes, lineNum, false);
               System.out.printf("bytes 0x%8X\n", bytes);
               try {
                  out.write(bytes);
               }
               catch (IOException e){
                  e.printStackTrace();
               }
               return true;


            case "add": case "addu": case "and": case "or": case "sub": case "subu": case "sltu":
               rd = Instruction.getRegister(params[1].trim());
               rs = Instruction.getRegister(params[2].trim());
               rt = Instruction.getRegister(params[3].trim());
               if (rd == null || rs == null || rt == null){
                  return false;
               }
               bytes = rs.val << 21;
               bytes |= rt.val << 16;
               bytes |= rd.val << 11;
               bytes |= o.hex;
               mem.addInstruction(bytes, lineNum, false);
               System.out.printf("bytes 0x%8X\n", bytes);
               try {
                  out.write(bytes);
               }
               catch (IOException e){
                  e.printStackTrace();
               }
               return true;

            case "sll": case "sra": case "srl":
               rd = Instruction.getRegister(params[1].trim());
               rt = Instruction.getRegister(params[2].trim());
               sa = 0;
               try {
                  sa = Integer.parseInt(params[3].trim());
               }
               catch(Exception e){
                  return false;
               }
               if (rd == null || rt == null){
                  return false;
               }
               bytes = rt.val << 16;
               bytes |= rd.val << 11;
               bytes |= sa << 6;
               bytes |= o.hex;

               mem.addInstruction(bytes, lineNum, false);
               System.out.printf("bytes 0x%8X\n", bytes);
               try {
                  out.write(bytes);
               }
               catch (IOException e){
                  e.printStackTrace();
               }
               return true;


            case "addi": case "addiu": case "andi": case "ori":
               rt = Instruction.getRegister(params[1].trim());
               rs = Instruction.getRegister(params[2].trim());
               immd = 0;
               try {
                  immd = Integer.parseInt(params[3].trim());
               }
               catch(Exception e){
                  if(params[3].contains("0x")){
                     params[3] = params[3].replace("0x","").trim();
                  }

                  try{
                     immd = Integer.parseInt(params[3], 16);
                  }
                  catch (Exception f){
                     return false;
                  }
               }

               if (rt == null || rs == null){
                  return false;
               }

               bytes = o.hex << 26;
               bytes |= rs.val << 21;
               bytes |= rt.val << 16;
               bytes |= (0xFFFF & immd);

               mem.addInstruction(bytes, lineNum, false);
               System.out.printf("bytes 0x%8X\n", bytes);
               try {
                  out.write(bytes);
               }
               catch (IOException e){
                  e.printStackTrace();
               }
               return true;


            case "bne": case "beq":
               //System.out.print(params[1] + " " + params[2] + " " + params[3]);
               rs = Instruction.getRegister(params[1].trim());
               rt = Instruction.getRegister(params[2].trim());
               label = params[3].trim();

               if(!Label.hasLabel(label)){
                  return false;
               }
               else {
                  address = Label.getAddress(label);
               }

               if(rt == null || rs == null){
                  return false;
               }

               bytes = o.hex << 26;
               bytes |= rs.val << 21;
               bytes |= rt.val << 16;
               bytes |= (0xFFFF & (address - lineNum - 1));

               mem.addInstruction(bytes, lineNum, false);
               System.out.printf("bytes 0x%8X\n", bytes);
               try {
                  out.write(bytes);
               }
               catch (IOException e){
                  e.printStackTrace();
               }
               return true;

            case "jr":
               rs = Instruction.getRegister(params[1].trim());

               bytes |= rs.val << 21;
               bytes |= o.hex;

               mem.addInstruction(bytes, lineNum, false);
               System.out.printf("bytes 0x%8X\n", bytes);
               try {
                  out.write(bytes);
               }
               catch (IOException e){
                  e.printStackTrace();
               }
               return true;
         }
      }
      return true;
   }

   public void closeFile (boolean valid){
      try {
         out.close();
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      if (valid){
         System.out.println(filePath + " Binary File Written\n");
      }
      else {
         File f = new File(filePath);
         if(f.exists() && !f.isDirectory()) {
            f.delete();
         }
      }
   }
}
