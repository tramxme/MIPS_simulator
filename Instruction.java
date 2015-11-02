public class Instruction {
   public enum Register {
      zero ("$zero", "$0", 0),
      at ("$at", "$1", 1),
      v0 ("$v0", "$2", 2),
      v1 ("$v1", "$3", 3),
      a0 ("$a0", "$4", 4),
      a1 ("$a1", "$5", 5),
      a2 ("$a2", "$6", 6),
      a3 ("$a3", "$7", 7),
      t0 ("$t0", "$8", 8),
      t1 ("$t1", "$9", 9),
      t2 ("$t2", "$10", 10),
      t3 ("$t3", "$11", 11),
      t4 ("$t4", "$12", 12),
      t5 ("$t5", "$13", 13),
      t6 ("$t6", "$14", 14),
      t7 ("$t6", "$15", 15),
      s0 ("$t6", "$16", 16),
      s1 ("$t6", "$17", 17),
      s2 ("$t6", "$18", 18),
      s3 ("$t6", "$19", 19),
      s4 ("$t6", "$20", 20),
      s5 ("$t6", "$21", 21),
      s6 ("$t6", "$22", 22),
      s7 ("$t6", "$23", 23),
      t8 ("$t6", "$24", 24),
      t9 ("$t6", "$25", 25),
      k0 ("$t6", "$26", 26),
      k1 ("$t6", "$27", 27),
      gp ("$t6", "$28", 28),
      sp ("$t6", "$29", 29),
      fp ("$t6", "$30", 30),
      ra ("$t6", "$31", 31);

      public final String name1;
      public final String name2;
      public final Integer val;

      Register(String name1, String name2, Integer val) {
         this.name1 = name1;
         this.name2 = name2;
         this.val = val;
      }
   }
   public static Register getRegister(String name){
	   for(Register reg : Register.values()){
		   if(name.equals(reg.name1) || name.equals(reg.name2)){
			   return reg;
		   }
	   }
       return null;
    }

   public enum Type {
      RType, IType, JType;
   }

   public enum OpCode {
      //RType
      ADD ("add", Type.RType, 0x20, 3),
      ADDU ("addu", Type.RType, 0x21, 3),
      AND ("and", Type.RType, 0x21, 3),
      JR ("jr", Type.RType, 0x08, 1),
      OR ("or", Type.RType, 0x25, 3),
      SLL ("sll", Type.RType, 0x00, 3),
      SLTU ("sltu", Type.RType, 0x2B, 3),
      SRA ("sra", Type.RType, 0x03, 3),
      SRL ("srl", Type.RType, 0x02, 3),
      SUB ("sub", Type.RType, 0x22, 3),
      SYSCALL ("syscall", Type.RType, 0x0C, 0),

      //IType
      ADDI ("addi", Type.IType, 0x08, 3),
      ADDIU ("addiu", Type.IType, 0x09, 3),
      ANDI ("andi", Type.IType, 0x0C, 3),
      BEQ ("beq", Type.IType, 0x04, 3),
      BNE ("bne", Type.IType, 0x05, 3),
      LW ("lw", Type.IType, 0x23, 3),
      ORI ("ori", Type.IType, 0x0D, 3),
      SLTIU ("sltiu", Type.IType, 0x0B, 3),
      SW ("sw", Type.IType, 0x2B, 3),
      LUI ("lui", Type.IType, 0x0F, 2),

      //JType
      JAL ("jal", Type.JType, 0x03, 1),
      J ("j", Type.JType, 0x02, 1),
	  WORD (".word",Type.JType, 0x00, 1),
	  BYTE (".byte", Type.JType, 0x00, 1);

      public final String name;
      public final Type type;
      public final Integer hex;
      public final Integer params;

      OpCode(String name, Type type, Integer hex, Integer params){
         this.name = name;
         this.type = type;
         this.hex = hex;
         this.params = params;
      }
   }
   public static OpCode getCode(String opcode) {
      for(OpCode o : OpCode.values()){
         if(opcode.equals(o.name)){
            return o;
         }
      }
      return null;
   }
}
