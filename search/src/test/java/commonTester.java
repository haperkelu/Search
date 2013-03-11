import org.junit.Test;

import com.pieli.search.SimilarService;

public class commonTester {

	@Test
	public void test() throws Exception {
		
		String[] content = {
				"e:/DuckDuckGo Architecture - 1 Million Deep Searches a Day and Growing.txt",
				"e:/Processing 100 Million Pixels a Day - Small Amounts of Contention Cause Big Problems at Scale.txt",
				"e:/BigData using Erlang, C and Lisp to Fight the Tsunami of Mobile Data.txt"
		};
		SimilarService service = new SimilarService();
		service.analysis(content);
		
	}

}
