package org.harryng.kotlin.demo.rpc;

import io.grpc.stub.StreamObserver;
import org.harryng.kotlin.demo.rpc.gen.ChatMessage;
import org.harryng.kotlin.demo.rpc.gen.ChatServiceGrpc;
import org.harryng.kotlin.demo.rpc.gen.ChatSignal;

public class ChatServiceImpl extends ChatServiceGrpc.ChatServiceImplBase {
    @Override
    public StreamObserver<ChatMessage> sendChatStream(
            StreamObserver<ChatSignal> responseObserver) {
        return new StreamObserver<ChatMessage>() {
            @Override
            public void onNext(ChatMessage value) {
//                responseObserver.onNext();
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {

            }
        };
    }
}
