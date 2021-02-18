package com.example.linuxgames;

import org.jsoup.nodes.Document;

public class steamPageDoc {
    private Document document;

    private static steamPageDoc instance = new steamPageDoc();

    steamPageDoc() {}

    public static steamPageDoc getInstance() {
        return instance;
    }

    public void setDocument(Document doc) {
        this.document = doc;
    }

    public Document getDocument() {
        return document;
    }
}
