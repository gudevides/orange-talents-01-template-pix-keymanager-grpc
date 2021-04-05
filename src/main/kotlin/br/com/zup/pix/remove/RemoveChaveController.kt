package br.com.zup.pix.remove

import br.com.zup.KeymanagerRemoveGrpcServiceGrpc
import br.com.zup.RemoverChaveReply
import br.com.zup.RemoverChaveRequest
import br.com.zup.conf.excecoes.ErrorHandler
import io.grpc.stub.StreamObserver
import javax.inject.Singleton

@ErrorHandler
@Singleton
class RemoveChaveController(val removeChaveService: RemoveChaveService) :
    KeymanagerRemoveGrpcServiceGrpc.KeymanagerRemoveGrpcServiceImplBase() {

    override fun removerChave(request: RemoverChaveRequest, responseObserver: StreamObserver<RemoverChaveReply>) {

        removeChaveService.removeChave(request.clienteId, request.pixId)

        responseObserver.onNext(RemoverChaveReply.newBuilder()
            .setPixId(request.pixId)
            .build())
        responseObserver.onCompleted()
    }
}