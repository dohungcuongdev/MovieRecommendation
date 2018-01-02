package com.hcmiu.thesis.mining.movielens.home;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import com.hcmiu.thesis.mining.movielens.helpers.FileHelpers;
import com.hcmiu.thesis.mining.movielens.utils.Constants;
import com.hcmiu.thesis.mining.movielens.utils.FPGrowth;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Font;

public class Main {

	public static int numTransactions;
	public static long treetime;
	public static long E1Time;
	public static long E5Time;
	public static long startTime;
	public static int TestPara;
	public static long currentTime;
	public static double Evaluation1;
	public static double Evaluation5;

	private JFrame frame;
	private JTextField textMinSupport;
	private JTextField textPrecision;
	private File dataFile;
	private JTextField textPeriod;
	private JTextField textSatisfaction;
	private JButton btnBrowseFile;
	private JLabel lblFptreeTime;
	private JLabel lblRunTime;
	private JTextField textPrecision10;
	private JTextField textSatisfaction10;
	private JLabel lblFptreeTime10;
	private JTextField textPrecision20;
	private JTextField textSatisfaction20;
	private JLabel lblFptreeTime20;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setForeground(Color.DARK_GRAY);
		frame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 17));
		frame.setBounds(100, 100, 531, 723);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setTitle("Ready!");

		JButton btnExcecute = new JButton("Execute");
		btnExcecute.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnExcecute.setBackground(Color.ORANGE);
		btnExcecute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.setTitle("Running...");
				// INPUT
				Constants.MIN_SUPPORT = Double.parseDouble(textMinSupport.getText()) / 100;
				Constants.PERIOD = (int) TimeUnit.DAYS.toSeconds(Long.parseLong(textPeriod.getText()));

				startTime = System.currentTimeMillis();
				numTransactions = 0;
				FileHelpers.parseDataToMovies(dataFile, Constants.SPLIT_BY, Constants.MOVIES);
				long parseDateTime = System.currentTimeMillis() - startTime;
				currentTime = parseDateTime;

				Constants.TOP_N = 5;
				while (Constants.TOP_N <= 20 ) {
					for (int i = 1; i <= 5; i++) {
						TestPara = i;
						try {
							FPGrowth fpGrowth = new FPGrowth();
							ArrayList<ArrayList<String>> data = fpGrowth.readFile(Constants.MOVIES, Constants.SPLIT_BY);
							fpGrowth.FPAlgo(data);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					NumberFormat numberFormat = NumberFormat.getPercentInstance();

					if (Constants.TOP_N.equals(5)) {
						// OUTPUT
						textPrecision.setText(numberFormat.format(Evaluation1 / 5));
						textSatisfaction.setText(numberFormat.format(Evaluation5 / 5));

						resetData();
						lblFptreeTime.setText(splitToComponentTimes(treetime));

						Constants.TOP_N = 10;
					} else if (Constants.TOP_N.equals(10)) {
						// OUTPUT
						textPrecision10.setText(numberFormat.format(Evaluation1 / 5));
						textSatisfaction10.setText(numberFormat.format(Evaluation5 / 5));

						resetData();
						lblFptreeTime10.setText(splitToComponentTimes(treetime));

						Constants.TOP_N = 20;
					}else if (Constants.TOP_N.equals(20)) {
						// OUTPUT
						textPrecision20.setText(numberFormat.format(Evaluation1 / 5));
						textSatisfaction20.setText(numberFormat.format(Evaluation5 / 5));

						resetData();
						lblFptreeTime20.setText(splitToComponentTimes(treetime));
						
						Constants.TOP_N = 50;
					}

				}
				long endTime = System.currentTimeMillis() - startTime;
				lblRunTime.setText(splitToComponentTimes(endTime));
				frame.setTitle("Finished!");
			}
		});
		btnExcecute.setBounds(10, 148, 121, 30);
		frame.getContentPane().add(btnExcecute);

		JLabel lblMinSupport = new JLabel("Min Support:");
		lblMinSupport.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblMinSupport.setBounds(10, 80, 121, 23);
		frame.getContentPane().add(lblMinSupport);

		textMinSupport = new JTextField();
		textMinSupport.setFont(new Font("Tahoma", Font.PLAIN, 17));
		textMinSupport.setBounds(160, 80, 108, 23);
		frame.getContentPane().add(textMinSupport);
		textMinSupport.setColumns(10);

		JLabel lblInput = new JLabel("INPUT");
		lblInput.setFont(new Font("Tahoma", Font.BOLD, 25));
		lblInput.setForeground(Color.BLUE);
		lblInput.setBackground(Color.WHITE);
		lblInput.setBounds(10, 11, 108, 23);
		frame.getContentPane().add(lblInput);

		JSeparator separator = new JSeparator();
		separator.setForeground(Color.BLACK);
		separator.setBackground(new Color(0, 0, 255));
		separator.setBounds(10, 189, 496, 10);
		frame.getContentPane().add(separator);

		JLabel lblOutput = new JLabel("OUTPUT: TOP-N RECOMMENDATION");
		lblOutput.setFont(new Font("Tahoma", Font.BOLD, 25));
		lblOutput.setForeground(Color.BLUE);
		lblOutput.setBackground(Color.WHITE);
		lblOutput.setBounds(10, 199, 467, 30);
		frame.getContentPane().add(lblOutput);

		JLabel lblPrecision = new JLabel("Precision:");
		lblPrecision.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblPrecision.setBounds(10, 287, 108, 23);
		frame.getContentPane().add(lblPrecision);

		textPrecision = new JTextField();
		textPrecision.setFont(new Font("Tahoma", Font.PLAIN, 17));
		textPrecision.setEditable(false);
		textPrecision.setBounds(160, 287, 108, 23);
		frame.getContentPane().add(textPrecision);
		textPrecision.setColumns(10);

		JLabel lblData = new JLabel("Data:");
		lblData.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblData.setBounds(10, 45, 72, 23);
		frame.getContentPane().add(lblData);

		btnBrowseFile = new JButton("Browse file");
		btnBrowseFile.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnBrowseFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser("data");
				int returnVal = fileChooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					dataFile = fileChooser.getSelectedFile();
					btnBrowseFile.setText(dataFile.getName());
					btnBrowseFile.setToolTipText(dataFile.getName());
				}
			}
		});
		btnBrowseFile.setBounds(160, 45, 108, 23);
		frame.getContentPane().add(btnBrowseFile);

		JLabel lblPeriod = new JLabel("Period:");
		lblPeriod.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblPeriod.setBounds(10, 114, 93, 23);
		frame.getContentPane().add(lblPeriod);

		textPeriod = new JTextField();
		textPeriod.setFont(new Font("Tahoma", Font.PLAIN, 17));
		textPeriod.setBounds(160, 114, 108, 23);
		frame.getContentPane().add(textPeriod);
		textPeriod.setColumns(10);

		JLabel lblDays = new JLabel("days");
		lblDays.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblDays.setBounds(278, 115, 60, 23);
		frame.getContentPane().add(lblDays);

		JLabel lblSatisfaction = new JLabel("Satisfaction (m=5):");
		lblSatisfaction.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblSatisfaction.setBounds(10, 321, 154, 23);
		frame.getContentPane().add(lblSatisfaction);

		textSatisfaction = new JTextField();
		textSatisfaction.setFont(new Font("Tahoma", Font.PLAIN, 17));
		textSatisfaction.setEditable(false);
		textSatisfaction.setBounds(160, 321, 108, 23);
		frame.getContentPane().add(textSatisfaction);
		textSatisfaction.setColumns(10);

		JLabel lblRT = new JLabel("Runtime:");
		lblRT.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblRT.setBounds(160, 148, 72, 23);
		frame.getContentPane().add(lblRT);

		lblRunTime = new JLabel("");
		lblRunTime.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblRunTime.setBounds(228, 149, 274, 23);
		frame.getContentPane().add(lblRunTime);

		JLabel lblFT = new JLabel("FP-Tree Time:");
		lblFT.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblFT.setBounds(10, 355, 108, 23);
		frame.getContentPane().add(lblFT);

		lblFptreeTime = new JLabel("");
		lblFptreeTime.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblFptreeTime.setBounds(118, 355, 289, 23);
		frame.getContentPane().add(lblFptreeTime);

		JLabel label = new JLabel("%");
		label.setFont(new Font("Tahoma", Font.PLAIN, 15));
		label.setBounds(278, 81, 46, 23);
		frame.getContentPane().add(label);

		JLabel lblN = new JLabel("N = 5");
		lblN.setForeground(new Color(0, 128, 0));
		lblN.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblN.setBounds(10, 253, 72, 23);
		frame.getContentPane().add(lblN);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 240, 496, 2);
		frame.getContentPane().add(separator_1);

		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(10, 389, 496, 2);
		frame.getContentPane().add(separator_2);

		JLabel lblN10 = new JLabel("N = 10");
		lblN10.setForeground(new Color(0, 128, 0));
		lblN10.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblN10.setBounds(10, 402, 93, 23);
		frame.getContentPane().add(lblN10);

		JLabel lblPrecision10 = new JLabel("Precision:");
		lblPrecision10.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblPrecision10.setBounds(10, 436, 108, 23);
		frame.getContentPane().add(lblPrecision10);

		textPrecision10 = new JTextField();
		textPrecision10.setFont(new Font("Tahoma", Font.PLAIN, 17));
		textPrecision10.setEditable(false);
		textPrecision10.setColumns(10);
		textPrecision10.setBounds(160, 436, 108, 23);
		frame.getContentPane().add(textPrecision10);

		JLabel lblSatisfaction10 = new JLabel("Satisfaction (m=5):");
		lblSatisfaction10.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblSatisfaction10.setBounds(10, 470, 154, 23);
		frame.getContentPane().add(lblSatisfaction10);

		textSatisfaction10 = new JTextField();
		textSatisfaction10.setFont(new Font("Tahoma", Font.PLAIN, 17));
		textSatisfaction10.setEditable(false);
		textSatisfaction10.setColumns(10);
		textSatisfaction10.setBounds(160, 470, 108, 23);
		frame.getContentPane().add(textSatisfaction10);

		JLabel lblFT10 = new JLabel("FP-Tree Time:");
		lblFT10.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblFT10.setBounds(10, 504, 108, 23);
		frame.getContentPane().add(lblFT10);

		lblFptreeTime10 = new JLabel("");
		lblFptreeTime10.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblFptreeTime10.setBounds(118, 504, 289, 23);
		frame.getContentPane().add(lblFptreeTime10);

		JSeparator separator_3 = new JSeparator();
		separator_3.setBounds(10, 538, 496, 2);
		frame.getContentPane().add(separator_3);

		JLabel lblN20 = new JLabel("N = 20");
		lblN20.setForeground(new Color(0, 128, 0));
		lblN20.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblN20.setBounds(10, 551, 93, 23);
		frame.getContentPane().add(lblN20);

		JLabel lblPrecision20 = new JLabel("Precision:");
		lblPrecision20.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblPrecision20.setBounds(10, 585, 108, 23);
		frame.getContentPane().add(lblPrecision20);

		textPrecision20 = new JTextField();
		textPrecision20.setFont(new Font("Tahoma", Font.PLAIN, 17));
		textPrecision20.setEditable(false);
		textPrecision20.setColumns(10);
		textPrecision20.setBounds(160, 585, 108, 23);
		frame.getContentPane().add(textPrecision20);

		JLabel lblSatisfaction20 = new JLabel("Satisfaction (m=5):");
		lblSatisfaction20.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblSatisfaction20.setBounds(10, 619, 154, 23);
		frame.getContentPane().add(lblSatisfaction20);

		textSatisfaction20 = new JTextField();
		textSatisfaction20.setFont(new Font("Tahoma", Font.PLAIN, 17));
		textSatisfaction20.setEditable(false);
		textSatisfaction20.setColumns(10);
		textSatisfaction20.setBounds(160, 619, 108, 23);
		frame.getContentPane().add(textSatisfaction20);

		JLabel lblFT20 = new JLabel("FP-Tree Time:");
		lblFT20.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblFT20.setBounds(10, 653, 108, 23);
		frame.getContentPane().add(lblFT20);

		lblFptreeTime20 = new JLabel("");
		lblFptreeTime20.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblFptreeTime20.setBounds(118, 653, 289, 23);
		frame.getContentPane().add(lblFptreeTime20);

	}

	private void resetData() {
		Evaluation1 = 0;
		Evaluation5 = 0;
	}

	public static String splitToComponentTimes(long miliseconds) {
		String runTime = "";
		int days = (int) miliseconds / 86400000;
		if (days > 0) {
			runTime = days + " Days ";
		}
		int remainder = (int) miliseconds - days * 86400000;

		int hours = remainder / 3600000;
		if (hours > 0) {
			runTime += hours + " Hours ";
		}
		remainder = remainder - hours * 3600000;

		int mins = remainder / 60000;
		if (mins > 0) {
			runTime += mins + " Minutes ";
		}
		remainder = remainder - mins * 60000;

		int secs = remainder / 1000;
		if (secs > 0) {
			runTime += secs + " Seconds ";
		} else {
			runTime = miliseconds + " Miliseconds";
		}

		return runTime;
	}
}
