public class Parser {
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

	public Parser(){
	}

	public int getOpCode(Integer ins) {
		return ins >>> 26;
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

	public void parse(Integer ins) {
		opcode = getOpCode(ins);
		type = checkType(opcode);
		if (type == Instruction.Type.RType) {
			rs = ins >>> 21 & 0x1F;
			rt = ins >>> 16 & 0x1F;
			rd = ins >>> 11 & 0x1F;
			shamt = ins >>> 6 & 0x1F;
			funct = ins & 0x3F;
		}
		else if (type == Instruction.Type.IType) {
			rs = ins >>> 21 & 0x1F;
			rt = ins >>> 16 & 0x1F;
			immediate = ins & 0x0000FFFF;
		}
		else {
			index = ins & 0x3FFFFFF;
		}
	}
}
