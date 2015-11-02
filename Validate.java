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
      else if (ins.contains(".byte") || ins.contains(".word")){
    	  ins.trim();
    	  reg = ins.split("\\s+");
    	  opCode = reg[1];
    	  opCode.trim();
    	  
    	  registers = reg[2];
    	  registers.trim();
    	  
      }
   }

   public boolean CheckSyntax() {
	   System.out.println(opCode);
	   if(opCode.equals(".byte") || opCode.equals(".word")){
  		 System.out.println(opCode);
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
         if (o.type == Instruction.Type.IType){
            String num = reg[o.params - 1].trim();
            System.out.println(num);
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
