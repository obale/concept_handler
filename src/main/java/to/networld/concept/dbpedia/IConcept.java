/**
 * Concept Handler
 *
 * Copyright (C) 2010 by Networld Project
 * Written by Alex Oberhauser <oberhauseralex@networld.to>
 * All Rights Reserved
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software.  If not, see <http://www.gnu.org/licenses/>
 */

package to.networld.concept.dbpedia;

import java.util.List;
import java.util.Map;

/**
 * @author Alex Oberhauser
 *
 */
public interface IConcept {
	
	/**
	 * Extracts the human readable name of the concept. 
	 * 
	 * @return The language as key and the name of the concept as value.
	 */
	public Map<String, String> getName();
	
	/**
	 * Takes the language as parameter and returns the name in that language.
	 * 
	 * @param _language The language 
	 * @return If name not found NULL otherwise the name.
	 */
	public String getName(String _language);
	
	/**
	 * Parse the super concepts (Hyponym) for a given DBPedia file.
	 * 
	 * @param _level The level how deep the tree should be evaluated.
	 * @return List with super concept URLs
	 */
	public List<String> getSuperConcepts(int _level);
	
	/**
	 * Parse the sub concepts (Hypernym) for a given DBPedia file.
	 * 
	 * @param _level The level how deep the tree should be evaluated.
	 * @return key = DBPedia URL and value = concept name
	 */
	public List<String> getSubConcepts(int _level);
}
