package br.com.zup.pix.apis.bcb

import br.com.zup.pix.Pix
import br.com.zup.pix.TipoChaveEnum
import br.com.zup.pix.TipoContaEnum

data class CreatePixKeyRequest(val keyType: KeyType,
                          val key: String,
                          val bankAccount: BankAccount,
                          val owner: OwnerAccount){
    companion object {
        fun converter(chave: Pix): CreatePixKeyRequest {
            return CreatePixKeyRequest(
                keyType = KeyType.converter(TipoChaveEnum.valueOf(chave.tipoChave.name)),
                key = chave.chave,
                bankAccount = BankAccount(
                    participant = "60701190",
                    branch = chave.conta.agencia,
                    accountNumber = chave.conta.numero,
                    accountType = AccountType.converter(TipoContaEnum.valueOf(chave.conta.tipo))
                ),
                owner = OwnerAccount(
                    type = OwnerType.NATURAL_PERSON,
                    name = chave.conta.nome,
                    taxIdNumber = chave.conta.cpf
                )
            )
        }
    }
}

data class OwnerAccount(
    val type: OwnerType,
    val name: String,
    val taxIdNumber: String
)

enum class OwnerType {
    NATURAL_PERSON, LEGAL_PERSON
}

data class BankAccount(val participant: String,
                       val branch: String,
                       val accountNumber: String,
                       val accountType: AccountType)

enum class AccountType {
    CACC, SVGS;

    companion object {
        fun converter(tipoConta: TipoContaEnum): AccountType {
            return when (tipoConta) {
                TipoContaEnum.CONTA_CORRENTE -> CACC
                TipoContaEnum.CONTA_POUPANCA -> SVGS
            }
        }
    }
}

enum class KeyType {
    CPF, CNPJ, PHONE, EMAIL, RANDOM;

    companion object {
        fun converter(tipoConta: TipoChaveEnum): KeyType {
            return when (tipoConta) {
                TipoChaveEnum.CPF -> CPF
                TipoChaveEnum.CELULAR -> PHONE
                TipoChaveEnum.EMAIL -> EMAIL
                TipoChaveEnum.ALEATORIO -> RANDOM
            }
        }
    }
}

