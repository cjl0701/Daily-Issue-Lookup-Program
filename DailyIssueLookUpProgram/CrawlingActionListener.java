package DailyIssueLookUpProgram;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/* Jsoup API(HTML �Ľ�)�� ����Ͽ�
      ������ ��¥�� ����� ���� ������ �α�˻�� ũ�Ѹ��Ͽ� TextArea�� append.*/
public class CrawlingActionListener implements ActionListener {
	private IssueLookUpUI view;

	public CrawlingActionListener(IssueLookUpUI view) {
		super();
		this.view = view;
	}

	// ��¥ ���ý� ȣ��, �ش� ��¥�� �α� �˻���� ���� ������ ũ�Ѹ��Ͽ� textArea�� ������
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
			// �α� �˻��� ũ�Ѹ�
			view.keywordArea.setText("");
			view.keywordArea.append(date + "�� 18:00 ���� \n");
			Document keywordDoc = Jsoup.connect(keywordUrl).get(); // get ������� HTML ������
			Elements rankigList = keywordDoc.select(".ranking_list > li"); // Ŭ���� ���� ranking_list�� �±��� ���� <li>�±�'��' ����

			// textArea�� ������
			int i = 1;
			for (Element li : rankigList) {
				String line = li.select(".item_title").first().text();
				view.keywordArea.append(i + " : " + line + "\n");
				i++;
			}

			
			// ���� ���� ũ�Ѹ�
			view.newsArea.setText("");
			Document newsDoc = Jsoup.connect(newsUrl).get(); // get ������� HTML ������
			Elements newsList = newsDoc.select(".type02 > li"); // Ŭ���� ���� type02�� �±��� ���� <li>�±�'��' ����
			
			// textArea�� ������
			i = 1;
			for (Element li : newsList) {
				String news = li.select("a > strong").first().text(); // a �±��� ���� strong �±׸� ����
				view.newsArea.append(i + " : " + news + "\n");
				i++;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
