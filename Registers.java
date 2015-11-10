
public class Registers {
	public static int[] registers = new int[32];
	
	public int read(int address) {
		return registers[address];
	}
	public void write(int value, int address) {
		registers[address] = value;
	}
}
