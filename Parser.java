public class Parser {
	public Integer instruction;
	public Instruction.Type type;
	// Parsed RType instruction: opcode, rs, rt, rd, shamt, funct
	public int opcode; // 6-bits
	public int rs; // 5-bits
	public int rt; // 5 bits
	public int rd; // 5 bits
	public int shamt; // 5-bits
	public int funct; // 6-bits
	// Parsed IType instruction: opcode, rs, rt, immediate
	public int immediate; // 16-bits
	// Parsed JType instruction: opcode, index
	public int index; // 26-bits
	
	public Parser(Integer ins){
		instruction = ins;
		opcode = instruction >>> 26;
		type = checkType(opcode);
		parse(type);
	}
	
	public Instruction.Type checkType(int opcode) {
		Instruction.Type type;
		if (opcode == 0x00)
			type = Instruction.Type.RType;
		else if (opcode == 0x2 || opcode == 0x3)
			type = Instruction.Type.JType;
		else 
			type = Instruction.Type.IType;
		return type;
	}
	
	public void parse(Instruction.Type type) {
		if (type == Instruction.Type.RType) {
			rs = instruction & 0x3E00000;
			rt = instruction & 0x1F0000;
			rd = instruction & 0xF800;
			shamt = instruction & 0x7C0;
			funct = instruction & 0x3F;
		}
		else if (type == Instruction.Type.IType) {
			rs = instruction & 0x03E00000;
			rt = instruction & 0x001F0000;
			immediate = instruction & 0x0000FFFF;
		}
		else {
			index = instruction & 0x3FFFFFF;
		}
	}
}
