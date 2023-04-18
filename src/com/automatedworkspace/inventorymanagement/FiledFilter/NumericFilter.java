package com.automatedworkspace.inventorymanagement.FiledFilter;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * The type Numeric filter.
 */
public final class NumericFilter extends PlainDocument {
	@Override
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
		if (str == null) {
			return;
		}

		char[] chars = str.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (char ch : chars) {
			if (Character.isDigit(ch)) {
				sb.append(ch);
			}
		}
		super.insertString(offs, sb.toString(), a);
	}
}