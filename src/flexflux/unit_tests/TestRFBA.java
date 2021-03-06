/*******************************************************************************
 * Copyright INRA
 * 
 *  Contact: ludovic.cottret@toulouse.inra.fr
 * 
 * 
 * This software is governed by the CeCILL license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 * 
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *  In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *  The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 ******************************************************************************/
/**
 * 11 avr. 2013 
 */
package flexflux.unit_tests;

import static org.junit.Assert.fail;
import flexflux.analyses.Analysis;
import flexflux.analyses.FBAAnalysis;
import flexflux.analyses.RFBAAnalysis;
import flexflux.analyses.result.AnalysisResult;
import flexflux.analyses.result.RFBAResult;
import flexflux.general.Bind;
import flexflux.general.CplexBind;
import flexflux.general.GLPKBind;
import flexflux.general.Vars;
import flexflux.interaction.InteractionNetwork;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import parsebionet.biodata.BioEntity;
import parsebionet.biodata.BioNetwork;

/**
 * @author lmarmiesse 11 avr. 2013
 * 
 */
public class TestRFBA extends FFUnitTest{
	
	
	
	static Bind bind;

	static BioNetwork n;
	static InteractionNetwork i;

	@BeforeClass
	public static void init() {
		
		String solver = "GLPK";
		if (System.getProperties().containsKey("solver")) {
			solver = System.getProperty("solver");
		}
		
		try {
			if (solver.equals("CPLEX")) {
				bind = new CplexBind();
			} else {
				bind = new GLPKBind();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail("Solver error");
		}

		Vars.maxThread = 1;

		bind.loadSbmlNetwork("src/flexflux/unit_tests/data/rfba/coli.xml", false);
		n = bind.getBioNetwork();
		i = bind.getInteractionNetwork();

		bind.loadConstraintsFile("src/flexflux/unit_tests/data/rfba/condTestRfba");

//		bind.loadInteractionsFile("Data/intTestRfba");
		
		bind.loadRegulationFile("src/flexflux/unit_tests/data/rfba/rfba.sbml");

		bind.prepareSolver();

	}

	@Test
	public void test() {
		
		go();
	
	}

	public void go() {
		
		

		RFBAAnalysis rfba = new RFBAAnalysis(bind,
				"R_Ec_biomass_iAF1260_core_59p81M", 0.003, 0.1, 150,
				new ArrayList<String>());
		RFBAResult result = rfba.runAnalysis();
		
//		result.plot();
//		int a = 1;
//
//		while (a == 1) {
//
//		}
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					"src/flexflux/unit_tests/data/rfba/rFBATest"));

			String line = in.readLine();

			List<String> entities = new ArrayList<String>();

			for (String s : line.split("\\s+")) {
				if (!s.equals("")) {
					entities.add(s);
				}
			}

			while ((line = in.readLine()) != null) {

				String[] splittedLine = line.split("\t");

				double time = Double.parseDouble(splittedLine[0].replaceAll(
						"\\s+", ""));
				
				Map<String, Double> values = result.getValuesforTime(time);
				
				for (int i = 1; i < splittedLine.length; i++) {

					double trueResult = Double.parseDouble(splittedLine[i]
							.replaceAll("\\s+", ""));
					
					double resultToTest = values.get(entities.get(i - 1));
					
					Assert.assertTrue(Math.abs(trueResult - resultToTest) < 0.001);

				}

			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
