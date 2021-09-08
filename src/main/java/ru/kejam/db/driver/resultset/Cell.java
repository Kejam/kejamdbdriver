package ru.kejam.db.driver.resultset;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Cell implements Cloneable {
    private String name;
    private Class className;
    private Object value;

    @Override
    protected Cell clone() throws CloneNotSupportedException {
        Cell clone = (Cell) super.clone();
        clone.setValue(value);
        clone.setClassName(className);
        clone.setName(name);
        return clone;
    }
}
