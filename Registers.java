
public class Registers {
	private Integer[] registers = new Integer[32];
	private Boolean[] flags = new Boolean[32];

	public Registers() {
		for (int i = 0; i < 32; i++) {
			registers[i] = 0;
			flags[i] = false;
		}
	}
	public Integer read(Integer address) {
		return registers[address];
	}
	public void write(Integer address, Integer value) {
		//System.out.printf("%8X - value", address, value);
		registers[address] = value;
	}
	public void printregs() {
		for (int i = 0; i < 32; i++) {
			System.out.printf("%8X " ,registers[i]);
		}
		System.out.println();
	}
	public Boolean checkFlag(Integer addr) {
		return flags[addr];
	}
	public void setFlag(Integer addr) {
		flags[addr] = true;
	}
	public void clearFlag(Integer addr) {
		flags[addr] = false;
	}
	public void clearAllFlags() {
		for (int i = 0; i < 32; i++) {
			flags[i] = false;
		}
	}
}
