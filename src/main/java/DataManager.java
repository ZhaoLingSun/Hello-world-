import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Random;
import java.util.Scanner;
import java.awt.*;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/*
 * This class provides a GUI for managing data
 * The data is stored in a HashMap in the DataStorage class
 * Following operations are supported:
 * - Load/Refresh Data
 *     - Read data from a file and display it in a JTable
 * - Add Data
 *     - Add new data to the HashMap
 * - Remove Data
 *    - Remove data from the HashMap by key
 * - Update Data
 *   - Update data in the HashMap, if the key exists, update the value, otherwise add a new key-value pair
 * - Save Data
 *   - Write the data to a file
 * - Filter Data
 *   - Filter the data based on a column-value pair
 * - Show Data
 *   - Display the data in the console (unused)
 */

public class DataManager extends JFrame{
    File defaultDir = new File(".");
    String fileName = "data.txt";
    String filePath = null;
    String dataFile = (filePath==null)?fileName:filePath+File.separator+fileName;
    
    File defaultPriceDir = new File(".");
    String priceFileName = "price.txt";
    String priceFilePath = null;
    String priceFile = (priceFilePath==null)?priceFileName:priceFilePath+File.separator+priceFileName;

    String filterValue = null;

    int height = 800;
    int width = 1000;

    private Robot robot;

    DataStorage ds = new DataStorage();
    HashMap<String, HashMap<String, String>> data = ds.getData();
    HashMap<Integer, Double> priceCurve = new HashMap<>();
    HashMap<Integer, Double> hourlyCosts = new HashMap<>();
    HashMap<Integer, List<String>> livingHabits = new HashMap<>();

    JFrame frame = new JFrame();
    JLabel fileDirLabel = new JLabel("File Directory");
    JLabel keyLabel = new JLabel("Data Filter:");
    JLabel valueLabel = new JLabel("with Value");
    JLabel emailLabel = new JLabel("Email:");
    JTextField fileDirField = new JTextField("data.txt");
    JTextField emailField = new JTextField("yq-sun21@mails.tsinghua.edu.cn");
    JTextField priceCurveField = new JTextField("price.txt");
    JTextField livingHabitsField = new JTextField("5:1;");
    JButton openFileButton = new JButton("Open File");
    JButton loadDataButton = new JButton("Load/Refresh Data");
    JButton addDataButton = new JButton("Add Data");
    JButton removeDataButton = new JButton("Remove Data");
    JButton updateDataButton = new JButton("Update Data");
    JButton saveDataButton = new JButton("Save Data");
    JButton sendEmailButton = new JButton("Send Email");
    JButton simulateButton = new JButton("Simulate Electricity Usage");
    JButton loadPriceDataButton = new JButton("Open Price File");
    JButton setPriceCurveButton = new JButton("Set Price Data");
    JButton setLivingHabitsButton = new JButton("Set Living Habits");
    JCheckBox optimizeCheckBox = new JCheckBox("Optimize Usage");
    JComboBox<String> keyComboBox = new JComboBox<String>();
    JComboBox<String> valueComboBox = new JComboBox<String>();
    JTable dataTable = new JTable();
    JScrollPane scrollPane = new JScrollPane(dataTable);
    JPanel upPanel = new JPanel();
    JPanel centerPanel = new JPanel();
    JPanel downPanel = new JPanel();

    JFileChooser fc = new JFileChooser();
    JFileChooser priceFc = new JFileChooser();

    Font largeFont = new Font("Arial", Font.BOLD, 20);
    Boolean optimize = false;
    
    public DataManager() {
        System.out.println(dataFile);
        setSize(width, height);
        setTitle("DataManager");
		setupComponent();
		setupActions();
        this.robot = new Robot(10, 2, 3);
    }

