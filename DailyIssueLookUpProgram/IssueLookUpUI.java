package DailyIssueLookUpProgram;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class IssueLookUpUI extends JFrame implements ItemListener {
	JTextArea keywordArea; // �α� �˻��� ���
	JTextArea newsArea; // ���� ���� ���
	private JPanel outputPanel; // ��� ��� �г�
	private JPanel calendarPanel; // �޷� �г�
	private JPanel choicePanel; // ��, �� ���� �г�
	JComboBox<String> yearChoice, monthChoice; // ��,�� ���� �޺� �ڽ�
	private JButton[] days = new JButton[42];// ��¥ ��ư. 7���� 6���̹Ƿ� 42���� ��ư�ʿ�
	JLabel dateLabel; // ���� ��¥ ǥ�� ��
	private Calendar cal = Calendar.getInstance();

	public IssueLookUpUI() {
		setTitle(
				"���� ��¥:" + cal.get(Calendar.YEAR) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DATE));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1100, 600);

		// ȭ���� ����� ���
		Dimension dimen = Toolkit.getDefaultToolkit().getScreenSize();
		int xpos = (int) ((dimen.getWidth() - this.getWidth()) / 2);
		int ypos = (int) ((dimen.getHeight() - this.getHeight()) / 2);
		setLocation(xpos, ypos);

		// ��, �� ���� �޺��ڽ�
		yearChoice = new JComboBox<String>();
		monthChoice = new JComboBox<String>();

		outputPanel = new JPanel();
		calendarPanel = new JPanel(new GridLayout(7, 7, 5, 5));// 7�� 7���� �׸��巹�̾ƿ�
		choicePanel = new JPanel();
		makeOutputArea();
		makeCalendar();

		this.add(choicePanel, "North");// ������ ���� ������ �� �ִ� ȭ���� ��ܿ� ���
		this.add(calendarPanel, "Center");
		this.add(outputPanel, "East");

		setVisible(true);
	}

	public void makeOutputArea() {
		// ũ�Ѹ� ��� ��� �г�
		outputPanel.setLayout(new BorderLayout());
		dateLabel = new JLabel(); 
		dateLabel.setFont(new Font("Arial", Font.BOLD, 20));
		dateLabel.setHorizontalAlignment(JLabel.CENTER);

		// ���� �г�
		JPanel newsPanel = new JPanel();
		newsPanel.setLayout(new BorderLayout());
		JLabel newsLabel = new JLabel("Naver main ����");
		newsLabel.setFont(new Font("Gothic", Font.BOLD, 15));
		newsLabel.setHorizontalAlignment(JLabel.CENTER);
		newsArea = new JTextArea(40, 40);
		newsArea.setCaretPosition(newsArea.getDocument().getLength()); // ���� �ǳ��� ����Ʈ ��ġ ��Ŵ
		JScrollPane newsPane = new JScrollPane(newsArea);

		newsPanel.add(newsLabel, "North");
		newsPanel.add(newsPane, "Center");

		// �α� �˻��� �г�
		JPanel kwPanel = new JPanel();
		kwPanel.setLayout(new BorderLayout());
		JLabel kwLabel = new JLabel("Naver �α� �˻���");
		kwLabel.setFont(new Font("Gothic", Font.BOLD, 15));
		kwLabel.setHorizontalAlignment(JLabel.CENTER);
		keywordArea = new JTextArea(40, 25);
		keywordArea.setCaretPosition(keywordArea.getDocument().getLength()); // ���� �ǳ��� ����Ʈ ��ġ ��Ŵ
		JScrollPane keywordPane = new JScrollPane(keywordArea);

		kwPanel.add(kwLabel, "North");
		kwPanel.add(keywordPane, "Center");

		// ũ�Ѹ� ��� ��� �гο� ����
		outputPanel.add(dateLabel, "North");
		outputPanel.add(newsPanel, "West");
		outputPanel.add(kwPanel, "East");
	}

	public void makeCalendar() {
		JLabel[] dayLabel = new JLabel[7];
		String[] day = { "��", "��", "ȭ", "��", "��", "��", "��" };
		// ��, �� ���� â�� ������ �ޱ�
		for (int i = 2020; i >= 2015; i--)
			yearChoice.addItem(String.valueOf(i));
		for (int i = 1; i <= 12; i++)
			monthChoice.addItem(String.valueOf(i));
		JLabel yearLabel = new JLabel("��");
		JLabel monthLabel = new JLabel("��");
		choicePanel.add(yearChoice);
		choicePanel.add(yearLabel);
		choicePanel.add(monthChoice);
		choicePanel.add(monthLabel);

		// ���� �̸��� ���̺� ���
		for (int i = 0; i < day.length; i++) {
			dayLabel[i] = new JLabel(day[i], JLabel.CENTER);
			calendarPanel.add(dayLabel[i]);
			dayLabel[i].setBackground(Color.GRAY);// ��ǻ� �ǹ̰� ����. �ٲ��� ����.
		}
		dayLabel[6].setForeground(Color.BLUE);// ����� ����
		dayLabel[0].setForeground(Color.RED);// �Ͽ��� ����

		CrawlingActionListener listener = new CrawlingActionListener(this);
		// ��¥ ��ư ����
		for (int i = 0; i < 42; i++) {// ��� 42���� ��ư�� ����
			days[i] = new JButton("");// ������ ���� ��ư ����
			if (i % 7 == 0)
				days[i].setForeground(Color.RED);// �Ͽ��� ��ư�� ��
			else if (i % 7 == 6)
				days[i].setForeground(Color.BLUE);// ����� ��ư�� ��
			else
				days[i].setForeground(Color.BLACK);
			days[i].addActionListener(listener);// ������ ��¥�� ����� ���� ������ ũ�Ѹ��Ͽ� TextArea�� append
			calendarPanel.add(days[i]);
		}

		// �޺��ڽ��� �⺻ �� ����, ������ ������ �ޱ�
		String m = (cal.get(Calendar.MONTH) + 1) + "";
		String y = cal.get(Calendar.YEAR) + "";
		yearChoice.setSelectedItem(y);
		monthChoice.setSelectedItem(m);
		yearChoice.addItemListener(this);
		monthChoice.addItemListener(this);

		setDate(); // ��ư�� ��¥ ����
	}

	// ��ư�� ��¥ ����
	public void setDate() {
		String today = Integer.toString(cal.get(Calendar.DATE));// ���� ��¥ ȹ��
		String today_month = Integer.toString(cal.get(Calendar.MONTH) + 1);// ������ �� ȹ��

		Calendar ca = Calendar.getInstance();
		int year = Integer.parseInt(yearChoice.getSelectedItem().toString());// ������ �⵵
		int month = Integer.parseInt(monthChoice.getSelectedItem().toString());// ������ ��
		ca.set(year, month - 1, 1);
		int startDay = ca.get(Calendar.DAY_OF_WEEK); // 1���� ���� ������
		int endDay = ca.getActualMaximum(Calendar.DATE); // ���� ��������

		for (int i = 0; i < days.length; i++) {
			days[i].setEnabled(true);
		}
		// ������ ������ ��ư�� ��Ȱ��ȭ
		for (int i = 0; i < startDay - 1; i++)
			days[i].setEnabled(false);
		// ��ư�� ��¥ ����
		for (int i = startDay; i < startDay + endDay; i++) {
			days[i - 1].setText((String.valueOf(i - startDay + 1)));
			days[i - 1].setBackground(Color.WHITE);
			if (today_month.equals(String.valueOf(month))) {// ������ ���� �ް� ���� ���� ���
				if (today.equals(days[i - 1].getText()))// ��ư�� ��¥�� ���ó�¥�� ��ġ�ϴ� ���
					days[i - 1].setBackground(Color.CYAN);// ��ư�� ���� ����
			}
		}
		// ��¥�� ���� ��ư�� ��Ȱ��ȭ
		for (int i = (startDay + endDay - 1); i < days.length; i++)
			days[i].setEnabled(false);

		// �̷� ��¥ ��Ȱ��ȭ
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
			for (int i = 0; i < 42; i++) {// ���̳� ���� ���õǸ� ������ �޷��� ����� ���� �׸���.
				if (!days[i].getText().equals("")) {
					days[i].setText("");// ������ ��¥�� ����
					days[i].setBackground(color);// �޷��� ������ ������ ������ ��ư�� ������ ������.
				}
			}
			setDate();
		}
	}

	public static void main(String[] args) {
		new IssueLookUpUI();
	}

}
