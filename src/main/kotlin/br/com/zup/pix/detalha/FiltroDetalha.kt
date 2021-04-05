package br.com.zup.pix.detalha

import br.com.zup.conf.excecoes.ChaveNaoEncontrataException
import br.com.zup.pix.PixRepository
import br.com.zup.pix.apis.bcb.BcbClient
import io.micronaut.core.annotation.Introspected
import io.micronaut.validation.Validated
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Validated
@Introspected
sealed class FiltroDetalha {

    abstract fun filtra(pixRepository: PixRepository, bcbClient: BcbClient): DetalhesChavePix

    @Introspected
    data class PixId(
        @field:NotBlank val clienteId: String,
        @field:NotBlank val pixId: String
    ) : FiltroDetalha() {

        override fun filtra(pixRepository: PixRepository, bcbClient: BcbClient): DetalhesChavePix {
            return pixRepository.findByIdAndClienteId(UUID.fromString(pixId), clienteId).map(DetalhesChavePix::convert)
                .orElseThrow { throw ChaveNaoEncontrataException("Chave Pix não encontrada no sistema!") }
        }
    }

    @Introspected
    data class Chave(@field:NotBlank @field:Size(max = 77) val chave: String) : FiltroDetalha() {

        override fun filtra(pixRepository: PixRepository, bcbClient: BcbClient): DetalhesChavePix {
            return pixRepository.findByChave(chave).map(DetalhesChavePix::convert)
                .orElseGet {
                    val bcbResponse = bcbClient.detalhaChave(chave)
                    bcbResponse.body() ?: throw ChaveNaoEncontrataException("Chave não encontrada!")

                    bcbResponse.body()!!.toModel()
                }
        }
    }

    @Introspected
    object Invalido : FiltroDetalha() {

        override fun filtra(pixRepository: PixRepository, bcbClient: BcbClient): DetalhesChavePix {
            throw IllegalArgumentException("Chave pix inválida ou não informada!")
        }
    }
}
