package com.pieli.test;
import java.io.IOException;
import java.io.StringReader;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.ChineseAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Test;
import com.chenlb.mmseg4j.example.Complex;
import com.chenlb.mmseg4j.example.Simple;
import com.pieli.search.SimilarService;

public class commonTester {

	@Test
	public void test() throws Exception {

		String[] content = {
				"e:/DuckDuckGo Architecture - 1 Million Deep Searches a Day and Growing.txt",
				"e:/Processing 100 Million Pixels a Day - Small Amounts of Contention Cause Big Problems at Scale.txt",
				"e:/BigData using Erlang, C and Lisp to Fight the Tsunami of Mobile Data.txt" };
		SimilarService service = new SimilarService();
		service.analysis(content);

	}

	@Test
	public void testChineseAnalyzer() throws Exception {

		StringReader reader = new StringReader("123 好人做饭 good");
		ChineseAnalyzer analyzer = new ChineseAnalyzer();
		TokenStream ts = analyzer.tokenStream("", reader);
		
		CharTermAttribute term = ts.addAttribute(CharTermAttribute.class);
		ts.reset();
		while (ts.incrementToken()) {
			System.out.println(term);
		}
		ts.end();

	}

}
