package com.tableforge;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
//restricts number input parts
public class FormatText {
	public static void restrictToNumeric(TextField textField) {
		TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
			if (change.getControlNewText().matches("\\d*")) {
				return change;
			}
			return null;
		});
		textField.setTextFormatter(textFormatter);
	}
		
	public static void restrictToDecimal(TextField textField) {
		TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
			if (change.getControlNewText().matches("\\d*(\\.\\d*)?")) {
				return change;
			}
			return null;
		});
		textField.setTextFormatter(textFormatter);
	}
}