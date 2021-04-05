package br.com.zup.pix

import br.com.zup.pix.apis.itau.ConsultaContaItauResponse
import javax.persistence.Embeddable

@Embeddable
class ContaPix(
    val tipo: String,
    val instituicao: String,
    val ispb: String,
    val agencia: String,
    val numero: String,
    val nome: String,
    val cpf: String,
)