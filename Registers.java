
public class Registers {
	public static Integer[] registers = new Integer[32];

	public Registers() {
		for (int i = 0; i < 32; i++) {
			registers[i] = 0;
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
}
