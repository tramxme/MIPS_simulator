public enum OpCode {
   //RType
   ADD ("add", Type.RType, 0x20),
   ADDU ("addu", Type.RType, 0x21),
   AND ("and", Type.RType, 0x21),
   JR ("jr", Type.RType, 0x08),
   OR ("or", Type.RType, 0x25),
   SLL ("sll", Type.RType, 0x00),
   SLTU ("sltu", Type.RType, 0x2B),
   SRA ("sra", Type.RType, 0x03),
   SRL ("srl", Type.RType, 0x02),
   SUB ("sub", Type.RType, 0x22),
   SYSCALL ("syscall", Type.RType, 0x0C),

   //IType
   ADDI ("addi", Type.IType, 0x08),
   ADDIU ("addiu", Type.IType, 0x09),
   ANDI ("andi", Type.IType, 0x0C),

   BEQ ("beq", Type.IType, 0x04),
   BNE ("bne", Type.IType, 0x05),
   LW ("lw", Type.IType, 0x23),
   ORI ("ori", Type.IType, 0x0D),
   SLTIU ("sltiu", Type.IType, 0x0B),
   SW ("sw", Type.IType, 0x2B),
   LUI ("lui", Type.IType, 0x0F),

   //JType
   JAL ("jal", Type.JType, 0x03),
   J ("j", Type.JType, 0x02);

   private final String name;
   private final Type type;
   private final Integer hex;

   OpCode(String name, Type type, Integer hex){
      this.name = name;
      this.type = type;
      this.hex = hex;
   }
}

