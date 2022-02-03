package it.croway.benchmark;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * Benchmark                   (objects)  Mode  Cnt   Score    Error  Units
 * ArrayMergeBenchmark.array         100  avgt    5  ≈ 10⁻⁵           ms/op
 * ArrayMergeBenchmark.stream        100  avgt    5   0.001 ±  0.001  ms/op
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class ArrayMergeBenchmark {

	@Param({"100"})
	private int objects;

	private Object singleObject;

	private Object[] objectArray;

	public static void main(String[] args) throws RunnerException {

		Options opt = new OptionsBuilder()
				.include(ArrayMergeBenchmark.class.getSimpleName())
				.forks(1)
				.build();

		new Runner(opt).run();
	}

	@Setup
	public void setup() {
		singleObject = UUID.randomUUID();

		objectArray = new Object[objects];
		for (int i = 0; i < objects; i++) {
			objectArray[i] = UUID.randomUUID();
		}
	}

	@Benchmark
	public void stream() {
		Stream.concat(Stream.of(singleObject), Arrays.stream(objectArray)).toArray();
	}

	@Benchmark
	public void array() {
		Arrays.asList(objectArray, singleObject).toArray();
	}
}
