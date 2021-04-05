package br.com.zup.pix.cadastra

import br.com.zup.conf.excecoes.ChaveExistenteException
import br.com.zup.pix.Pix
import br.com.zup.pix.PixRepository
import br.com.zup.pix.apis.bcb.BcbClient
import br.com.zup.pix.apis.bcb.CreatePixKeyRequest
import br.com.zup.pix.apis.itau.ItauClient
import io.micronaut.http.HttpStatus
import io.micronaut.validation.Validated
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Singleton
class CadastraChaveService(
    val itauClient: ItauClient,
    val bcbClient: BcbClient,
    val pixRepository: PixRepository
) {

    @Transactional
    fun cadastraChave(@Valid request: NovaChaveRequest) : Pix {

        //Verifica e a chave já existe
        if(pixRepository.existsByChave(request.valorChave)){
            throw ChaveExistenteException("Chave ${request.valorChave} já cadastrada!")
        }

        //Identificar o cliente no Itau
        val contaResponse = itauClient.consultaConta(request.cliente, request.tipoConta.toString())
        contaResponse.body() ?: throw IllegalStateException("Conta não encontrada no sistema do Itau!")

        //Salva no banco
        val novaChave = request.toModel(contaResponse.body()!!)
        pixRepository.save(novaChave)

        //Registra no BCB
        val cadastraBcb = CreatePixKeyRequest.converter(novaChave)
        val bcbResponse = bcbClient.cadastraChave(cadastraBcb)
        if (bcbResponse.status != HttpStatus.CREATED) throw java.lang.IllegalStateException("Erro ao registrar a chave no BCB!")

        //Atualiza chave na base no caso de chave Aleatória gerada pelo BCB
        novaChave.atualizaChave(bcbResponse.body()!!.key)

        return novaChave
    }
}
