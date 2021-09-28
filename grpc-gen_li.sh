#!/usr/bin/env sh
export GRPC_GEN=/mnt/working/tools/protoc/bin
export GRPC_JAVA_GEN=$GRPC_GEN/protoc-gen-grpc-java-1.41.0-linux-x86_64.exe
export GRPC_KT_GEN=$GRPC_GEN/protoc-gen-grpc-kotlin.sh
export JAVA_OUTPUT_FILE=./src/main/java
export KT_OUTPUT_FILE=./src/main/kotlin
export DIR_OF_PROTO_FILE=./rpc/protos
export PROTO_FILE=$DIR_OF_PROTO_FILE/*.proto

protoc --plugin=protoc-gen-grpc-java=$GRPC_JAVA_GEN \
--grpc-java_out=$JAVA_OUTPUT_FILE --java_out=$JAVA_OUTPUT_FILE \
--proto_path=$DIR_OF_PROTO_FILE $PROTO_FILE
protoc --plugin=protoc-gen-grpckt=$GRPC_KT_GEN \
--grpckt_out=$KT_OUTPUT_FILE \
--kotlin_out=$KT_OUTPUT_FILE \
--proto_path=$DIR_OF_PROTO_FILE $PROTO_FILE