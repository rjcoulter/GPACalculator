// Ryan Coulter, rjc8ez, HW 5
// default list and JList methods were found on stackoverflow
import javax.swing.*;
import java.util.ArrayList;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GpaFrame extends JFrame {

	// Initialize fields that are going to be used
	private JPanel courseInputPanel, courseDisplayPanel, gpaPanel;
	private JLabel hoursLabel, gpaLabel, nameLabel;
	private JComboBox courseBox;
	private JTextField courseHoursInput, courseGPAInput, courseNameInput, calcGPADisplay, targetInput;
	private JButton enterCourse, removeOneCourse, clearCourse, addHours, calcGPA, targetGpa;
	private JScrollPane courseScroll;
	private DefaultListModel defaultCoursesList;
	private JList coursesJList;

	// Initializing different ArrayLists that will store specific values, to be
	// summed in GPA calculations
	ArrayList<Double> weightedAvgList = new ArrayList<Double>(); // an ArrayList that contains the weighed average, each
																	// value is the GPA * the number of credit hours for
																	// that corresponding course
	ArrayList<Double> credHoursList = new ArrayList<Double>(); // an ArrayList that contains the credit hours for each
																// course with a GPA
	ArrayList<Double> totalCredHours = new ArrayList<Double>(); // an ArrayList that contains the credit hours for every
																// course entered
	ArrayList<Double> nonGPAHours = new ArrayList<Double>(); // an ArrayList that contains the credit hours for each
																// course that was not entered with a GPA, courses the
																// user anticipates taking

	public GpaFrame() {
		// setting the size of frame, makes it so user can't resize it
		setSize(900, 400);
		setResizable(false);
		// setting program to terminate when the GUI is closed
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// title of the frame
		setTitle("Course Planner and GPA Calculator");
		// sets the layout manager to be a FlowLayout, found that to work the best
		setLayout(new FlowLayout());
		// calls the method that creates an interface for the user to input course
		// information
		buildCourseInputPanel();
		add(courseInputPanel);
		// builds the window where the course information is displayed
		buildCourseDisplayPanel();
		add(courseDisplayPanel);
		// builds area for user to calculate current GPA and find a required GPA to meet
		// a target
		buildGpaPanel();
		add(gpaPanel);
		// centers frame on the center of the screen, and makes it visible
		setLocationRelativeTo(null);
		setVisible(true);

	}

	/**
	 * method that creates a panel that contains the components in which the user
	 * can input information and add a course
	 */
	private void buildCourseInputPanel() {

		// creates three JLabels that describe what to input in their corresponding
		// TextField
		hoursLabel = new JLabel("Enter number of Credit Hours");
		gpaLabel = new JLabel("Enter numerical GPA");
		nameLabel = new JLabel("Enter Course Name");

		// creates three TextFields in which the user enters information
		courseHoursInput = new JTextField(2);
		courseGPAInput = new JTextField(2);
		courseNameInput = new JTextField(10);

		// creates a JButton that adds a course to be displayed
		enterCourse = new JButton("Add Course");

		// associates the corresponding ActionListener to the add course button
		enterCourse.addActionListener(new CourseButtonListener());

		// creates an array containing the options for the drop down menu
		String[] courseTimes = { "Previously Taken", "Currently Taking", "Taking in the future" };

		// creates a ComboBox with the options set above
		courseBox = new JComboBox(courseTimes);

		// sets a layout manager for this panel
		setLayout(new FlowLayout());

		// creates a panel to display all this information, and then adds each component
		// to it
		courseInputPanel = new JPanel();
		courseInputPanel.add(hoursLabel);
		courseInputPanel.add(courseHoursInput);
		courseInputPanel.add(gpaLabel);
		courseInputPanel.add(courseGPAInput);
		courseInputPanel.add(nameLabel);
		courseInputPanel.add(courseNameInput);
		courseInputPanel.add(courseBox);
		courseInputPanel.add(enterCourse);

	}

	/**
	 * creates a panel that houses where the courses are displayed and the buttons
	 * needed to remove and add blank courses at the users discretion
	 */
	private void buildCourseDisplayPanel() {

		// creates a default list that courses are added to initially
		defaultCoursesList = new DefaultListModel();

		// creates a JList that can be added to display on the ScrollPane
		coursesJList = new JList(defaultCoursesList);

		// creates three buttons to remove courses, or add 15 blank credit hours
		removeOneCourse = new JButton("Remove Selected Course");
		clearCourse = new JButton("Remove All Courses");
		addHours = new JButton("Add 15 Blank Hours");

		// associates each button with its corresponding ActionListener
		removeOneCourse.addActionListener(new removeOneListener());
		clearCourse.addActionListener(new removeAllListener());
		addHours.addActionListener(new addHoursListener());

		// adds the JList containing the courses to the ScrollPane, where they can be
		// displayed
		courseScroll = new JScrollPane(coursesJList);

		// sets a layout manager for this panel
		setLayout(new FlowLayout());

		// creates the JPanel and then adds each necessary component
		courseDisplayPanel = new JPanel();
		courseDisplayPanel.add(courseScroll);
		courseDisplayPanel.add(removeOneCourse);
		courseDisplayPanel.add(clearCourse);
		courseDisplayPanel.add(addHours);
	}

	/**
	 * method that creates the panel that houses all the elements for calculating
	 * GPA, such as the button to calculate GPA and its display field, also has a
	 * target GPA button and a TextField to input the users target GPA
	 */
	private void buildGpaPanel() {

		// creates two buttons, one to calculate current GPA, and another to calculate a
		// required GPA based on the users input
		calcGPA = new JButton("Calculate Current GPA");
		targetGpa = new JButton("Set Target GPA");

		// TextField where the user can enter their desired GPA, only up to two decimal
		// places
		targetInput = new JTextField(3);

		// associates buttons with their corresponding ActionListener
		calcGPA.addActionListener(new gpaCalcListener());
		targetGpa.addActionListener(new gpaTargetListener());

		// TextField that displays the calculated GPA
		calcGPADisplay = new JTextField(3);
		
		// sets Layout Manager
		setLayout(new FlowLayout());

		// creates the panel and then adds the required components
		gpaPanel = new JPanel();
		gpaPanel.add(calcGPA);
		gpaPanel.add(calcGPADisplay);
		gpaPanel.add(targetGpa);
		gpaPanel.add(targetInput);
	}

	private class CourseButtonListener implements ActionListener {
		/**
		 * method that executes if the add course button is pressed
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == enterCourse) {
				String displayString = "Credit Hours - "; // creates an empty string, will eventually be what is
															// displayed

				// gets the string form of what the user inputed for the course fields
				String hourString = courseHoursInput.getText().trim();
				String gpaString = courseGPAInput.getText().trim();
				String nameString = courseNameInput.getText().trim();

				// as the number of credit hours is required, will always add the credit hours
				// to the display
				displayString += hourString;

				if (courseGPAInput.getText().equals("")) { // checks if the GPA input is empty or not
					displayString += "";
					nonGPAHours.add(Double.parseDouble(hourString)); // casts the hour string to a double, and then adds
																		// it to an array that has credit hours not
																		// attributed to a GPA
					totalCredHours.add(Double.parseDouble(hourString)); // casts the string to a double, and then adds
																		// it to an array that contains the total credit
																		// hours
				} else {
					displayString += ", GPA = " + gpaString; // adds the GPA to the display string if there was input

					// statement below checks to see if the class has been taken or is being taken
					// currently, which I assumed to mean that their would be a GPA attributed to that
					// course
					if (courseBox.getSelectedItem().toString().equals("Previously Taken")
							|| courseBox.getSelectedItem().toString().equals("Currently Taking")) {
						Double gpa = Double.parseDouble(gpaString); // converts the GPA input to a double
						Double currentCredHours = Double.parseDouble(hourString); // converts the course hour input to a
																					// double

						Double currentWeightedAverage = gpa * currentCredHours; // multiplies the course GPA by the
																				// number of credit hours, which is used
																				// in GPA calculation
						totalCredHours.add(currentCredHours);
						weightedAvgList.add(currentWeightedAverage);
						credHoursList.add(currentCredHours);
					}

				}
				if (courseNameInput.getText().equals("")) { // checks to see if a course name was inputed
					displayString += "";
				} else {
					displayString += ", " + nameString; // if their was a name entered, adds it to the displayString
				}
				defaultCoursesList.addElement(displayString); // adds the string to the default list, which allows it to
																// be shown in the ScrollPane
			}

		}
	}

	private class removeOneListener implements ActionListener {
		/**
		 * method that executes if the remove course button is pressed
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == removeOneCourse) {
				int value = coursesJList.getSelectedIndex(); // finds the index of the course that is selected
				((DefaultListModel) coursesJList.getModel()).remove(value); // removes the element at that index from
																			// the the JList
				// removes that element from all the ArrayLists, so the GPA calculation is
				// correct
				weightedAvgList.remove(value);
				credHoursList.remove(value);
				totalCredHours.remove(value);
				nonGPAHours.remove(value);
			}
		}
	}

	private class removeAllListener implements ActionListener {
		/**
		 * method that executes if the remove all button is pressed
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == clearCourse) {
				// clears all elements from the default list and ArrayLists
				((DefaultListModel) coursesJList.getModel()).removeAllElements();
				weightedAvgList.clear();
				credHoursList.clear();
				totalCredHours.clear();
				nonGPAHours.clear();
			}
		}
	}

	private class addHoursListener implements ActionListener {
		/**
		 * method that executes if the add blank courses button is pressed
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == addHours) {
				// casts the JList as a new add list
				DefaultListModel addList = (DefaultListModel) coursesJList.getModel();
				// adds an 15 credit hours as 5 new courses, each being 3 credit hours
				addList.addElement("Credit Hours - 3");
				addList.addElement("Credit Hours - 3");
				addList.addElement("Credit Hours - 3");
				addList.addElement("Credit Hours - 3");
				addList.addElement("Credit Hours - 3");
			}
		}
	}

	private class gpaCalcListener implements ActionListener {
		/**
		 * method that is executed if the calculate GPA button is pressed
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == calcGPA) {
				// initializing values used in calculations
				Double avgSumTot = 0.0;
				Double credHoursTot = 0.0;
				// loops through the weighted average list, adding each value to the total
				for (Double num : weightedAvgList) {
					avgSumTot += num;
				}
				// loops through an array that contains the credit hours for courses that have a
				// GPA
				for (Double num : credHoursList) {
					credHoursTot += num;
				}
				Double finalGpa = avgSumTot / credHoursTot; // calculates the GPA by dividing the weighted sum by the
															// amount of credit hours
				// rounds the GPA to two decimals, and then displays it in the TextField
				Double gpaRound = (double) round(finalGpa, 2);
				calcGPADisplay.setText(gpaRound.toString());
			}
		}
	}

	private class gpaTargetListener implements ActionListener {
		/**
		 * method that is executed when the user presses the target GPA button
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == targetGpa) {
				// initializes the values that will be used in the calculation
				Double credHoursTotal = 0.0;
				Double nonUsedHours = 0.0;
				Double avgSumTot = 0.0;
				Double gpaTarget = Double.parseDouble(targetInput.getText().toString()); // converts the users target
																							// GPA input to a double
				// adds the total credit hours together
				for (Double num : totalCredHours) {
					credHoursTotal += num;
				}
				// adds the credit hours for courses that did not have a GPA
				for (Double num : nonGPAHours) {
					nonUsedHours += num;
				}
				// adds the values from the weighted sum list
				for (Double num : weightedAvgList) {
					avgSumTot += num;
				}

				// equation to calculate the required average GPA of all future courses in order to
				// reach the user's goal, which is rounded
				Double finalTarget = ((gpaTarget * credHoursTotal) - avgSumTot) / nonUsedHours;
				Double finalTargetRound = (Double) round(finalTarget, 2);
				// displays a pop up message based on what the required GPA was
				if (finalTarget < 2.0) {
					JOptionPane.showMessageDialog(null,
							"You should probabbly take fewer credit hours to reach a GPA of " + gpaTarget);
				} else if (finalTarget > 4.0) {
					JOptionPane.showMessageDialog(null,
							"To reach a GPA of " + gpaTarget + " you should add more credit hours and recalculate");
				} else {
					JOptionPane.showMessageDialog(null, "You need a average GPA of " + finalTargetRound
							+ " in your future classes in order to get a GPA of " + gpaTarget);
				}
			}
		}
	}

	/**
	 * this method used for rounding was found on stackoverflow, all credit to the
	 * user Jonik who created it
	 * 
	 * @param value
	 * @param places
	 * @return
	 */
	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();
		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	/**
	 * main method that creates the frame
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new GpaFrame();
	}
}