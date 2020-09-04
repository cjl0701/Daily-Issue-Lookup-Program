package DailyIssueLookUpProgram;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/* Jsoup API(HTML 파싱)를 사용하여
      선택한 날짜에 게재된 메인 뉴스와 인기검색어를 크롤링하여 TextArea에 append.*/
public class CrawlingActionListener implements ActionListener {
	private IssueLookUpUI view;

	public CrawlingActionListener(IssueLookUpUI view) {
		super();
		this.view = view;
	}

	// 날짜 선택시 호출, 해당 날짜의 인기 검색어와 메인 뉴스를 크롤링하여 textArea에 보여줌
	@Override
	public void actionPerformed(ActionEvent e) {

		String year = view.yearChoice.getSelectedItem().toString();
		String month = view.monthChoice.getSelectedItem().toString();
		JButton btn = (JButton) e.getSource();
		String day = btn.getText();
		if (month.length() < 2)
			month = 0 + month;
		if (day.length() < 2)
			day = 0 + day;
		String date = year + "-" + month + "-" + day;
		view.dateLabel.setText(date);
		String keywordUrl = "https://datalab.naver.com/keyword/realtimeList.naver?datetime=" + date + "T18%3A00%3A00";
		String newsUrl = "https://news.naver.com/main/list.nhn?mode=LPOD&sid2=140&sid1=001&mid=sec&oid=001&isYeonhapFlash=Y&date="
				+ year + month + day;
		try {
			// 인기 검색어 크롤링
			view.keywordArea.setText("");
			view.keywordArea.append(date + "일 18:00 기준 \n");
			Document keywordDoc = Jsoup.connect(keywordUrl).get(); // get 방식으로 HTML 가져옴
			Elements rankigList = keywordDoc.select(".ranking_list > li"); // 클래스 명이 ranking_list인 태그의 하위 <li>태그'들' 선택

			// textArea에 보여줌
			int i = 1;
			for (Element li : rankigList) {
				String line = li.select(".item_title").first().text();
				view.keywordArea.append(i + " : " + line + "\n");
				i++;
			}

			
			// 메인 뉴스 크롤링
			view.newsArea.setText("");
			Document newsDoc = Jsoup.connect(newsUrl).get(); // get 방식으로 HTML 가져옴
			Elements newsList = newsDoc.select(".type02 > li"); // 클래스 명이 type02인 태그의 하위 <li>태그'들' 선택
			
			// textArea에 보여줌
			i = 1;
			for (Element li : newsList) {
				String news = li.select("a > strong").first().text(); // a 태그의 하위 strong 태그를 선택
				view.newsArea.append(i + " : " + news + "\n");
				i++;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
