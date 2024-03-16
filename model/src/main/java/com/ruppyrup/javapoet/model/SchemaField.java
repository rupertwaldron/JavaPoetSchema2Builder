package com.ruppyrup.javapoet.model;


import java.util.Objects;

public record SchemaField<T> (String name, Class<T> clazz, T initialValue) {

    @Override
    public String toString() {
        return "SchemaField{" +
                "name='" + name + '\'' +
                ", clazz=" + clazz +
                ", initialValue=" + initialValue +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SchemaField<?> that)) return false;
        return Objects.equals(name, that.name) && Objects.equals(clazz, that.clazz) && Objects.equals(initialValue, that.initialValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, clazz, initialValue);
    }
}