    private void setupComponent() {

		SwingUtilities.invokeLater(() -> {
            int upHeight = (int) (height*0.1);
            int centerHeight = (int) (height*0.2);
            int downHeight = (int) (height*0.7);
            Dimension upDimension = new Dimension(width, upHeight);
            Dimension centerDimension = new Dimension(width, centerHeight);
            Dimension downDimension = new Dimension(width, downHeight);
            // Create a new panel for email input and send button
            JPanel emailPanel = new JPanel(new GridLayout(1, 3));
            emailPanel.add(emailLabel);
            emailPanel.add(emailField);
            emailPanel.add(sendEmailButton);

            JPanel priceCurvePanel = new JPanel(new GridLayout(1, 3));
            priceCurvePanel.add(priceCurveField);
            priceCurvePanel.add(loadPriceDataButton);
            priceCurvePanel.add(setPriceCurveButton);

            JPanel findPanel = new JPanel(new GridLayout(1, 3));
            findPanel.add(keyLabel);
            findPanel.add(keyComboBox);
            findPanel.add(valueComboBox);

            BorderLayout simulatePanelLayout = new BorderLayout();
            JPanel simulatePanel = new JPanel(simulatePanelLayout);            
            simulatePanel.add(simulateButton, BorderLayout.CENTER);
            simulatePanel.add(optimizeCheckBox, BorderLayout.EAST);

            // Create a new panel for living habits input and set button
            JPanel livingHabitsPanel = new JPanel(new GridLayout(1, 3));
            livingHabitsPanel.add(livingHabitsField);
            livingHabitsPanel.add(setLivingHabitsButton);

            upPanel.setPreferredSize(upDimension);
            GridBagConstraints constraints = new GridBagConstraints();
            GridBagLayout gbl = new GridBagLayout();
            constraints.fill = GridBagConstraints.BOTH;
            constraints.weightx = 1.0;  
            constraints.weighty = 1.0;
            upPanel.setLayout(gbl);
            constraints.gridx = 0;
            constraints.gridy = 0;
            upPanel.add(fileDirLabel, constraints);
            constraints.gridx = 1;
            constraints.gridy = 0;
            upPanel.add(fileDirField, constraints);
            constraints.gridx = 2;
            constraints.gridy = 0;
            upPanel.add(openFileButton, constraints);
            constraints.gridx = 3;
            constraints.gridy = 0;
            upPanel.add(loadDataButton, constraints);

            constraints.gridx = 0;
            constraints.gridy = 1;
            upPanel.add(addDataButton, constraints);
            constraints.gridx = 1;
            constraints.gridy = 1;
            upPanel.add(removeDataButton, constraints);
            constraints.gridx = 2;
            constraints.gridy = 1;
            upPanel.add(updateDataButton, constraints);
            constraints.gridx = 3;
            constraints.gridy = 1;
            upPanel.add(saveDataButton, constraints);

            centerPanel.setPreferredSize(centerDimension);
            GridLayout gl = new GridLayout(5, 1);
            centerPanel.setLayout(gl);
            centerPanel.add(priceCurvePanel);
            centerPanel.add(livingHabitsPanel);
            centerPanel.add(simulatePanel);
            centerPanel.add(emailPanel);
            centerPanel.add(findPanel);

            downPanel.setPreferredSize(downDimension);
            BorderLayout dbl = new BorderLayout();
            downPanel.setLayout(dbl);
            downPanel.add(dataTable, BorderLayout.CENTER);

            BorderLayout bl = new BorderLayout();
            setLayout(bl);
            add(upPanel, BorderLayout.NORTH);
            add(centerPanel, BorderLayout.CENTER);
            add(downPanel, BorderLayout.SOUTH);
        });
    }

