package br.com.zup.pix.cadastra

import br.com.zup.conf.validacao.ValidaChavePix
import br.com.zup.pix.ContaPix
import br.com.zup.pix.Pix
import br.com.zup.pix.TipoChaveEnum
import br.com.zup.pix.TipoContaEnum
import br.com.zup.pix.apis.itau.ConsultaContaItauResponse
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@ValidaChavePix
@Introspected
class NovaChaveRequest(
    @field:NotBlank
    val cliente: String,
    @field:NotNull
    val tipoChave: TipoChaveEnum,
    @field:Size(max = 77)
    val valorChave: String,
    @field:NotNull
    val tipoConta: TipoContaEnum
) {
    fun toModel(conta: ConsultaContaItauResponse): Pix {
        return Pix(
            clienteId = this.cliente,
            tipoChave = this.tipoChave,
            chave = if (this.tipoChave == TipoChaveEnum.ALEATORIO) UUID.randomUUID().toString() else this.valorChave,
            tipoConta = this.tipoConta,
            conta = ContaPix(
                conta.tipo,
                conta.instituicao.nome,
                conta.instituicao.ispb,
                conta.agencia,
                conta.numero,
                conta.titular.nome,
                conta.titular.cpf
            )
        )
    }
}

