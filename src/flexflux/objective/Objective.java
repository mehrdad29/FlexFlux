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
 * 11 mars 2013 
 */
package flexflux.objective;
import parsebionet.biodata.BioEntity;

/**
 * 
 * This class represents the objective function of the FBA.
 * 
 * @author lmarmiesse 11 mars 2013
 * 
 */

public class Objective {

	/**
	 * 
	 * Name of the objective function.
	 * 
	 */
	private String name = "Objective value";

	/**
	 * 
	 * If true, it maximizes the function, if false it minimizes.
	 * 
	 */
	private boolean maximize;

	/**
	 * 
	 * Entities composing the function.
	 * 
	 */
	BioEntity[] entities;

	/**
	 * 
	 * Their coefficients.
	 * 
	 */
	double[] coeffs;

	public Objective() {
		maximize = true;
		entities = new BioEntity[0];
		coeffs = new double[0];
	}

	public Objective(BioEntity[] entities, double[] coeffs, String name,
			boolean maximize) {
		this.name = name;
		this.coeffs = coeffs;
		this.maximize = maximize;
		this.entities = entities;

	}

	public String getName() {
		return name;
	}

	public boolean getMaximize() {
		return maximize;
	}

	public BioEntity[] getEntities() {
		return entities;
	}

	public double[] getCoeffs() {
		return coeffs;
	}

	public String toString() {

		String res = "";

		if (maximize) {
			res += "maximize : ";
		} else {
			res += "minimize : ";
		}

		for (int i = 0; i < entities.length; i++) {

			res += coeffs[i] + "*" + entities[i].getId() + " ";

		}

		return res;

	}

}
