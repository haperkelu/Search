package com.pieli.search;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math.linear.OpenMapRealVector;
import org.apache.commons.math.linear.RealVectorFormat;
import org.apache.commons.math.linear.SparseRealVector;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * 
* @ClassName: SimilarService
* @Description: TODO
* @date 2013-3-11 下午2:45:40
*
 */
public class SimilarService {

	private static final Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
	private static final String path = "d:/index";
	
	public Map<String, Double> analysis(String[] content) throws Exception{
		
		Map<String, Double> result = new HashMap<String, Double>();
		if(content == null || content.length == 0){
			return null;
		}
		int count = 0;
		Directory directory = FSDirectory.open(new File(path));
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36, analyzer);
		config.setOpenMode(OpenMode.CREATE_OR_APPEND);
		IndexWriter writer = new IndexWriter(directory, config);		
		int[] docIdArr = new int[content.length];
		for(int i = 0; i < content.length ; i ++){
			Document doc = new Document();
			docIdArr[i] = ++ count;
			doc.add(new Field("id", String.valueOf(docIdArr[i]), Field.Store.YES, Field.Index.NO, Field.TermVector.NO));
			doc.add(new Field("content", new FileReader(new File(content[i])), Field.TermVector.WITH_POSITIONS_OFFSETS));
			writer.addDocument(doc);
		}
		
		writer.close();
		
		DocVector[] docs = new DocVector[content.length];
		IndexReader reader = IndexReader.open(
			      FSDirectory.open(new File(path)));
		
		Map<String,Integer> terms = new HashMap<String,Integer>();
		TermEnum termEnum = reader.terms(new Term("content"));
		int pos = 0;
	    while (termEnum.next()) {
	      Term term = termEnum.term();
	      if (! "content".equals(term.field())) {
	    	  break;
	      }
	      terms.put(term.text(), pos++);
	    }

		for(int i = 0; i < docIdArr.length; i ++){
			docs[i] = new DocVector(terms);
			TermFreqVector[] tfvs = reader.getTermFreqVectors(docIdArr[i]);
			 for (TermFreqVector tfv : tfvs){
				 String[] termTexts = tfv.getTerms();
				 int[] termFreqs = tfv.getTermFrequencies();
				 for (int j = 0; j < termTexts.length; j++) {
					  System.out.println(i + ":" + termTexts[j] + "," + termFreqs[j]);
			          docs[i].setEntry(termTexts[j], termFreqs[j]);
			     }
			 }
			 docs[i].normalize();
		}		
		
		double cosim01 = getCosineSimilarity(docs[0], docs[1]);
		System.out.println("1&2:" + cosim01);
		double cosim02 = getCosineSimilarity(docs[0], docs[2]);
		System.out.println("1&3:" + cosim02);
		double cosim03 = getCosineSimilarity(docs[1], docs[2]);
		System.out.println("2&3:" + cosim03);
		
		reader.close();
		return result;
		
	}
	
	/**
	 * 计算cos值
	* @Title: getCosineSimilarity
	* @Description: TODO
	* @param @param d1
	* @param @param d2
	* @param @return
	* @return double
	* @throws
	 */
	private double getCosineSimilarity(DocVector d1, DocVector d2) {
	    return (d1.vector.dotProduct(d2.vector)) /       //向量相乘之和
	      (d1.vector.getNorm() * d2.vector.getNorm());   //L2 Norm Sum
	}
	
	private class DocVector {
	    public Map<String,Integer> terms;
	    public SparseRealVector vector;
	    
	    public DocVector(Map<String,Integer> terms) {
	      this.terms = terms;
	      this.vector = new OpenMapRealVector(terms.size());
	    }
	    
	    public void setEntry(String term, int freq) {
	      if (terms.containsKey(term)) {
	        int pos = terms.get(term);
	        vector.setEntry(pos, (double) freq);
	      }
	    }
	    
	    /**
	     * 
	    * @Title: normalize
	    * @Description: TODO
	    * @param 
	    * @return void
	    * @throws
	     */
	    public void normalize() {
	      double sum = vector.getL1Norm();
	      vector = (SparseRealVector) vector.mapDivide(sum);
	    }
	    
	    public String toString() {
	      RealVectorFormat formatter = new RealVectorFormat();
	      return formatter.format(vector);
	    }
	  }
	
}
