package com.pieli.test;
import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Test;

import com.chenlb.mmseg4j.analysis.MMSegAnalyzer;

public class MMSegTester {

	@Test
	public void testMMSeg() throws IOException{
		String args = "Good News!眼看就要来了，京华时报２００８年1月23日报道 昨天，受一股来自中西伯利亚的强冷空气影响，本市出现大风降温天气，白天最高气温只有零下7摄氏度，同时伴有6到7级的偏北风。";
		MMSegAnalyzer analyzer = new MMSegAnalyzer();
		TokenStream ts = analyzer.tokenStream("", new StringReader(args));
		CharTermAttribute att = ts.addAttribute(CharTermAttribute.class);
		while(ts.incrementToken()){
			System.out.println(att.toString());
		}
		analyzer.close();
	}
}
