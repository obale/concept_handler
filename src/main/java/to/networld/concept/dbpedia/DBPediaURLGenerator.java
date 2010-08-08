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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.sparql.lib.org.json.JSONArray;
import com.hp.hpl.jena.sparql.lib.org.json.JSONException;

/**
 * @author Alex Oberhauser
 *
 */
public class DBPediaURLGenerator {
	
	/**
	 * Searches with the help of the Wikipedia API for valid identifier of DBPedia entries.
	 * 
	 * @param _conceptName The name of the concept as String.
	 * @return A list of valid, but possible empty DBPedia URLs
	 */
	public static List<String> getCategoryURLs(String _conceptName) {
		final String prefix = "http://dbpedia.org/resource/Category:";
		StringBuffer dbpediaURL = new StringBuffer();
		dbpediaURL.append(prefix);
		
		StringBuffer wikiAPIprefix = new StringBuffer();
		wikiAPIprefix.append("http://en.wikipedia.org/w/api.php?action=opensearch&format=json&search=");
		wikiAPIprefix.append(_conceptName.replaceAll(" ", "%20"));
		try {
			URL url = new URL(wikiAPIprefix.toString());
			URLConnection con = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) 
				response.append(inputLine);
			in.close();
        
			JSONArray jsonArray = new JSONArray(response.toString());
			if ( jsonArray.length() > 1 ) {
				JSONArray resultArray = jsonArray.getJSONArray(1);
				String searchTerm;
				List<String> dbpediaURLs = new ArrayList<String>();
				for ( int count=0; count < resultArray.length(); count++ ) {
					searchTerm = resultArray.getString(count);
					dbpediaURL.delete(0, dbpediaURL.length());
					dbpediaURL.append(prefix);
					dbpediaURL.append(searchTerm.replaceAll(" ", "_"));
					dbpediaURLs.add(dbpediaURL.toString());
				}
				return dbpediaURLs;
			}
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		
	}
}