    private void setupActions() {
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				int width = getWidth();
				int height = getHeight();
				
                int upHeight = (int) (height*0.1);
                int centerHeight = (int) (height*0.2);
                int downHeight = (int) (height*0.7);
		
				Dimension upSize = new Dimension(width, upHeight);
                Dimension centerSize = new Dimension(width, centerHeight);
				Dimension downSize = new Dimension(width, downHeight);

				upPanel.setPreferredSize(upSize);
                centerPanel.setPreferredSize(centerSize);
				downPanel.setPreferredSize(downSize);

                frame.revalidate();
                frame.repaint();

			}
		});
        
        fileDirField.addActionListener(e -> {
            dataFile = fileDirField.getText();
            loadData(dataFile);
        });
        openFileButton.addActionListener(e -> {
            fc.setCurrentDirectory(defaultDir);
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fc.showOpenDialog(null);
            File file = fc.getSelectedFile();
            if (file != null) {
                fileName = file.getName();
                filePath = file.getParent();
                dataFile = (filePath==null)?fileName:filePath+File.separator+fileName;
                fileDirField.setText(dataFile);
            }
        });
        loadDataButton.addActionListener(e -> {
            dataFile = fileDirField.getText();
            loadData(dataFile);
        });
        addDataButton.addActionListener(e -> {
            JDialog dialog = new JDialog();
            dialog.setSize(400, 400);
            dialog.setLayout(new GridLayout(8, 2));
            dialog.setTitle("Add Data");
            JLabel newIndexLabel = new JLabel("Index:");
            JLabel newStatusLabel = new JLabel("Status:");
            JLabel newTypeLabel = new JLabel("Type:");
            JLabel newVoltageLabel = new JLabel("Voltage:");
            JLabel newFrequencyLabel = new JLabel("Frequency:");
            JLabel newPowerLabel = new JLabel("Power:");
            JLabel newNameLabel = new JLabel("Name:");
            JButton confirmButton = new JButton("Confirm");

            JTextField newIndexField = new JTextField();
            JTextField newStatusField = new JTextField();
            JTextField newTypeField = new JTextField();
            JTextField newVoltageField = new JTextField();
            JTextField newFrequencyField = new JTextField();
            JTextField newPowerField = new JTextField();
            JTextField newNameField = new JTextField();
            JButton cancelButton = new JButton("Cancel");

            dialog.add(newIndexLabel);
            dialog.add(newIndexField);
            dialog.add(newStatusLabel);
            dialog.add(newStatusField);
            dialog.add(newTypeLabel);
            dialog.add(newTypeField);
            dialog.add(newVoltageLabel);
            dialog.add(newVoltageField);
            dialog.add(newFrequencyLabel);
            dialog.add(newFrequencyField);
            dialog.add(newPowerLabel);
            dialog.add(newPowerField);
            dialog.add(newNameLabel);
            dialog.add(newNameField);
            dialog.add(confirmButton);
            dialog.add(cancelButton);
            dialog.setVisible(true);

            confirmButton.addActionListener(e1 -> {
                String index = newIndexField.getText();
                String status = newStatusField.getText();
                String type = newTypeField.getText();
                String voltage = newVoltageField.getText();
                String frequency = newFrequencyField.getText();
                String power = newPowerField.getText();
                String name = newNameField.getText();
                HashMap<String, String> value = new HashMap<>();
                value.put("Index", index);
                value.put("Status", status);
                value.put("Type", type);
                value.put("Voltage", voltage);
                value.put("Frequency", frequency);
                value.put("Power", power);
                value.put("Name", name);
                ds.addData(name, value);
                JOptionPane.showMessageDialog(null, "Data Added Successfully! Click Save Data to save the changes.");
                dialog.dispose();
            });
            cancelButton.addActionListener(e2 -> {
                dialog.dispose();
            });
        });
        removeDataButton.addActionListener(e -> {
            JDialog dialog = new JDialog();
            dialog.setSize(400, 200);
            dialog.setLayout(new GridLayout(2, 2));
            dialog.setTitle("Remove Data");
            JLabel indexLabel = new JLabel("Target Index:");
            JTextField indexField = new JTextField();
            JButton confirmButton = new JButton("Confirm");
            JButton cancelButton = new JButton("Cancel");
            dialog.add(indexLabel);
            dialog.add(indexField);
            dialog.add(confirmButton);
            dialog.add(cancelButton);
            dialog.setVisible(true);

            confirmButton.addActionListener(e1 -> {
                String index = indexField.getText();
                ds.removeData(index);
                if (data.containsKey(index)) {
                    JOptionPane.showMessageDialog(null, "Data Removed Successfully! Click Save Data to save the changes.");
                } else {
                    JOptionPane.showMessageDialog(null, "Data Not Found!");
                }
                dialog.dispose();
            });
            cancelButton.addActionListener(e2 -> {
                dialog.dispose();
            });
        });
        updateDataButton.addActionListener(e -> {
            JDialog dialog = new JDialog();
            dialog.setSize(400, 400);
            dialog.setLayout(new GridLayout(8, 2));
            dialog.setTitle("Add Data");
            JLabel newIndexLabel = new JLabel("Index:");
            JLabel newStatusLabel = new JLabel("Status:");
            JLabel newTypeLabel = new JLabel("Type:");
            JLabel newVoltageLabel = new JLabel("Voltage:");
            JLabel newFrequencyLabel = new JLabel("Frequency:");
            JLabel newPowerLabel = new JLabel("Power:");
            JLabel newNameLabel = new JLabel("Name:");
            JButton confirmButton = new JButton("Confirm");

            JTextField newIndexField = new JTextField();
            JTextField newStatusField = new JTextField();
            JTextField newTypeField = new JTextField();
            JTextField newVoltageField = new JTextField();
            JTextField newFrequencyField = new JTextField();
            JTextField newPowerField = new JTextField();
            JTextField newNameField = new JTextField();
            JButton cancelButton = new JButton("Cancel");

            dialog.add(newIndexLabel);
            dialog.add(newIndexField);
            dialog.add(newStatusLabel);
            dialog.add(newStatusField);
            dialog.add(newTypeLabel);
            dialog.add(newTypeField);
            dialog.add(newVoltageLabel);
            dialog.add(newVoltageField);
            dialog.add(newFrequencyLabel);
            dialog.add(newFrequencyField);
            dialog.add(newPowerLabel);
            dialog.add(newPowerField);
            dialog.add(newNameLabel);
            dialog.add(newNameField);
            dialog.add(confirmButton);
            dialog.add(cancelButton);
            dialog.setVisible(true);

            confirmButton.addActionListener(e1 -> {
                String index = newIndexField.getText();
                String status = newStatusField.getText();
                String type = newTypeField.getText();
                String voltage = newVoltageField.getText();
                String frequency = newFrequencyField.getText();
                String power = newPowerField.getText();
                String name = newNameField.getText();
                ds.updateData(index, "Status", status);
                ds.updateData(index, "Type", type);
                ds.updateData(index, "Voltage", voltage);
                ds.updateData(index, "Frequency", frequency);
                ds.updateData(index, "Power", power);
                ds.updateData(index, "Name", name);
                JOptionPane.showMessageDialog(null, "Data Updated Successfully! Click Save Data to save the changes.");
                dialog.dispose();
            });
            cancelButton.addActionListener(e2 -> {
                dialog.dispose();
            });
        });
        saveDataButton.addActionListener(e -> {
            JDialog dialog = new JDialog();
            dialog.setSize(400, 400);
            GridBagConstraints gbc = new GridBagConstraints();
            GridBagLayout gbl = new GridBagLayout();
            dialog.setLayout(gbl);
            dialog.setTitle("Save Data");
            JLabel saveLabel = new JLabel();
            saveLabel.setFont(largeFont);
            saveLabel.setText("<html>Save Current data to File?<br>This will overwrite the original file.</html>");
            JButton confirmButton = new JButton("Confirm");
            JButton cancelButton = new JButton("Cancel");
            
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weightx = 1.0; 
            gbc.weighty = 5.0; 
            gbc.gridx = 0; 
            gbc.gridy = 0; 
            gbc.gridwidth = 2;
            dialog.add(saveLabel, gbc);
            
            gbc.gridwidth = 1; 
            gbc.weighty = 1.0; 
            gbc.gridx = 0;
            gbc.gridy = 1; 
            dialog.add(confirmButton, gbc);
            
            gbc.gridx = 1; 
            dialog.add(cancelButton, gbc);
            
            dialog.setVisible(true);
            confirmButton.addActionListener(e1 -> {
                ds.writeDataToFile(dataFile);
                JOptionPane.showMessageDialog(null, "Data Saved Successfully!");
                dialog.dispose();
            });
            cancelButton.addActionListener(e2 -> {
                dialog.dispose();
            });
        });
        valueComboBox.addActionListener(e -> {
            HashMap<String, HashMap<String, String>> filteredData = new HashMap<>();
            for (String key : data.keySet()) {
                HashMap<String, String> subMap = data.get(key);
                if (subMap.containsKey(keyComboBox.getSelectedItem()) && subMap.get(keyComboBox.getSelectedItem()).equals(valueComboBox.getSelectedItem())) {
                    filteredData.put(key, subMap);
                }
            }
            Set<String> columnNamesSet = new LinkedHashSet<>();
            for (HashMap<String, String> subMap : filteredData.values()) {
                columnNamesSet.addAll(subMap.keySet());
            }
            String[] columnNames = columnNamesSet.toArray(new String[0]);
            TreeSet<String> sortedKeys = new TreeSet<>(filteredData.keySet());
            tableGenerate(sortedKeys, filteredData, columnNames);
        });
        keyComboBox.addActionListener(e -> {
            valueComboBox.removeAllItems();
            String key = (String) keyComboBox.getSelectedItem();
            Set<String> columnNamesSet = new LinkedHashSet<>();
            if (key.equals("ALL")) {
                for (HashMap<String, String> subMap : data.values()) {
                    columnNamesSet.addAll(subMap.keySet());
                }
                String[] columnNames = columnNamesSet.toArray(new String[0]);
                
                TreeSet<String> sortedKeys = new TreeSet<>(data.keySet());
                tableGenerate(sortedKeys, data, columnNames);
            } else {
                Set<String> keySet = new TreeSet<>();
                for (HashMap<String, String> subMap : data.values()) {
                    // Check if the key exists in the subMap
                    if (subMap.containsKey(key)) {
                        String value = subMap.get(key);
                        keySet.add(value);
                    } 
                }
                // Update valueComboBox 
                for (String value : keySet) {
                        valueComboBox.addItem(value);
                    }
            }
        });
        sendEmailButton.addActionListener(e -> {
            String email = emailField.getText();
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter an Email address.");
            } else {
                String subject = "Building Electricity Management Report";
                String body = "Here is your report on the building's electricity management.";
                sendEmail(email, subject, body);
            }
        });
        simulateButton.addActionListener(e -> {
            optimize = optimizeCheckBox.isSelected();
            simulateElectricityUsage(optimize);
        });
        loadPriceDataButton.addActionListener(e -> {
            priceFc.setCurrentDirectory(defaultPriceDir);
            priceFc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            priceFc.showOpenDialog(null);
            File file = priceFc.getSelectedFile();
            if (file != null) {
                priceFileName = file.getName();
                priceFilePath = file.getParent();
                priceFile = (priceFilePath==null)?priceFileName:priceFilePath+File.separator+priceFileName;
                priceCurveField.setText(priceFile);
            }
            else{
                JOptionPane.showMessageDialog(null, "Error: Price file not found.");
            }
        });
        setPriceCurveButton.addActionListener(e -> {
            priceFile = priceCurveField.getText();
            setPriceCurve(priceCurve, priceFile);
        });
        setLivingHabitsButton.addActionListener(e -> {
            setLivingHabits();
        });
    }

    private void loadData(String fileDir) {
        // Read data from file selected by user
        ds.readDataFromFile(fileDir);
        // Use LinkedHashSet to keep the order of column names
        Set<String> columnNamesSet = new LinkedHashSet<>();
        for (HashMap<String, String> subMap : data.values()) {
            columnNamesSet.addAll(subMap.keySet());
        }
        String[] columnNames = columnNamesSet.toArray(new String[0]);
        
        TreeSet<String> sortedKeys = new TreeSet<>(data.keySet());
        tableGenerate(sortedKeys, data, columnNames);
        // Update keyComboBox
        keyComboBox.removeAllItems();
        keyComboBox.addItem("ALL");
        keyComboBox.setSelectedItem("ALL");
        for (String feature : columnNames) {
            keyComboBox.addItem(feature);
        }
    }

    private void tableGenerate(TreeSet<String> keys, HashMap<String, HashMap<String, String>> data, String[] columnNames) {
        // Create a 2D array to store the data for JTable
        int row = 0;
        String[] columnNamesWithIndex = new String[columnNames.length + 1];
        columnNamesWithIndex[0] = "Index";
        System.arraycopy(columnNames, 0, columnNamesWithIndex, 1, columnNames.length);   
        String[][] tableData = new String[data.size()][columnNames.length + 1];
        for (String key : keys) {
            HashMap<String, String> subMap = data.get(key);
            tableData[row][0] = key;
            int col = 1;
            for (String columnName : columnNames) {
                // 
                tableData[row][col] = subMap.getOrDefault(columnName, "");
                col++;
            }
            row++;
        }
        // Create a JTable and add it to the scrollPane
        dataTable = new JTable(tableData, columnNamesWithIndex);
        scrollPane = new JScrollPane(dataTable);
        downPanel.removeAll();
        downPanel.add(scrollPane, BorderLayout.CENTER);
        downPanel.revalidate();
        downPanel.repaint();
    }


    private void sendEmail(String to, String subject, String body) {
        String from = "597744685@qq.com";
        String host = "smtp.qq.com";
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");

        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("597744685@qq.com", "glsmbwagedcobbii");
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            body += "\n\nToday's highest load: " + maxLoad + " kWh";
            body += "\nToday's lowest load: " + minLoad + " kWh";
            body += "\nToday's highest cost: " + maxCost + " USD";
            body += "\nToday's lowest cost: " + minCost + " USD";
            message.setText(body);
            Transport.send(message);
            JOptionPane.showMessageDialog(null, "Email sent successfully.");
        } catch (MessagingException mex) {
            mex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to send Email. Please check the console for more details.");
        }
    }

    private void setPriceCurve(HashMap<Integer, Double> priceCurve, String priceFile) {
        try {
            File file = new File(priceFile);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(":");
                if (parts.length == 2 && parts[0].matches("\\d+") 
                    && parts[1].matches("\\d+(\\.\\d+)?") && Double.parseDouble(parts[1]) >= 0 
                    && Double.parseDouble(parts[0]) <= 23 && Double.parseDouble(parts[0]) >= 0){
                    try {
                        int key = Integer.parseInt(parts[0]);
                        double value = Double.parseDouble(parts[1]);
                        priceCurve.put(key, value);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "价格数据格式错误，无法解析为整数或双精度浮点数: " + Arrays.toString(parts), "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            if (priceCurve.size() > 0){
                JFrame frame = new JFrame("价格曲线");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(400, 300);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setVisible(true);
                PriceCurvePanel panel = new PriceCurvePanel(priceCurve);
                frame.add(panel);
                frame.setVisible(true);
            }else{
                JOptionPane.showMessageDialog(null, "未读取到符合格式的数据: " + priceFile, "错误", JOptionPane.ERROR_MESSAGE);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "文件未找到: " + priceFile, "错误", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void setLivingHabits() {
        String[] habits = livingHabitsField.getText().split(";");
        for (String habit : habits) {
            String[] parts = habit.split(":");
            if (parts.length < 2) {
                JOptionPane.showMessageDialog(null, "Invalid input format. Please use the format 'hour:type;hour:type;...'");
                continue; 
            }
            int hour = Integer.parseInt(parts[0]);
            String type = parts[1];
            livingHabits.computeIfAbsent(hour, k -> new ArrayList<>()).add(type);
        }
        JOptionPane.showMessageDialog(null, "Living habits set successfully.");
    }


    private double maxLoad = 0;
    private double minLoad = Double.MAX_VALUE;
    private double maxCost = 0;
    private double minCost = Double.MAX_VALUE;

    private void simulateElectricityUsage(Boolean optimize) {
        // Simulate electricity usage for 24 hours
        int simulationHours = 24;
        double totalEnergyConsumed = 0;
        double totalCost = 0;
        HashMap<String, Double> energyConsumptionMap = new HashMap<>();
        Random random = new Random();

        for (int hour = 0; hour < simulationHours; hour++) {
            double hourlyCost = 0;
            double hourlyLoad = 0;
            for (String key : ds.getData().keySet()) {
                HashMap<String, String> appliance = ds.getData().get(key);
                String status = appliance.get("Status");
                String type = appliance.get("Type");
                double power = Double.parseDouble(appliance.get("Power"));

                if (optimize.equals(true)) {
                    status = optimizeUsage(hour, type, power, status, random);
                } else {
                    if (livingHabits.containsKey(hour)) {
                        if (livingHabits.get(hour).contains(type)) {
                            status = "ON";
                        } else {
                            status = "OFF";
                        }
                    } else {
                        if (status.equals("OFF")) {
                            if (type.equals("1")) {
                                status = "ON";
                            } else if (type.equals("2") || type.equals("3")) {
                                if (random.nextBoolean()) {
                                    status = "ON";
                                }
                            }
                        } else {
                            if (random.nextBoolean()) {
                                status = "OFF";
                            }
                        }
                    }
                }
                appliance.put("Status", status);
                ds.updateData(key, "Status", status);

                // Calculate energy consumption if the appliance is ON
                if (status.equals("ON")) {
                    double energyConsumed = power / 1000; // Convert to kWh
                    totalEnergyConsumed += energyConsumed;
                    hourlyCost += energyConsumed * priceCurve.get(hour % 24); // Apply the price curve
                    energyConsumptionMap.put(key, energyConsumptionMap.getOrDefault(key, 0.0) + energyConsumed);
                }
            }
            totalCost += hourlyCost;
            hourlyCosts.put(hour, hourlyCost);

            // Determine whether to charge or discharge the robot
            if (priceCurve.get(hour % 24) < 0.15) { 
                hourlyLoad += robot.charge();
            } else {
                hourlyLoad -= robot.discharge();
            }

            totalCost += hourlyCost;
            hourlyCosts.put(hour, hourlyCost);

            // Update max and min load
            if (hourlyLoad > maxLoad) {
                maxLoad = hourlyLoad;
                maxCost = hourlyCost;
            }
            if (hourlyLoad < minLoad) {
                minLoad = hourlyLoad;
                minCost = hourlyCost;
            }
        }

        // Display simulation results
        StringBuilder result = new StringBuilder("Simulation Results:\n");
        result.append("Total Energy Consumed: ").append(totalEnergyConsumed).append(" kWh\n");
        result.append("Total Cost: ").append(totalCost).append(" USD\n");
        result.append("Energy Consumption by Appliance:\n");
        for (String key : energyConsumptionMap.keySet()) {
            result.append(key).append(": ").append(energyConsumptionMap.get(key)).append(" kWh\n");
        }
        JOptionPane.showMessageDialog(null, result.toString());

        // Generate the cost chart
        generateCostChart();
    }


    private String optimizeUsage(int hour, String type, double power, String status, Random random) {
        if (priceCurve != null) {
            if(priceCurve.isEmpty()){
                JOptionPane.showMessageDialog(null, "Price curve is empty. Please set the price curve first.");
                return status;
            }
        }else{
            JOptionPane.showMessageDialog(null, "Price curve is not set. Please set the price curve first.");
            return status;
        }
        if (!priceCurve.containsKey(hour % 24)) {
            return status;
        }
        double currentPrice = priceCurve.get(hour % 24);
        if (currentPrice < 0.15) { 
            if (type.equals("2") || type.equals("3")) {
                return "ON";
            }
        } else {
            if (livingHabits.containsKey(hour) && livingHabits.get(hour).contains(type)) {
                return "ON";
            } else {
                return "OFF";
            }
        }

        if (status.equals("OFF")) {
            if (type.equals("1")) {
                return "ON";
            } else if (type.equals("2") || type.equals("3")) {
                if (random.nextBoolean()) {
                    return "ON";
                }
            }
        } else {
            if (random.nextBoolean()) {
                return "OFF";
            }
        }
        return status;
    }

    private void generateCostChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Integer hour : hourlyCosts.keySet()) {
            dataset.addValue(hourlyCosts.get(hour), "Cost", hour);
        }

        JFreeChart barChart = ChartFactory.createBarChart(
            "Hourly Electricity Cost",
            "Hour",
            "Cost (USD)",
            dataset,
            PlotOrientation.VERTICAL,
            true, true, false);

        ChartPanel chartPanel = new ChartPanel(barChart);
        JFrame chartFrame = new JFrame();
        chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        chartFrame.add(chartPanel);
        chartFrame.pack();
        chartFrame.setVisible(true);
    }

    // private void setupMenu() {
    //     // TODO Auto-generated method stub
    // }

    class PriceCurvePanel extends JPanel {
        private HashMap<Integer, Double> priceCurve;
    
        public PriceCurvePanel(HashMap<Integer, Double> priceCurve) {
            this.priceCurve = priceCurve;
        }
    
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.drawString("价格曲线图", getWidth() / 2 - 40, 20);
            g.setColor(Color.BLACK);
            g.fillRect(10, getHeight() - 30, 10, 10); 
            g.drawString("每小时价格", 25, getHeight() - 20);

            int xAxisY = getHeight() - 50; 
            int yAxisX = 50; 
            g.drawLine(yAxisX, 30, yAxisX, xAxisY);
            g.drawLine(yAxisX, xAxisY, getWidth() - 10, xAxisY); 

            for (int i = 0; i <= 24; i++) {
                int x = yAxisX + i * (getWidth() - 60) / 24;
                g.drawLine(x, xAxisY, x, xAxisY + 5); 
                g.drawString(String.valueOf(i), x - 5, xAxisY + 20); 
            }

            double maxValue = Collections.max(priceCurve.values());
            int numTicks = 5; 
            for (int i = 0; i <= numTicks; i++) {
                int y = xAxisY - i * (xAxisY - 30) / numTicks;
                g.drawLine(yAxisX - 5, y, yAxisX, y); 
                String label = String.format("%.2f", i * maxValue / numTicks);
                g.drawString(label, 5, y + 5); 
            }

            if (priceCurve != null && !priceCurve.isEmpty()) {
                int prevX = -1, prevY = -1;
                for (Map.Entry<Integer, Double> entry : priceCurve.entrySet()) {
                    int x = yAxisX + entry.getKey() * (getWidth() - 60) / 24; 
                    int y = xAxisY - (int)(entry.getValue() * (xAxisY - 30) / maxValue); 
                    g.fillOval(x - 2, y - 2, 4, 4);
                    if (prevX != -1 && prevY != -1) {
                        g.drawLine(prevX, prevY, x, y);
                    }
                    prevX = x;
                    prevY = y;
                }
            }
        }
    }

    public static void main(String[] args) {
		DataManager dm = new DataManager();
        dm.setVisible(true);
	}
}   
