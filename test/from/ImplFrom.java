package from;

public class ImplFrom {

	public static final int staticFinal = 42;
	public static int _static = 42;
	public final int _final = 42;
	
	public From complexValue;
	
	public ImplFrom() {
		this.complexValue = new From("Pippo","Paperino",new From("InnerPippo","InnerPaperino"));
	}
	
}
