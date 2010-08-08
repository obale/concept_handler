package to.networld.concept;

import java.util.List;

import to.networld.concept.dbpedia.DBPedia;
import to.networld.concept.dbpedia.DBPediaURLGenerator;

/**
 * @author Alex Oberhauser
 *
 */
public class Main {
	
	private static void outputList(List<String> _list) {
		for ( String entry : _list ) {
			System.out.println("\t* " + entry);
		}
	}
	
	public static void main(String[] args) {
		List<String> dbpediaURLs = DBPediaURLGenerator.getCategoryURLs("petrol");
		for ( String entry : dbpediaURLs ) {
			DBPedia dbpedia = new DBPedia(entry);
			
			System.out.println("Seeking for Super Concepts for " + entry);
			outputList(dbpedia.getSuperConcepts(0));
			
			System.out.println("Seeking for Sub Concepts for " + entry);
			outputList(dbpedia.getSubConcepts(0));
		}
	}
}
