package br.com.zup.pix.remove

import br.com.zup.conf.excecoes.ChaveNaoEncontrataException
import br.com.zup.pix.PixRepository
import br.com.zup.pix.apis.bcb.BcbClient
import br.com.zup.pix.apis.bcb.DeletePixKeyRequest
import io.micronaut.http.HttpStatus
import io.micronaut.validation.Validated
import java.lang.IllegalStateException
import java.util.*
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.constraints.NotBlank

@Validated
@Singleton
class RemoveChaveService(
    val pixRepository: PixRepository,
    val bcbClient: BcbClient
) {

    @Transactional
    fun removeChave(@NotBlank clienteId: String, @NotBlank pixId: String) {

        //Pesquisa na base
        pixRepository.findByIdAndClienteId(UUID.fromString(pixId), clienteId)
            .map {

                //Deleta da base
                pixRepository.delete(it)

                val bcbRequest = DeletePixKeyRequest(it.chave)

                //Deleta do BCB
                val deletaChaveResponse = bcbClient.deletaChave(
                    key = it.chave,
                    request = bcbRequest
                )

                //Valida se foi deletado
                if ( deletaChaveResponse.status != HttpStatus.OK) throw IllegalStateException("Erro ao deletar a chave do BCB!")

            }
            .orElseThrow {
                throw ChaveNaoEncontrataException("Chave pix $pixId não encontrada para este cliente!")
            }


    }
}
