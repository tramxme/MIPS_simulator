import java.util.*;
import java.io.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;



public class ToBin {
   private OutputStream out = null;

   private String filePath;
   // constructor with given path
   public ToBin(String path){
      File f = new File(path);
      filePath = path;
      if(f.exists() && !f.isDirectory()) {
         f.delete();
      }
      try {
         out = new FileOutputStream(path,true);
      } catch (FileNotFoundException e) {
         System.out.println("Unable to write to " + path + ".\n");
         e.printStackTrace();
      }
   }
   // constructor creating generic file name
   public ToBin(){
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
   // this does the real work pulling apart the instruction translates to bin data and saves
   public void writeToBin(String ins){
      int bytes = 0;
      Instruction.OpCode o = null;
      Instruction.Register r;
      String opCode = ins.trim();
      String registers = "";
      String[] reg = {};
      Integer immd = 0;


      if (ins.contains("$")){
         opCode = ins.substring(0, ins.indexOf("$")).trim();
         registers = ins.substring(ins.indexOf("$"), ins.length()).trim();
         reg = registers.split(",");
         for (int i = 0; i < reg.length; i++){
            reg[i] = reg[i].trim();
         }
      }
      else if (ins.contains(".byte") || ins.contains(".word")){
         ins.trim();
         reg = ins.split("\\s+");
         opCode = reg[1];
         opCode.trim();

         registers = reg[2];
         registers.trim();
      }
      o = Instruction.getCode(opCode);
      if(opCode.equals(".byte") || opCode.equals(".word")){
         try{
            bytes = Integer.parseInt(registers);
            o = null;
         }
         catch(Exception e){
            if(registers.contains("0x")){
               registers = registers.replace("0x","");
               bytes = Integer.parseInt(registers, 16);
               o = null;
            }
         }
      }

      if (o != null){
         if ((o == Instruction.OpCode.SLL || o == Instruction.OpCode.SRA || o == Instruction.OpCode.SRL || o == Instruction.OpCode.ADDI ||
                  o == Instruction.OpCode.ADDIU || o == Instruction.OpCode.ORI || o == Instruction.OpCode.SLTIU || o == Instruction.OpCode.LUI)){
            String num = reg[o.params - 1].trim();
            try {
               immd = Integer.parseInt(num);
            }
            catch(Exception e) {
               immd = Label.labelTable.get(num);
            }
                  }

         if (o.type == Instruction.Type.RType){
            bytes |= o.hex;
            bytes |= (immd & 0x3F) << 6;
         }
         else if (o.type == Instruction.Type.JType){
            bytes |= o.hex << 26;
            bytes |= immd & 0x3FFFFFF;
         }
         else {
            bytes |= o.hex << 26;
            bytes |= immd & 0xFFFF;
         }
         for(int i = 0; i < reg.length; i++){
            r = Instruction.getRegister(reg[i]);
            if (r != null){
               bytes |= r.val << (21 - 5 * i);
            }
         }
      }
      for (int i = 0; i < 4; i++){
         try {
            out.write(bytes >>> (24 - i * 8));
         } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
      Integer binCom = bytes;
      System.out.println("binary output: " + String.format("%8s",(Integer.toHexString(binCom))).replace(" ","0") + "\n");
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
