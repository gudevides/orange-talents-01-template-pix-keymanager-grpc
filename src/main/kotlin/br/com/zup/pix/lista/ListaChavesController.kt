package br.com.zup.pix.lista

import br.com.zup.*
import br.com.zup.conf.excecoes.ErrorHandler
import br.com.zup.pix.PixRepository
import com.google.protobuf.Timestamp
import io.grpc.stub.StreamObserver
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@ErrorHandler
@Singleton
class ListaChavesController(@Inject val pixRepository: PixRepository) :
    KeymanagerListaGrpcServiceGrpc.KeymanagerListaGrpcServiceImplBase() {

    override fun listarChaves(request: ListarChavesRequest, responseObserver: StreamObserver<ListarChavesReply>) {

        if (request.clienteId.isNullOrBlank()) throw IllegalArgumentException("Id do cliente não foi informado!")

        val chaves = pixRepository.findAllByClienteId(request.clienteId)
            .map {
                ListarChavesReply.ChavesPix.newBuilder()
                    .setPixId(it.id.toString())
                    .setTipo(ChaveEnum.valueOf(it.tipoChave.name))
                    .setChave(it.chave)
                    .setTipoConta(ContaEnum.valueOf(it.tipoConta.name))
                    .setCriadaEm(it.criadoEm.let {
                        val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                        Timestamp.newBuilder()
                            .setSeconds(createdAt.epochSecond)
                            .setNanos(createdAt.nano)
                            .build()
                    })
                    .build()
            }

        responseObserver.onNext(
            ListarChavesReply.newBuilder()
                .setClienteId(request.clienteId)
                .addAllChaves(chaves)
                .build()
        )
        responseObserver.onCompleted()
    }

}