# Elasticsearch Vector Binary Encoding Ingest Processor

Built on Elasticsearch 6.1.2

## Usage


```
PUT _ingest/pipeline/binary-vector-pipeline
{
  "description": "A pipeline to encode vectors as binary.",
  "processors": [
    {
      "vector_binary_encode": {
        "targets": [
          {
            "source": "vector",
            "target": "vector_binary"
          }
        ]
      }
    }
  ]
}
PUT /index/doc/1?pipeline=binary-vector-pipeline
{
  "vector" : [1.1,2.2,3.4]
}
GET /index/doc/1

then, the doc has 2 fields like this.
{
    "vector": [
      1.1,
      2.2,
      3.4
    ],
    "vector_binary": "P/GZmZmZmZpAAZmZmZmZmkALMzMzMzMz"
  }
```


## Setup

In order to install this plugin, you need to create a zip distribution first by running

```bash
gradle clean assemble
```

This will produce a zip file in `build/distributions`.

After building the zip file, you can install it like this

```bash
elasticsearch-plugin install file:///path/to/iplugin/build/distribution/ingest-csv-0.0.1-SNAPSHOT.zip
```

