package bangjdev.judger.main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class GUI extends JFrame {

	private static final long serialVersionUID = 9053221221290669191L;

	private static JMenuBar mainBar = new JMenuBar();
	private static JMenu mnFile = new JMenu("File");
	private static JMenuItem mniLoadTask = new JMenuItem("Update tests");
	private static JMenuItem mniLoadContestant = new JMenuItem("Update contestants");
	private static JMenuItem mniQuit = new JMenuItem("Quit");
	private static JMenu mnHelp = new JMenu("Help");
	private static JMenuItem mniAbout = new JMenuItem("About");
	private static JMenuItem mniHelp = new JMenuItem("How to use");
	private static JLabel lblScrTab = new JLabel("Score");
	private static JScrollPane scrTable = new JScrollPane();
	private static JTable tblScr = new JTable();
	private static DefaultTableModel tmScr = new DefaultTableModel();
	private static DbUpdate db = new DbUpdate();

	private static String testDir = null, conDir = null;

	public static DbUpdate getDb() {
		return db;
	}

	public static void setDb(DbUpdate db) {
		GUI.db = db;
	}

	public static String getTestDir() {
		return testDir;
	}

	public static void setTestDir(String testDir) {
		GUI.testDir = testDir;
	}

	public static String getConDir() {
		return conDir;
	}

	public static void setConDir(String conDir) {
		GUI.conDir = conDir;
	}

	public void updateConfig() {
		try {
			PrintWriter pw = new PrintWriter(new File("judger.cfg"));
			pw.println(testDir);
			pw.println(conDir);
			pw.flush();
			pw.close();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void loadConfigs() {
		File configs = new File("judger.cfg");
		try {
			Scanner sc = new Scanner(configs);
			setTestDir(sc.nextLine());
			setConDir(sc.nextLine());
			sc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public GUI() {
		loadConfigs();
		setTitle("Cross - platform judger");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		setLayout(new BorderLayout());
		initComponents();
		pack();
	}

	public void initMenu() {
		mnFile.add(mniLoadContestant);
		mnFile.add(mniLoadTask);
		mnFile.add(mniQuit);

		mnHelp.add(mniAbout);
		mnHelp.add(mniHelp);

		mainBar.add(mnFile);
		mainBar.add(mnHelp);

	}

	public void initScrTab() {

		tblScr.setModel(tmScr);
		tmScr.setDataVector(db.loadContestants(new File(conDir)), db.loadTest(new File(testDir)));

		scrTable.setViewportView(tblScr);
		add(lblScrTab, BorderLayout.NORTH);
		add(scrTable, BorderLayout.CENTER);
	}

	public void initComponents() {
		initMenu();
		initScrTab();

		// add(mainBar, BorderLayout.NORTH);
		setJMenuBar(mainBar);

		mniLoadTask.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser taskChooser = new JFileChooser();
				taskChooser.setAcceptAllFileFilterUsed(false);
				taskChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int choice = taskChooser.showDialog(null, "Select this folder");
				if (choice == JFileChooser.APPROVE_OPTION) {
					Vector<String> cols = db.loadTest(taskChooser.getSelectedFile());
					tmScr.setDataVector(tmScr.getDataVector(), cols);
					testDir = taskChooser.getSelectedFile().getPath();
					System.out.println(testDir);
					updateConfig();
				}
			}
		});

		mniLoadContestant.addActionListener(new ActionListener() {

			@SuppressWarnings("rawtypes")
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int choice = chooser.showDialog(null, "Select this folder");
				if (choice == JFileChooser.APPROVE_OPTION) {
					Vector<Vector> tblData = db.loadContestants(chooser.getSelectedFile());
					Vector<String> cols = new Vector<String>();
					cols.add("Contestants");
					if (testDir != null)
						tmScr.setDataVector(tblData, db.loadTest(new File(testDir)));
					else
						tmScr.setDataVector(tblData, cols);
					conDir = chooser.getSelectedFile().getAbsolutePath();
					updateConfig();
				}
			}
		});
	}

}
