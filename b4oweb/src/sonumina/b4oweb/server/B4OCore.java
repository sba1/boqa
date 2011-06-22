package sonumina.b4oweb.server;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Logger;

import ontologizer.association.AssociationContainer;
import ontologizer.association.AssociationParser;
import ontologizer.go.OBOParser;
import ontologizer.go.OBOParserException;
import ontologizer.go.Ontology;
import ontologizer.go.Term;
import ontologizer.go.TermContainer;
import sonumina.math.graph.SlimDirectedGraphView;

public class B4OCore
{
	private static Logger logger = Logger.getLogger(B4OCore.class.getName());

	static final String DEFINITIONS_PATH = "/home/sba/workspace/b4oweb/human-phenotype-ontology.obo.gz";
	static final String ASSOCIATIONS_PATH = "/home/sba/workspace/b4oweb/phenotype_annotation.omim.gz";

	/**
	 * The static ontology object. Defines terms that the user can select.
	 */
	static private Ontology ontology;

	/**
	 * The corresponding slim view.
	 */
	static private SlimDirectedGraphView<Term> slimGraph;

	/**
	 * Contains the indices of the term in sorted order
	 */
	static private int [] sorted2Idx;
	
	/**
	 * Contains the rank of the term within the sorted order. 
	 */
	static private int [] idx2Sorted;
	
	/**
	 * The static association container. Defines the items.
	 */
	static private AssociationContainer associations;

	static
	{
		logger.info("Starting " + B4OCore.class.getName());

		OBOParser oboParser = new OBOParser(DEFINITIONS_PATH);
		try {
			oboParser.doParse();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (OBOParserException e1) {
			e1.printStackTrace();
		}
		TermContainer goTerms = new TermContainer(oboParser.getTermMap(), oboParser.getFormatVersion(), oboParser.getDate());
		logger.info("OBO file \"" + DEFINITIONS_PATH + "\" parsed");

		ontology = new Ontology(goTerms);
		logger.info("Ontology graph with " + ontology.getNumberOfTerms() + " terms created");
		
		slimGraph = ontology.getSlimGraphView();
		logger.info("Slim graph greated");

		/* Sort the term according to the alphabet */
		class TermName
		{
			int index;
			String name;
		}
		TermName [] terms = new TermName[slimGraph.getNumberOfVertices()];
		for (int i=0;i<slimGraph.getNumberOfVertices();i++)
		{
			terms[i] = new TermName();
			terms[i].name = slimGraph.getVertex(i).getName();
			terms[i].index = i;
		}
		Arrays.sort(terms, new Comparator<TermName>() {
			@Override
			public int compare(TermName o1, TermName o2)
			{
				return o1.name.compareTo(o2.name);
			}
		});
		sorted2Idx = new int[terms.length];
		idx2Sorted = new int[terms.length];
		for (int i=0;i<terms.length;i++)
		{
			sorted2Idx[i] = terms[i].index;
			idx2Sorted[terms[i].index] = i;
		}
		
		/* Load associations */
		try {
			AssociationParser ap = new AssociationParser(ASSOCIATIONS_PATH,ontology.getTermContainer(),null,null);
			associations = new AssociationContainer(ap.getAssociations(), ap.getSynonym2gene(), ap.getDbObject2gene());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the global ontology.
	 * 
	 * @return
	 */
	public static Ontology getOntology()
	{
		return ontology;
	}
	
	/**
	 * Returns the global association container.
	 * 
	 * @return
	 */
	public static AssociationContainer getAssociations()
	{
		return associations;
	}

	/**
	 * Returns the number of terms.
	 * 
	 * @return
	 */
	public static int getNumberTerms()
	{
		return slimGraph.getNumberOfVertices();
	}

	/**
	 * Returns the term at the given sorted index.
	 * 
	 * @param sortedIdx
	 * @return
	 */
	public static Term getTerm(int sortedIdx)
	{
		return slimGraph.getVertex(sorted2Idx[sortedIdx]);
	}
}
