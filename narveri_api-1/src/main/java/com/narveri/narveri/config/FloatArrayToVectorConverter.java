package com.narveri.narveri.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.postgresql.util.PGobject;


@Converter(autoApply = false)
public class FloatArrayToVectorConverter
        implements AttributeConverter<float[], PGobject> {

    @Override
    public PGobject convertToDatabaseColumn(float[] attribute) {
        if (attribute == null) return null;

        try {
            PGobject pgObject = new PGobject();
            pgObject.setType("vector");

            StringBuilder sb = new StringBuilder();
            sb.append("[");

            for (int i = 0; i < attribute.length; i++) {
                sb.append(attribute[i]);
                if (i < attribute.length - 1) sb.append(",");
            }

            sb.append("]");
            pgObject.setValue(sb.toString());

            return pgObject;
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert float[] to vector", e);
        }
    }

    @Override
    public float[] convertToEntityAttribute(PGobject dbData) {
        if (dbData == null) return null;

        String value = dbData.getValue(); // "[0.1,0.2,...]"
        value = value.replace("[", "").replace("]", "");

        String[] parts = value.split(",");
        float[] array = new float[parts.length];

        for (int i = 0; i < parts.length; i++) {
            array[i] = Float.parseFloat(parts[i]);
        }

        return array;
    }
}
