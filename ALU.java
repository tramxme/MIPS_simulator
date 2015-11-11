import java.util.*;



public class ALU {
	
	
	public static int runCom (Parser cParser, Registers regs, int lastPC, int currPC) {
		int ret = currPC;
		Integer opCode, rs, rt, immd, index, funCode;
		Instruction.Type type;
		opCode = cParser.opcode;
		type = cParser.type;
		if (type == Instruction.Type.RType) {
			rs = regs.read(cParser.rs);
			rt = regs.read(cParser.rt);
			funCode = cParser.funct;
			
			
			switch (funCode) {
			//add
			case 0x20: regs.write(cParser.rd, rs + rt);
				break;
			//add unsigned virtually identical because of 64 bit 
			case 0x21: regs.write(cParser.rd, rs + rt);
				break;
			//subtract
			case 0x22: regs.write(cParser.rd, rs - rt);
				break;
			//subtract unsgned see addu above
			case 0x23: regs.write(cParser.rd, rs - rt);
				break;
			//and
			case 0x24: regs.write(cParser.rd, rs & rt);
				break;
			case 0x8: 
				ret = rs - 4;

				break;
			case 0x25: regs.write(cParser.rd, rs | rt);
				break;
			case 0x0: regs.write(cParser.rd, rt << cParser.shamt);
				break;
			case 0x2B: regs.write(cParser.rd, (rs < rt)? 1:0);
				break;
			case 0x03: regs.write(cParser.rd, rt >> cParser.shamt);
				break;
			case 0x02: regs.write(cParser.rd, rt >>> cParser.shamt);
				break;
			case 0x0C: 		
				if (regs.read(2) == 0xA) {				
					ret = lastPC + 4;
			    }
				break;				
			}
		}
		else if (type == Instruction.Type.IType){
			rs = regs.read(cParser.rs);
			rt = regs.read(cParser.rt);
			immd = (cParser.immediate << 16) >> 16;
			
			switch (opCode){
			case 0x08: regs.write(cParser.rt, rs + immd);
				break;
			case 0x09: regs.write(cParser.rt, rs + immd);
				break;
			case 0x0C: regs.write(cParser.rt, rs & immd);
				break;
			case 0x04: if (rs == rt){
				
					ret += immd << 2;
				}
				break;
			case 0x05: if (rs != rt) {
					ret += immd << 2;
				}
				break;
			//lw
			case 0x23: 
				regs.write(cParser.rt,Memory.getData(rs + immd));
				break;
			case 0x0D: regs.write(cParser.rt, rs | immd);
				break;
			case 0x0B: regs.write(cParser.rt,(rs < immd)?1:0);
				break;
			case 0x2B: Memory.addDataToMem(rt, rs + immd);
				break;
				//lui
			case 0x0F: regs.write(cParser.rt, immd << 16);
				break;
			
			}
		}
		else if (type == Instruction.Type.JType){
			index = cParser.index;
			switch (opCode){
			case 0x02: ret = index - 4;
				break;
			case 0x03: regs.write(31, ret + 4);
				ret = index - 4;
				break;
				
			}
			
		}
		return ret;
	}
}
