package stsc.general.simulator.multistarter.genetic;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import stsc.common.FromToPeriod;
import stsc.common.algorithms.AlgorithmSettings;
import stsc.common.algorithms.BadAlgorithmException;
import stsc.general.simulator.multistarter.AlgorithmSettingsIteratorFactory;
import stsc.general.simulator.multistarter.BadParameterException;
import stsc.general.simulator.multistarter.MpDouble;
import stsc.general.simulator.multistarter.MpInteger;
import stsc.general.simulator.multistarter.MpString;
import stsc.general.simulator.multistarter.MpSubExecution;
import stsc.general.testhelper.TestStatisticsHelper;
import junit.framework.TestCase;

public class AlgorithmSettingsGeneticListTest extends TestCase {

	private AlgorithmSettingsGeneticList getList() throws BadParameterException {
		final FromToPeriod period = TestStatisticsHelper.getPeriod();
		final AlgorithmSettingsIteratorFactory factory = new AlgorithmSettingsIteratorFactory(period);
		factory.add(new MpInteger("q", -20, 100, 1));
		factory.add(new MpInteger("w", -40, 15, 1));
		factory.add(new MpDouble("a", -60.0, 100.0, 0.15));
		factory.add(new MpDouble("s", -100.0, 101.0, 2.0));
		factory.add(new MpString("z", Arrays.asList(new String[] { "asd", "ibm", "yhoo" })));
		factory.add(new MpString("z", Arrays.asList(new String[] { "vokrug", "fileName" })));
		factory.add(new MpSubExecution("p", Arrays.asList(new String[] { "12313-432423", "234535-23424", "35345-234234135", "24454-65462245" })));
		final AlgorithmSettingsGeneticList mas = factory.getGeneticList();
		return mas;
	}

	public void testAlgorithmSettingsGeneticListGenerateRandom() throws ParseException, BadParameterException, BadAlgorithmException {
		final AlgorithmSettingsGeneticList mas = getList();

		final Set<String> codes = new HashSet<>();
		final int TEST_SIZE = 50000;
		while (codes.size() < TEST_SIZE) {
			for (int i = 0; i < TEST_SIZE; ++i) {
				final StringBuilder b = new StringBuilder();
				mas.generateRandom().stringHashCode(b);
				codes.add(b.toString());
			}
		}
		assertEquals(true, codes.size() >= TEST_SIZE);
	}

	public void testAlgorithmSettingsGeneticListSize() throws ParseException, BadParameterException, BadAlgorithmException {
		final AlgorithmSettingsGeneticList mas = getList();
		assertEquals(17070292800L, mas.size());
	}

	public void testAlgorithmSettingsGeneticListMutate() throws ParseException, BadParameterException, BadAlgorithmException {
		final AlgorithmSettingsGeneticList mas = getList();
		final AlgorithmSettings original = mas.generateRandom();
		final AlgorithmSettings copy = original.clone();

		int i = 0;
		while (true) {
			final StringBuilder originalSb = new StringBuilder();
			final StringBuilder copySb = new StringBuilder();
			mas.mutate(copy);
			original.stringHashCode(originalSb);
			copy.stringHashCode(copySb);
			i += 1;
			if (!originalSb.toString().equals(copySb.toString()))
				break;
		}
		if (i > 2) { // it is highly impossible that two times we will have the
						// same mutation result
			System.out.println(i);
			fail("mutation test failed, there were no mutation");
		}
	}

	public void testAlgorithmSettingsGeneticListMerge() throws ParseException, BadParameterException, BadAlgorithmException {
		final AlgorithmSettingsGeneticList mas = getList();
		final AlgorithmSettings original = mas.generateRandom();
		final AlgorithmSettings copy = original.clone();

		final AlgorithmSettings merge = mas.merge(original, copy);
		assertEquals(merge.getSubExecutions().size(), original.getSubExecutions().size());
		assertEquals(merge.getSubExecutions().get(0), original.getSubExecutions().get(0));
		assertEquals(merge.getInteger("q"), original.getInteger("q"));
		assertEquals(merge.getInteger("w"), original.getInteger("w"));
		assertEquals(merge.getDouble("a"), original.getDouble("a"));
		assertEquals(merge.getDouble("s"), original.getDouble("s"));
		assertEquals(merge.getString("z"), original.getString("z"));
	}
}
