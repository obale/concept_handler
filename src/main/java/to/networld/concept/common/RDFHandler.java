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

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.util.FileManager;

/**
 * Provides useful implementation to read/write RDF files with the help
 * of the Jena Framework.
 * 
 * Each object of this class is related to one RDF file.
 * 
 * @author Alex Oberhauser
 *
 */
public class RDFHandler {
	private String inputFile = null;
	private Model model = null;
	
	public RDFHandler(String _inputFile) {
		this.inputFile = _inputFile;
		this.model = ModelFactory.createDefaultModel();
		if ( this.inputFile != null ) this.readFile();
	}
	
	public void readStream(InputStream _xmlFile, String _url) {
		this.inputFile = "";
		this.model = ModelFactory.createDefaultModel();
		this.model.read(_xmlFile, null);
	}
	
	/**
	 * Add the file that is assigned to this object to the model.
	 */
	private void readFile() {
		InputStream in = FileManager.get().open(this.inputFile);
		this.model.read(in, "file:///" + this.inputFile);
	}
	
	/**
	 * Writes the current model to the file (given as filepath as String). 
	 * 
	 * Format:			RDF/XML-ABBREV
	 * Encoding:		UTF-8
	 * TAB:				4 whitespace
	 * XML Declaration: Shown
	 * 
	 * @param outputFile The output file as String.
	 * @throws IOException
	 */
	public void writeFile(String outputFile) throws IOException {
		Writer file = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));
    	RDFWriter writer = this.model.getWriter("RDF/XML-ABBREV");
        writer.setProperty("showXmlDeclaration", "true");
        writer.setProperty("tab", "4");
        writer.setProperty("relativeURIs", "same-document,relative");
        writer.write(this.model, file, "");
        // TODO: Fix logger settings.
//        Config.getLogger().debug("Writing a 'RDF/XML-ABBREV' model to '" + outputFile + "'");
	}
	
	/**
	 * Executes a query on the current model.
	 * 
	 * @param queryString The query as String.
	 * @return A ResultSet that could be read out with the help of the Jena framework.
	 */
	public ResultSet executeQuery(String queryString) {
        // TODO: Fix logger settings.
//		Config.getLogger().trace("Executing query: \n" + queryString);
		Query query = QueryFactory.create(queryString);
		QueryExecution qe;
		if ( this.inputFile != null )
			qe = QueryExecutionFactory.create(query, this.model);
		else
			qe = QueryExecutionFactory.create(query);
		ResultSet results = qe.execSelect();
		return results;
	}

	/**
	 * Print the output as table to the standard output.
	 * 
	 * @param results A ResultSet that will be returned by executeQuery(String)
	 */
	public void printResult(ResultSet results) {
		ResultSetFormatter.out(System.out, results);
	}
	
	public String getFileName() { return this.inputFile; }
	public Model getModel() { return this.model; }
}
