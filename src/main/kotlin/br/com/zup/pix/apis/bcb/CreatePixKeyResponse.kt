package br.com.zup.pix.apis.bcb

import java.time.LocalDateTime

class CreatePixKeyResponse(val keyType: KeyType,
                           val key: String,
                           val bankAccount: BankAccount,
                           val owner: OwnerAccount,
                           val createdAt: LocalDateTime)
