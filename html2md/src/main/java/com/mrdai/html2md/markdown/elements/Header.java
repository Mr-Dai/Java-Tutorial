package com.mrdai.html2md.markdown.elements;

import com.mrdai.html2md.markdown.MDElement;

/**
 * Markdown Headers
 */
public class Header extends MDElement {
    private int n;
    private String text;

    public Header(int n, String text) {
        if (n < 1 || n > 6)
            throw new IllegalArgumentException("Argument `n` can only be integer within 1 and 6");
        this.n = n;
        this.text = text;
    }

    @Override
    public String toString() {
        switch (n) {
            case 1: return "# " + text;
            case 2: return "## " + text;
            case 3: return "### " + text;
            case 4: return "#### " + text;
            case 5: return "##### " + text;
            case 6: return "###### " + text;
            default:
                return "# " + text;
        }
    }
}
