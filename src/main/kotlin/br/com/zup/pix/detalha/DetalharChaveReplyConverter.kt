package br.com.zup.pix.detalha

import br.com.zup.ChaveEnum
import br.com.zup.ContaEnum
import br.com.zup.DetalharChaveReply
import com.google.protobuf.Timestamp
import java.time.ZoneId

class DetalharChaveReplyConverter {

        fun converter(detalhesChave: DetalhesChavePix): DetalharChaveReply {
            return DetalharChaveReply.newBuilder()
                .setClienteId(detalhesChave.clienteId?.toString() ?: "")
                .setPixId(detalhesChave.pixId?.toString() ?: "")
                .setChave(
                    DetalharChaveReply.ChavePix
                        .newBuilder()
                        .setTipo(ChaveEnum.valueOf(detalhesChave.tipoChave.name))
                        .setChave(detalhesChave.chave)
                        .setConta(
                            DetalharChaveReply.ChavePix.ContaInfo.newBuilder()
                                .setTipo(ContaEnum.valueOf(detalhesChave.tipoConta.name))
                                .setInstituicao(detalhesChave.conta.instituicao)
                                .setNomeTitular(detalhesChave.conta.nome)
                                .setCpfTitular(detalhesChave.conta.cpf)
                                .setAgencia(detalhesChave.conta.agencia)
                                .setNumeroConta(detalhesChave.conta.numero)
                                .build()
                        )
                        .setCriadaEm(detalhesChave.cadastradaEm.let {
                            val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                            Timestamp.newBuilder()
                                .setSeconds(createdAt.epochSecond)
                                .setNanos(createdAt.nano)
                                .build()
                        })
                )
                .build()
        }

}