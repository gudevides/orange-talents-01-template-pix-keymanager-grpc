package br.com.zup.pix.apis.bcb

import br.com.zup.pix.ContaPix
import br.com.zup.pix.TipoChaveEnum
import br.com.zup.pix.TipoContaEnum
import br.com.zup.pix.detalha.DetalhesChavePix
import java.lang.IllegalArgumentException
import java.time.LocalDateTime

data class PixKeyDetailsResponse(
    val keyType: KeyType,
    val key: String,
    val bankAccount: BankAccount,
    val owner: OwnerAccount,
    val createdAt: LocalDateTime
) {

    fun toModel(): DetalhesChavePix {
        return DetalhesChavePix(
            tipoChave = when (keyType) {
                KeyType.CPF -> TipoChaveEnum.CPF
                KeyType.PHONE -> TipoChaveEnum.CELULAR
                KeyType.EMAIL -> TipoChaveEnum.EMAIL
                KeyType.RANDOM -> TipoChaveEnum.ALEATORIO
                KeyType.CNPJ -> throw IllegalArgumentException("CNPJ não implementado no sistema!")
            },
            chave = key,
            tipoConta = when (bankAccount.accountType) {
                AccountType.CACC -> TipoContaEnum.CONTA_CORRENTE
                AccountType.SVGS -> TipoContaEnum.CONTA_POUPANCA
            },
            conta = ContaPix(
                bankAccount.accountType.name,
                "Itau",
                bankAccount.participant,
                bankAccount.branch,
                bankAccount.accountNumber,
                owner.name,
                owner.taxIdNumber
            ),
            cadastradaEm = createdAt
        )
    }
}
