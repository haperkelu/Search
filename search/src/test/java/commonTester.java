import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.ChineseAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Test;

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
	
	@Test
	public void testMMSeg() throws IOException{
		String[] args = {"大家早上好，京华时报２００８年1月23日报道 昨天，受一股来自中西伯利亚的强冷空气影响，本市出现大风降温天气，白天最高气温只有零下7摄氏度，同时伴有6到7级的偏北风。", ""};
		new Simple().forTest(args);
	}

}
