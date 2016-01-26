package com.mrdai.html2md.converts;

import com.mrdai.html2md.markdown.MDElement;
import org.jsoup.nodes.Element;

/**
 * A rule of converting HTML element to Markdown element
 */
public abstract class ConvertRule {

    public abstract boolean supports(Element element);

    public abstract MDElement convert(Element element);

}
