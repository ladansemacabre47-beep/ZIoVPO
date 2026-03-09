package com.example.lab.service.license;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Component
public class JsonCanonicalizationService {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    public byte[] canonicalize(Object payload) {
        try {
            JsonNode node = objectMapper.valueToTree(payload);
            String canonical = serializeNode(node);
            return canonical.getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Canonicalization failed", e);
        }
    }

    private String serializeNode(JsonNode node) {
        if (node.isObject()) {
            return serializeObject((ObjectNode) node);
        } else if (node.isArray()) {
            return serializeArray((ArrayNode) node);
        } else if (node.isTextual()) {
            return serializeString(node.textValue());
        } else if (node.isNumber()) {
            return node.numberValue().toString();
        } else if (node.isBoolean()) {
            return node.booleanValue() ? "true" : "false";
        } else if (node.isNull()) {
            return "null";
        }
        throw new RuntimeException("Unsupported node type: " + node.getNodeType());
    }

    private String serializeObject(ObjectNode node) {
        // RFC 8785: сортировка по UTF-16 code units
        TreeMap<String, JsonNode> sorted = new TreeMap<>((a, b) -> {
            int len = Math.min(a.length(), b.length());
            for (int i = 0; i < len; i++) {
                int diff = (int) a.charAt(i) - (int) b.charAt(i);
                if (diff != 0) return diff;
            }
            return a.length() - b.length();
        });

        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            sorted.put(entry.getKey(), entry.getValue());
        }

        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, JsonNode> entry : sorted.entrySet()) {
            if (!first) sb.append(",");
            sb.append(serializeString(entry.getKey()));
            sb.append(":");
            sb.append(serializeNode(entry.getValue()));
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }

    private String serializeArray(ArrayNode node) {
        List<String> items = new ArrayList<>();
        for (JsonNode item : node) {
            items.add(serializeNode(item));
        }
        return "[" + String.join(",", items) + "]";
    }

    private String serializeString(String value) {
        StringBuilder sb = new StringBuilder("\"");
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
                case '"'  -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\b' -> sb.append("\\b");
                case '\f' -> sb.append("\\f");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> {
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
                }
            }
        }
        sb.append("\"");
        return sb.toString();
    }
}