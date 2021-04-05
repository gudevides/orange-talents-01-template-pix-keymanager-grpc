package br.com.zup.pix.cadastra

import br.com.zup.CadastrarChaveReply
import br.com.zup.CadastrarChaveRequest
import br.com.zup.KeymanagerCadastraGrpcServiceGrpc
import br.com.zup.conf.excecoes.ErrorHandler
import br.com.zup.pix.TipoChaveEnum
import br.com.zup.pix.TipoContaEnum
import io.grpc.stub.StreamObserver
import javax.inject.Singleton

@ErrorHandler
@Singleton
class CadastraChaveController(private val cadastraChaveService: CadastraChaveService) :
    KeymanagerCadastraGrpcServiceGrpc.KeymanagerCadastraGrpcServiceImplBase() {

    override fun cadastrarChave(
        request: CadastrarChaveRequest,
        responseObserver: StreamObserver<CadastrarChaveReply>
    ) {

        val chaveValidada = request.toModel()

        val chave = cadastraChaveService.cadastraChave(chaveValidada)
        responseObserver.onNext(CadastrarChaveReply.newBuilder().setPixId(chave.id.toString()).build())
        responseObserver.onCompleted()
    }

    fun CadastrarChaveRequest.toModel(): NovaChaveRequest {
        return NovaChaveRequest(
            cliente,
            TipoChaveEnum.valueOf(tipoChave.name),
            valorChave,
            TipoContaEnum.valueOf(tipoConta.name)
        )
    }
}