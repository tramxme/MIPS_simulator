import java.util.*;



public class ALU {
	public static int exe, writes, mem;
	private static int writeVal = 0, writeType = 0, writeAddress = 0;
	
	public ALU(){
		exe = 0;
		writes = 0;
		mem = 0;
	}
	
	
	public static int Execute (Parser cParser, Registers regs, int lastPC, int currPC, Boolean startFlag, Boolean runFlag) {
		
		int ret = currPC;
		Integer opCode, rs, rt, immd, index, funCode;
		Instruction.Type type;
		opCode = cParser.opcode;
		type = cParser.type;
		int currIns = cParser.currIns;
		
		if (currIns != 0 && (runFlag || startFlag)){
			exe++;
			if (type == Instruction.Type.RType) {
				rs = regs.read(cParser.rs);
				rt = regs.read(cParser.rt);
				funCode = cParser.funct;
				writeType = 1;
				writeAddress = cParser.rd;
				
				switch (funCode) {
				//add
				case 0x20: writeVal = rs + rt;
					break;
				//add unsigned virtually identical because of 64 bit 
				case 0x21: writeVal = rs + rt;
					break;
				//subtract
				case 0x22: writeVal = rs - rt;
					break;
				//subtract unsgned see addu above
				case 0x23: writeVal = rs - rt;
					break;
				//and
				case 0x24: writeVal = rs & rt;
					break;
				case 0x8: 
					ret = rs;
					writeType = 0;
					writeAddress = 0;
					writeVal = 0;
					cParser.flush(runFlag, startFlag);
					regs.clearAllFlags();
	
					break;
				case 0x25: writeVal = rs | rt;
					break;
				case 0x0: writeVal = rt << cParser.shamt;
					break;
				case 0x2B: writeVal = (rs < rt)? 1:0;
					break;
				case 0x03: writeVal = rt >> cParser.shamt;
					break;
				case 0x02: writeVal = rt >>> cParser.shamt;
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
				writeType = 1;
				writeAddress = cParser.rt;
				
				switch (opCode){
				case 0x08: writeVal = rs + immd;
					break;
				case 0x09: writeVal = rs + immd;
					break;
				case 0x0C: writeVal = rs & immd;
					break;
				case 0x04:
					writeType = 0;
					writeAddress = 0;
					writeVal = 0;
					if (rs == rt){
					
						ret += (immd << 2) - 4;
						
						cParser.flush(runFlag, startFlag);
						regs.clearAllFlags();
					}
					break;
				case 0x05: 
					writeType = 0;
					writeAddress = 0;
					writeVal = 0;
					if (rs != rt) {
						ret += (immd << 2) - 4;
						
						cParser.flush(runFlag, startFlag);
						regs.clearAllFlags();
					}
					break;
				//lw
				case 0x23: 
					mem++;
					writeVal = Memory.getData(rs + immd);
					break;
				case 0x0D: writeVal = rs | immd;
					break;
				case 0x0B: writeVal =(rs < immd)?1:0;
					break;
				case 0x2B:
						mem++;
						writeType = 2;
						writeVal = rt;
						writeAddress = rs + immd;
					break;
					//lui
				case 0x0F: writeVal = immd << 16;
					break;
				
				}
			}
			else if (type == Instruction.Type.JType){
				index = cParser.index;
				switch (opCode){
				case 0x02: ret = index;
					writeType = 0;
					writeAddress = 0;
					writeVal = 0;
					cParser.flush(runFlag, startFlag);
					regs.clearAllFlags();
					break;
				case 0x03: regs.write(31, ret);
					ret = index;
					writeType = 0;
					writeAddress = 0;
					writeVal = 0;
					cParser.flush(runFlag, startFlag);
					regs.clearAllFlags();
					break;
					
				}
				
			}
		}
		return ret;
		
	}
	
	public static void writeBack (Registers regs, Boolean runFlag, Boolean startFlag, Parser cParser) {
		if (writeType == 1) {
			writes++;
			//regs.printregs();
			startFlag = false;
			regs.write(writeAddress, writeVal);
			regs.clearFlag(writeAddress);
			//regs.printregs();
			
			if (!runFlag) {
				if (cParser.type == Instruction.Type.RType){
					if (!(regs.checkFlag(cParser.rs)) && !(regs.checkFlag(cParser.rt))) {
						runFlag = true;
					}
				}
				else if (cParser.type == Instruction.Type.IType) {
					if (!(regs.checkFlag(cParser.rs))) {
						if (cParser.opcode == 0x4 || cParser.opcode == 0x5){
							if (!(regs.checkFlag(cParser.rt))) {
								runFlag = true;
							}
						}
						else {
							runFlag = true;
						}
					}
				}
			}
		}
		else if (writeType == 2) {
			Memory.addDataToMem(writeVal, writeAddress);
		}
	}
}
