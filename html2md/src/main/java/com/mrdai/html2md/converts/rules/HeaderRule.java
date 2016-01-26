package com.mrdai.html2md.converts.rules;

import com.mrdai.html2md.converts.ConvertRule;
import com.mrdai.html2md.markdown.elements.Header;
import com.mrdai.html2md.markdown.MDElement;
import org.jsoup.nodes.Element;

import java.util.regex.Pattern;

/**
 * Conversion rule for HTML headers
 */
public class HeaderRule extends ConvertRule {
    private static final Pattern HEADER_TAG_NAME = Pattern.compile("h[123456]");

    @Override
    public boolean supports(Element element) {
        return HEADER_TAG_NAME.matcher(element.tagName()).matches();
    }

    @Override
    public MDElement convert(Element element) {
        return new Header(Integer.valueOf(element.tagName().charAt(1)),
                         element.text());
    }
}
