package br.com.zup.pix.apis.bcb

data class DeletePixKeyRequest(
    val key: String,
    val participant: String = "60701190"
)
