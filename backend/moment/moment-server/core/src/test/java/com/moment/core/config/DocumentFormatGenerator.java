package com.moment.core.config;

import org.springframework.restdocs.snippet.Attributes;

import static org.springframework.restdocs.snippet.Attributes.key;

public interface DocumentFormatGenerator {

    static Attributes.Attribute getDateFormat() { // (2)
        return key("format").value("yyyy-MM-dd");
    }

    static Attributes.Attribute getDateTimeFormat() { // (2)
        return key("format").value("yyyy-MM-dd'T'HH:mm:ss");
    }

    static Attributes.Attribute getBooleanFormat() { // (3)
        return key("format").value("true or false");
    }

    static Attributes.Attribute getReceiptFormat() {
        return key("format").value("A for AirplaneTicket or B for BusTicket");
    }
    static Attributes.Attribute getStringNumFormat(int length) {
        return key("format").value("String of max length " + length);
    }
}
