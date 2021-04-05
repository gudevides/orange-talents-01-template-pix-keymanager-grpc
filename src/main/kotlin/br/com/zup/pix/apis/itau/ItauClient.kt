package br.com.zup.pix.apis.itau

import br.com.zup.pix.apis.itau.ConsultaContaItauResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client

@Client("http://localhost:9091")
interface ItauClient {

    @Get("/api/v1/clientes/{clienteId}/contas")
    fun consultaConta(@PathVariable clienteId: String, @QueryValue tipo: String) : HttpResponse<ConsultaContaItauResponse>
}