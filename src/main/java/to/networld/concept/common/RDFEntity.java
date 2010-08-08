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

package to.networld.concept.common;

import java.io.InputStream;
import java.util.Vector;

import com.hp.hpl.jena.query.ResultSet;

/**
 * Super class for all RDF entities. Puts the main function that are needed for querying the RDF files
 * at disposal. 
 * 
 * @author Alex Oberhauser
 *
 */
public class RDFEntity {
	protected String selfURI = null;
	protected RDFHandler rdfhandler = null;
	
	protected RDFEntity(String _selfURI) {
		this.selfURI = _selfURI;
		this.rdfhandler = new RDFHandler(this.selfURI);
	}
	
	protected RDFEntity(InputStream _xmlFile) {
		this.rdfhandler = new RDFHandler(null);
		this.rdfhandler.readStream(_xmlFile, null);
	}
	
	/**
	 * If the namespace definition that you need is not in the returned {@link StringBuffer}
	 * than feel free to add it to this header.
	 * 
	 * @return The header as {@link StringBuffer} with all important namespace definition.
	 */
	protected StringBuffer getHeader() {
		StringBuffer strbuffer = new StringBuffer();
		strbuffer.append("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n");
		strbuffer.append("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n");
		strbuffer.append("PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n");
		strbuffer.append("PREFIX sioc: <http://rdfs.org/sioc/ns#>\n");
		strbuffer.append("PREFIX scot: <http://scot-project.org/scot/ns#>\n");
		strbuffer.append("PREFIX dc: <http://purl.org/dc/elements/1.1/>\n");
		strbuffer.append("PREFIX dct: <http://purl.org/dc/terms/>\n");
		strbuffer.append("PREFIX dbpedia: <http://dbpedia.org/>\n");
		strbuffer.append("PREFIX dbpedia2: <http://dbpedia.org/property/>\n");
		strbuffer.append("PREFIX dbpediaEntry: <http://dbpedia.org/resource/>\n");
		strbuffer.append("PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n");
		return strbuffer;
	}
	
	/**
	 * Return only the objects with the following form:<p>
	 * 
	 *     <fileuri> inputProperty retValue<p>
	 *     
	 * inputProperty is the parameter as string and retValue will be returned. 
	 * If there are more than one match all results are returned. 
	 * 
	 * @param property The property as String that should be read out.
	 * @return All objects that matches the query.
	 */
	public Vector<String> getOwnProperty(String property) {
		StringBuffer strbuffer = this.getHeader();
		strbuffer.append("SELECT ?property\n");
		strbuffer.append("WHERE {\n");
		strbuffer.append("<" + this.selfURI + "> " + property + " ?property . \n");
		strbuffer.append("}");
		ResultSet results = this.rdfhandler.executeQuery(strbuffer.toString());
		Vector<String> retVector = new Vector<String>();
		while ( results.hasNext() ) {
			retVector.add(results.next().get("property").toString());
		}
		return retVector;
	}
	
	/**
	 * Return all object of the form:<p>
	 * 
	 *     [] inputProperty retValue<p>
	 *     
	 * inputProperty is the parameter as string and retValue will be returned.
	 * The subject doesn't matter.
	 * If there are more than one match all results are returned. 
	 * 
	 * @param property The property as String that should be read out.
	 * @return All objects that matches the query.
	 */
	public Vector<String> getAllProperty(String property) {
		StringBuffer strbuffer = this.getHeader();
		strbuffer.append("SELECT ?property\n");
		strbuffer.append("WHERE {\n");
		strbuffer.append(" [] " + property + " ?property . \n");
		strbuffer.append("}");
		ResultSet results = this.rdfhandler.executeQuery(strbuffer.toString());
		Vector<String> retVector = new Vector<String>();
		while ( results.hasNext() ) {
			retVector.add(results.next().get("property").toString());
		}
		return retVector;
	}
	
	/**
	 * <code>null</code> is returned when the RDF file is read in as {@link InputStream}
	 * and not from a valid URI.
	 * 
	 * @return The URI of the file or <code>null</code> if there is no URI.
	 */
	public String getURI() { return this.selfURI; }
}
