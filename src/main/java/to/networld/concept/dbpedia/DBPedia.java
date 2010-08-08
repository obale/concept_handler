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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

import to.networld.concept.common.RDFEntity;

/**
 * DBPedia.org Entity
 * 
 * @author Alex Oberhauser
 *
 */
public class DBPedia extends RDFEntity implements IConcept {

	private final String dbpediaURL;
	
	/**
	 * Should be a Category URL in the following form (example Artificial Intelligence):
	 * 
	 * http://dbpedia.org/resource/Category:Artificial_intelligence
	 * 
	 * @param _dbpediaURL The DBPedia URL that should be parsed
	 */
	public DBPedia(String _dbpediaURL) {
		super(_dbpediaURL);
		this.dbpediaURL = _dbpediaURL;
	}

	/**
	 * @see to.networld.concept.dbpedia.IConcept#getSubConcepts(int)
	 */
	@Override
	public List<String> getSubConcepts(int _level) {
		List<String> subConceptMap = new ArrayList<String>();
		StringBuffer strbuffer = this.getHeader();
		strbuffer.append("SELECT ?subConcept\n");
		strbuffer.append("WHERE {\n");
		strbuffer.append("?subConcept skos:subject <" + this.dbpediaURL + "> .\n");
		strbuffer.append("}");
		
		ResultSet results = this.rdfhandler.executeQuery(strbuffer.toString());
		RDFNode entry = null;
		while ( results.hasNext() ) {
			entry = results.next().get("subConcept");
			if ( entry.isResource() ) {
				String entryNodeURL = entry.asNode().toString();
				subConceptMap.add(entryNodeURL);
				if ( _level > 0 )
					subConceptMap.addAll(new DBPedia(entryNodeURL).getSubConcepts(--_level));
			}
		}
		return subConceptMap;
	}

	/**
	 * @see to.networld.concept.dbpedia.IConcept#getSuperConcepts(int)
	 */
	@Override
	public List<String> getSuperConcepts(int _level) {
		List<String> superConceptMap = new ArrayList<String>();
		StringBuffer strbuffer = this.getHeader();
		strbuffer.append("SELECT ?superConcept\n");
		strbuffer.append("WHERE {\n");
		strbuffer.append("<" + this.dbpediaURL + "> skos:broader ?superConcept .\n");
		strbuffer.append("}");
		
		ResultSet results = this.rdfhandler.executeQuery(strbuffer.toString());
		RDFNode entry = null;
		while ( results.hasNext() ) {
			entry = results.next().get("superConcept");
			if ( entry.isResource() ) {
				String entryNodeURL = entry.asNode().toString();
				superConceptMap.add(entryNodeURL);
				if ( _level > 0 )
					superConceptMap.addAll(new DBPedia(entryNodeURL).getSuperConcepts(--_level));
			}
		}
		return superConceptMap;
	}

	/**
	 * @see to.networld.concept.dbpedia.IConcept#getName()
	 */
	@Override
	public Map<String, String> getName() {
		StringBuffer strbuffer = this.getHeader();
		strbuffer.append("SELECT ?name\n");
		strbuffer.append("WHERE {\n");
		strbuffer.append("[] rdfs:label ?name .\n");
		strbuffer.append("}");
		
		ResultSet results = this.rdfhandler.executeQuery(strbuffer.toString());
		Map<String, String> nameMap = new HashMap<String, String>();
		while ( results.hasNext() ) {
			Node entry = results.next().get("name").asNode();
			nameMap.put(entry.getLiteralLanguage(), entry.getLiteralLexicalForm());
		}
		return nameMap;
	}

	/**
	 * @see to.networld.concept.dbpedia.IConcept#getName(java.lang.String)
	 */
	@Override
	public String getName(String _language) {
		StringBuffer strbuffer = this.getHeader();
		strbuffer.append("SELECT ?name\n");
		strbuffer.append("WHERE {\n");
		strbuffer.append("[] rdfs:label ?name .\n");
		strbuffer.append("FILTER ( regex(LANG(?name), \"" + _language + "\") )\n ");
		strbuffer.append("}");
		
		ResultSet results = this.rdfhandler.executeQuery(strbuffer.toString());
		if ( results.hasNext() )
			return results.next().getLiteral("name").asNode().getLiteralLexicalForm();
		return null;
	}
	
	@Override
	public String toString() { return this.getName().get("en"); }
}
 