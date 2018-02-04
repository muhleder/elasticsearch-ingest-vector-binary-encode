/*
 * Copyright [2017] [Jun Ohtani]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.gosololaw.elasticsearch;

import org.elasticsearch.ingest.AbstractProcessor;
import org.elasticsearch.ingest.IngestDocument;
import org.elasticsearch.ingest.Processor;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.ingest.ConfigurationUtils.readStringProperty;

public class BinaryEncodeProcessor extends AbstractProcessor {

    public static final String TYPE = "vector_binary_encode";

    private final String source;
    private final String target;

    private BinaryEncodeProcessor(String tag, String source, String target) throws IOException {
        super(tag);
        this.source = source;
        this.target = target;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void execute(IngestDocument ingestDocument) throws Exception {
        List<Double> vector_list = ingestDocument.getFieldValue(source, List.class, true);
        if (vector_list != null) {
            double[] vector = new double[vector_list.size()];
            for (int i = 0; i < vector_list.size(); i++) {
                vector[i] = vector_list.get(i);
            }
            String value = encodeVector(vector);
            ingestDocument.setFieldValue(target, value);
        }
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public static final class Factory implements Processor.Factory {
        @Override
        public BinaryEncodeProcessor create(Map<String, Processor.Factory> factories, String tag, Map<String, Object> config)
                throws Exception {
            String source = readStringProperty(TYPE, tag, config, "source");
            String target = readStringProperty(TYPE, tag, config, "target");
            return new BinaryEncodeProcessor(tag, source, target);
        }
    }

    private static String encodeVector(double[] doubleArray) {
        final byte[] myByteArray = doubleToByteArray(doubleArray);
        return Base64.getEncoder().encodeToString(myByteArray);
    }
    private static byte[] doubleToByteArray(double[] doubleArray) {
        ByteBuffer buf = ByteBuffer.allocate(Double.SIZE / Byte.SIZE * doubleArray.length);
        buf.asDoubleBuffer().put(doubleArray);
        return buf.array();
    }

}
