package br.com.zup.pix.detalha

import br.com.zup.pix.ContaPix
import br.com.zup.pix.Pix
import br.com.zup.pix.TipoChaveEnum
import br.com.zup.pix.TipoContaEnum
import java.time.LocalDateTime
import java.util.*

data class DetalhesChavePix(
    val pixId: UUID? = null,
    val clienteId: String? = null,
    val tipoChave: TipoChaveEnum,
    val chave: String,
    val tipoConta: TipoContaEnum,
    val conta: ContaPix,
    val cadastradaEm: LocalDateTime = LocalDateTime.now()) {

    companion object {
        fun convert(chave: Pix) : DetalhesChavePix {
            return DetalhesChavePix(
                pixId = chave.id,
                clienteId = chave.clienteId,
                tipoChave = chave.tipoChave,
                chave = chave.chave,
                tipoConta = chave.tipoConta,
                conta = chave.conta,
                cadastradaEm = chave.criadoEm
            )
        }
    }
}
