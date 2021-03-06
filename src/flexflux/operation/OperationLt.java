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
 * 7 mars 2013 
 */
package flexflux.operation;

import flexflux.general.Constraint;
import flexflux.general.Vars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import parsebionet.biodata.BioEntity;

/**
 * 
 * 
 * Class representing the operation "Less than" : <
 * 
 * @author lmarmiesse 7 mars 2013
 * 
 */
public class OperationLt extends Operation {

	public String toString() {

		return (" < ");

	}

	public List<Constraint> makeConstraint(BioEntity entity, double value) {

		List<Constraint> constraints = new ArrayList<Constraint>();

		Map<BioEntity, Double> constraintMap = new HashMap<BioEntity, Double>();
		constraintMap.put(entity, 1.0);

		if (Vars.cheat) {

			Constraint c = new Constraint(constraintMap, -Double.MAX_VALUE,
					value - Vars.epsilon);
			constraints.add(c);

		} else {
			Constraint c = new Constraint(constraintMap, -Double.MAX_VALUE,
					value);
			constraints.add(c);

			Constraint c2 = new Constraint(constraintMap, value, true);
			constraints.add(c2);

		}
		return constraints;

	}

	public boolean isTrue(Constraint cons, double value) {

		return cons.getUb() < value;

	}
	
	public boolean isInverseTrue(Constraint cons, double value) {

		return cons.getLb() >= value;

	}

}
