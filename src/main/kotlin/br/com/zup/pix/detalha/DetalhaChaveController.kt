package br.com.zup.pix.detalha

import br.com.zup.DetalharChaveReply
import br.com.zup.DetalharChaveRequest
import br.com.zup.KeymanagerDetalhaGrpcServiceGrpc
import br.com.zup.conf.excecoes.ErrorHandler
import br.com.zup.pix.PixRepository
import br.com.zup.pix.apis.bcb.BcbClient
import io.grpc.stub.StreamObserver
import io.micronaut.validation.validator.Validator
import javax.inject.Inject
import javax.inject.Singleton
import javax.validation.ConstraintViolationException


@ErrorHandler
@Singleton
class DetalhaChaveController(
    @Inject val pixRepository: PixRepository,
    @Inject val bcbClient: BcbClient,
    @Inject val validator: Validator
) : KeymanagerDetalhaGrpcServiceGrpc.KeymanagerDetalhaGrpcServiceImplBase() {

    override fun detalharChave(request: DetalharChaveRequest, responseObserver: StreamObserver<DetalharChaveReply>) {

        val filtro = request.toModel(validator)
        val detalhesChave = filtro.filtra(pixRepository, bcbClient)

        responseObserver.onNext(DetalharChaveReplyConverter().converter(detalhesChave))
        responseObserver.onCompleted()
    }

    fun DetalharChaveRequest.toModel(validator: Validator): FiltroDetalha {

        val filtro = when (filtroCase!!) {
            DetalharChaveRequest.FiltroCase.PIXID -> pixId.let { FiltroDetalha.PixId(it.clienteId, it.pixId) }
            DetalharChaveRequest.FiltroCase.CHAVE -> FiltroDetalha.Chave(chave)
            DetalharChaveRequest.FiltroCase.FILTRO_NOT_SET -> FiltroDetalha.Invalido
        }

        val violations = validator.validate(filtro)
        if (violations.isNotEmpty()) {
            throw ConstraintViolationException(violations)
        }

        return filtro
    }
}