package br.com.zup.pix

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface PixRepository : JpaRepository<Pix, UUID> {

    fun existsByChave(valorChave: String): Boolean
    fun findByIdAndClienteId(pixId: UUID, clienteId: String) : Optional<Pix>
    fun findByChave(chave: String) : Optional<Pix>
    fun findAllByClienteId(clienteId: String) : List<Pix>
}