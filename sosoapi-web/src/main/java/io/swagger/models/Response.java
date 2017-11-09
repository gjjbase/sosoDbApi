package io.swagger.models;

import io.swagger.models.properties.Property;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Response {
    private String description;
    private Property schema;
    private Map<String, Object> examples;
    private Map<String, Property> headers;
    /** 排序权重*/
    private int sortWeight;
    
    public Response schema(Property property) {
        this.setSchema(property);
        return this;
    }

    public Response description(String description) {
        this.setDescription(description);
        return this;
    }

    public Response example(String type, Object example) {
        if (examples == null) {
            examples = new HashMap<String, Object>();
        }
        examples.put(type, example);
        return this;
    }

    public Response header(String name, Property property) {
        addHeader(name, property);
        return this;
    }

    public Response headers(Map<String, Property> headers) {
        this.headers = headers;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Property getSchema() {
        return schema;
    }

    public void setSchema(Property schema) {
        this.schema = schema;
    }

    public Map<String, Object> getExamples() {
        return this.examples;
    }

    public void setExamples(Map<String, Object> examples) {
        this.examples = examples;
    }

    public Map<String, Property> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Property> headers) {
        this.headers = headers;
    }

    public void addHeader(String key, Property property) {
        if (this.headers == null) {
            this.headers = new LinkedHashMap<String, Property>();
        }
        this.headers.put(key, property);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((examples == null) ? 0 : examples.hashCode());
        result = prime * result + ((headers == null) ? 0 : headers.hashCode());
        result = prime * result + ((schema == null) ? 0 : schema.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Response other = (Response) obj;
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }
        if (examples == null) {
            if (other.examples != null) {
                return false;
            }
        } else if (!examples.equals(other.examples)) {
            return false;
        }
        if (headers == null) {
            if (other.headers != null) {
                return false;
            }
        } else if (!headers.equals(other.headers)) {
            return false;
        }
        if (schema == null) {
            if (other.schema != null) {
                return false;
            }
        } else if (!schema.equals(other.schema)) {
            return false;
        }
        return true;
    }

	public int getSortWeight() {
		return sortWeight;
	}

	public void setSortWeight(int sortWeight) {
		this.sortWeight = sortWeight;
	}
}