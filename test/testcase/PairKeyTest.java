package testcase;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import es.utils.doublekeymap.PairKey;

public class PairKeyTest {

	@Test
	public void shouldReturnTrueForSameInstance() {
		PairKey<String,String> pk1 = new PairKey<String, String>("Pippo","Paperino");
		assertThat(pk1.equals(pk1)).isTrue();
	}
	@Test
	public void shouldReturnFalseForSecondParameterNull() {
		PairKey<String,String> pk1 = new PairKey<String, String>("Pippo","Paperino");
		assertThat(pk1.equals(null)).isFalse();
	}
	@Test
	public void shouldReturnFalseForOtherClassInstance() {
		PairKey<String,String> pk1 = new PairKey<String, String>("Pippo","Paperino");
		assertThat(pk1.equals(new Object())).isFalse();
	}
	@Test
	public void shouldReturnFalseForDifferentValues() {
		PairKey<String,String> pk1 = new PairKey<String, String>("Pippo","Paperino");
		PairKey<String,String> pk2 = new PairKey<String, String>("Pippo2","Paperino");
		PairKey<String,String> pk3 = new PairKey<String, String>("Pippo","Paperino2");
		PairKey<String,String> pk4 = new PairKey<String, String>("Pippo2","Paperino2");
		assertThat(pk1.equals(pk2)).isFalse();
		assertThat(pk1.equals(pk3)).isFalse();
		assertThat(pk1.equals(pk4)).isFalse();
	}
	@Test
	public void shouldReturnTrueForSameValues() {
		PairKey<String,String> pk1 = new PairKey<String, String>("Pippo","Paperino");
		PairKey<String,String> pk2 = new PairKey<String, String>("Pippo","Paperino");
		assertThat(pk1.equals(pk2)).isTrue();
	}

}
