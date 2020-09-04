package DailyIssueLookUpProgram;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class IssueLookUpUI extends JFrame implements ItemListener {
	JTextArea keywordArea; // 인기 검색어 출력
	JTextArea newsArea; // 메인 뉴스 출력
	private JPanel outputPanel; // 결과 출력 패널
	private JPanel calendarPanel; // 달력 패널
	private JPanel choicePanel; // 년, 월 선택 패널
	JComboBox<String> yearChoice, monthChoice; // 년,월 선택 콤보 박스
	private JButton[] days = new JButton[42];// 날짜 버튼. 7일이 6주이므로 42개의 버튼필요
	JLabel dateLabel; // 선택 날짜 표시 라벨
	private Calendar cal = Calendar.getInstance();

	public IssueLookUpUI() {
		setTitle(
				"오늘 날짜:" + cal.get(Calendar.YEAR) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DATE));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1100, 600);

		// 화면의 가운데에 출력
		Dimension dimen = Toolkit.getDefaultToolkit().getScreenSize();
		int xpos = (int) ((dimen.getWidth() - this.getWidth()) / 2);
		int ypos = (int) ((dimen.getHeight() - this.getHeight()) / 2);
		setLocation(xpos, ypos);

		// 년, 월 선택 콤보박스
		yearChoice = new JComboBox<String>();
		monthChoice = new JComboBox<String>();

		outputPanel = new JPanel();
		calendarPanel = new JPanel(new GridLayout(7, 7, 5, 5));// 7행 7열의 그리드레이아웃
		choicePanel = new JPanel();
		makeOutputArea();
		makeCalendar();

		this.add(choicePanel, "North");// 연도와 월을 선택할 수 있는 화면읠 상단에 출력
		this.add(calendarPanel, "Center");
		this.add(outputPanel, "East");

		setVisible(true);
	}

	public void makeOutputArea() {
		// 크롤링 결과 출력 패널
		outputPanel.setLayout(new BorderLayout());
		dateLabel = new JLabel(); 
		dateLabel.setFont(new Font("Arial", Font.BOLD, 20));
		dateLabel.setHorizontalAlignment(JLabel.CENTER);

		// 뉴스 패널
		JPanel newsPanel = new JPanel();
		newsPanel.setLayout(new BorderLayout());
		JLabel newsLabel = new JLabel("Naver main 뉴스");
		newsLabel.setFont(new Font("Gothic", Font.BOLD, 15));
		newsLabel.setHorizontalAlignment(JLabel.CENTER);
		newsArea = new JTextArea(40, 40);
		newsArea.setCaretPosition(newsArea.getDocument().getLength()); // 문장 맨끝에 포인트 위치 시킴
		JScrollPane newsPane = new JScrollPane(newsArea);

		newsPanel.add(newsLabel, "North");
		newsPanel.add(newsPane, "Center");

		// 인기 검색어 패널
		JPanel kwPanel = new JPanel();
		kwPanel.setLayout(new BorderLayout());
		JLabel kwLabel = new JLabel("Naver 인기 검색어");
		kwLabel.setFont(new Font("Gothic", Font.BOLD, 15));
		kwLabel.setHorizontalAlignment(JLabel.CENTER);
		keywordArea = new JTextArea(40, 25);
		keywordArea.setCaretPosition(keywordArea.getDocument().getLength()); // 문장 맨끝에 포인트 위치 시킴
		JScrollPane keywordPane = new JScrollPane(keywordArea);

		kwPanel.add(kwLabel, "North");
		kwPanel.add(keywordPane, "Center");

		// 크롤링 결과 출력 패널에 부착
		outputPanel.add(dateLabel, "North");
		outputPanel.add(newsPanel, "West");
		outputPanel.add(kwPanel, "East");
	}

	public void makeCalendar() {
		JLabel[] dayLabel = new JLabel[7];
		String[] day = { "일", "월", "화", "수", "목", "금", "토" };
		// 년, 월 선택 창에 아이템 달기
		for (int i = 2020; i >= 2015; i--)
			yearChoice.addItem(String.valueOf(i));
		for (int i = 1; i <= 12; i++)
			monthChoice.addItem(String.valueOf(i));
		JLabel yearLabel = new JLabel("년");
		JLabel monthLabel = new JLabel("월");
		choicePanel.add(yearChoice);
		choicePanel.add(yearLabel);
		choicePanel.add(monthChoice);
		choicePanel.add(monthLabel);

		// 요일 이름을 레이블에 출력
		for (int i = 0; i < day.length; i++) {
			dayLabel[i] = new JLabel(day[i], JLabel.CENTER);
			calendarPanel.add(dayLabel[i]);
			dayLabel[i].setBackground(Color.GRAY);// 사실상 의미가 없슴. 바뀌지 않음.
		}
		dayLabel[6].setForeground(Color.BLUE);// 토요일 색상
		dayLabel[0].setForeground(Color.RED);// 일요일 색상

		CrawlingActionListener listener = new CrawlingActionListener(this);
		// 날짜 버튼 생성
		for (int i = 0; i < 42; i++) {// 모두 42개의 버튼을 생성
			days[i] = new JButton("");// 제목이 없는 버튼 생성
			if (i % 7 == 0)
				days[i].setForeground(Color.RED);// 일요일 버튼의 색
			else if (i % 7 == 6)
				days[i].setForeground(Color.BLUE);// 토요일 버튼의 색
			else
				days[i].setForeground(Color.BLACK);
			days[i].addActionListener(listener);// 선택한 날짜에 게재된 성경 말씀을 크롤링하여 TextArea에 append
			calendarPanel.add(days[i]);
		}

		// 콤보박스에 기본 값 설정, 아이템 리스너 달기
		String m = (cal.get(Calendar.MONTH) + 1) + "";
		String y = cal.get(Calendar.YEAR) + "";
		yearChoice.setSelectedItem(y);
		monthChoice.setSelectedItem(m);
		yearChoice.addItemListener(this);
		monthChoice.addItemListener(this);

		setDate(); // 버튼에 날짜 설정
	}

	// 버튼에 날짜 설정
	public void setDate() {
		String today = Integer.toString(cal.get(Calendar.DATE));// 오늘 날짜 획득
		String today_month = Integer.toString(cal.get(Calendar.MONTH) + 1);// 오늘의 달 획득

		Calendar ca = Calendar.getInstance();
		int year = Integer.parseInt(yearChoice.getSelectedItem().toString());// 선택한 년도
		int month = Integer.parseInt(monthChoice.getSelectedItem().toString());// 선택한 월
		ca.set(year, month - 1, 1);
		int startDay = ca.get(Calendar.DAY_OF_WEEK); // 1일의 요일 얻어오기
		int endDay = ca.getActualMaximum(Calendar.DATE); // 달의 마지막일

		for (int i = 0; i < days.length; i++) {
			days[i].setEnabled(true);
		}
		// 시작일 이전의 버튼을 비활성화
		for (int i = 0; i < startDay - 1; i++)
			days[i].setEnabled(false);
		// 버튼에 날짜 셋팅
		for (int i = startDay; i < startDay + endDay; i++) {
			days[i - 1].setText((String.valueOf(i - startDay + 1)));
			days[i - 1].setBackground(Color.WHITE);
			if (today_month.equals(String.valueOf(month))) {// 오늘이 속한 달과 같은 달인 경우
				if (today.equals(days[i - 1].getText()))// 버튼의 날짜와 오늘날짜가 일치하는 경우
					days[i - 1].setBackground(Color.CYAN);// 버튼의 배경색 지정
			}
		}
		// 날짜가 없는 버튼을 비활성화
		for (int i = (startDay + endDay - 1); i < days.length; i++)
			days[i].setEnabled(false);

		// 미래 날짜 비활성화
		if (cal.get(Calendar.YEAR) == year) {
			if (cal.get(Calendar.MONTH) + 1 == month) {
				for (int i = (startDay + cal.get(Calendar.DATE) - 1); i < days.length; i++)
					days[i].setEnabled(false);
			} else if (cal.get(Calendar.MONTH) + 1 < month) {
				for (int i = startDay; i < startDay + endDay; i++) {
					days[i - 1].setEnabled(false);
				}
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		Color color = this.getBackground();
		if (e.getStateChange() == ItemEvent.SELECTED) {
			for (int i = 0; i < 42; i++) {// 년이나 월이 선택되면 기존의 달력을 지우고 새로 그린다.
				if (!days[i].getText().equals("")) {
					days[i].setText("");// 기존의 날짜를 지움
					days[i].setBackground(color);// 달력의 배경색과 동일한 색으로 버튼의 배경색을 설정함.
				}
			}
			setDate();
		}
	}

	public static void main(String[] args) {
		new IssueLookUpUI();
	}

}
