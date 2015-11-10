
public class Registers {
	public static Integer[] registers = new Integer[32];

	public Integer read(Integer address) {
		return registers[address];
	}
	public void write(Integer value, Integer address) {
		registers[address] = value;
	}
}
