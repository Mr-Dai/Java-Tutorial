package com.mrdai.html2md.converts;

import com.mrdai.html2md.markdown.MDDocument;
import org.jsoup.nodes.Document;

import java.util.LinkedList;
import java.util.List;

public class Converter {
    private boolean isDebug = false;
    private List<ConvertRule> rules = new LinkedList<>();

    public Converter() {
        this(false);
    }

    public Converter(boolean isDebug) {
        this.isDebug = isDebug;
        loadConvertRules();
    }

    public MDDocument convert(Document htmlDocument) {

        return null;
    }

    private void loadConvertRules() {

    }

}
