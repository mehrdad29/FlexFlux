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
 * A condition is a set of constraints. 
 */
package flexflux.condition;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import flexflux.general.Constraint;
import flexflux.general.ConstraintType;
import flexflux.general.SimplifiedConstraint;

public class Condition {

	public String name = "NA";
	public String code = "NA";

	/**
	 * Map of {@link SimplifiedConstraint}. The key is the entities id
	 */
	public HashMap<String, SimplifiedConstraint> constraints = new HashMap<String, SimplifiedConstraint>();

	/**
	 * Constructor
	 * 
	 * @param bind
	 */
	public Condition(String code, String name) {

		this.name = name;
		this.code = code;

	}

	/**
	 * Add a constraint to the condition If the entity does not exist in the
	 * bind create it
	 * 
	 * @param entityId
	 *            : the identifier of the entity
	 * @param lb
	 *            : lower bound
	 * @param ub
	 *            : upper bound
	 * @param type
	 *            : type of the constraint
	 */
	public void addConstraint(String entityId, double value,
			ConstraintType type) {

		SimplifiedConstraint constraint = new SimplifiedConstraint(entityId,value, type);
		
		constraints.put(entityId, constraint);
		
		return;

	}
	
	/**
	 * Compare to another condition
	 * Compare only the constraints and not the name and the codes
	 * @param c2
	 * @return
	 */
	@Override
	public boolean equals(Object other) {

		if(other == null)
		{
			return false;
		}
		if(other == this) {
			return true;
		}
		
		if (!(other instanceof Condition))return false;
		
		Condition c2 = (Condition) other;
		
		if(! c2.constraints.equals(this.constraints)) {
			return false;
		}
		
		return true;
		
	}
	
	@Override
	public int hashCode() {
		
		int prim =  name.hashCode()+code.hashCode();
		
		for(SimplifiedConstraint c : constraints.values()) {
			prim += c.hashCode();
		}
		
		return prim;
		
	}
	
	
	/**
	 * 
	 * @return the number of constraints
	 */
	public int size() {
		return constraints.size();
	}
	
	/**
	 * Get a constraint by the entity id
	 * @param entityId
	 * @return
	 */
	public SimplifiedConstraint getConstraint(String entityId) {
		
		return this.constraints.get(entityId);
		
		
	}
	
	/**
	 * Check if a constraint exists for an entity id
	 * @param entityId
	 * @return
	 */
	public Boolean containsConstraint(String entityId) {
		return this.constraints.containsKey(entityId);
	}
	
	
	
}

