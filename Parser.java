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
	public int currIns;
	// Parsed JType instruction: opcode, index
	public int index; // 26-bits
	public int instruction;
	public static int fetches;
	public static int decodes;
	
	public Parser(){
		fetches = 0;
		decodes = 0;
		currIns = 0;
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
	
	public void fetch(Integer ins) {
		fetches++;
		instruction = ins;
	}
	
	public int decode(int PC, Boolean runFlag, Boolean startFlag, Registers regs) {
		
		if (runFlag || startFlag) {
			currIns = instruction;
			opcode = instruction >>> 26;
			type = checkType(opcode);
			if (type == Instruction.Type.RType) {
				rs = instruction >>> 21 & 0x1F;
				rt = instruction >>> 16 & 0x1F;
				rd = instruction >>> 11 & 0x1F;
				shamt = instruction >>> 6 & 0x1F;
				funct = instruction & 0x3F;
				if (regs.checkFlag(rs) || regs.checkFlag(rt)) {
					runFlag = false;
				}
				else {
					if (rd != 0)
						regs.setFlag(rd);
				}
			}
			else if (type == Instruction.Type.IType) {
				rs = instruction >>> 21 & 0x1F;
				rt = instruction >>> 16 & 0x1F;
				immediate = instruction & 0x0000FFFF;
				if (opcode == 0x4 || opcode == 0x5) {
					if (regs.checkFlag(rt) || regs.checkFlag(rs)) {
						runFlag = false;
					}	
				}
				else {
					if (regs.checkFlag(rs))
						runFlag = false;
					else
						regs.setFlag(rt);
				}
			}
			else {
				index = instruction & 0x3FFFFFF;
			}
			decodes++;
			PC += 4;
		}
		return PC;
	}
	
	public void flush(Boolean runFlag, Boolean startFlag) {
		runFlag = false;
		startFlag = true;
		opcode = 0; // 6-bits
		rs = 0; // 5-bits
		rt = 0; // 5 bits
		rd = 0; // 5 bits
		shamt = 0; // 5-bits
		funct = 0; // 6-bits
		immediate = 0; // 16-bits
		index = 0; // 26-bits
		instruction = 0;
		currIns = 0;
	}
}