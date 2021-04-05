package br.com.zup.pix

import org.hibernate.annotations.Type
import org.hibernate.type.UUIDCharType
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
class Pix(
    @field:NotBlank
    @Column(nullable = false)
    val clienteId: String,
    @field:NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val tipoChave: TipoChaveEnum,
    @field:NotBlank
    @Column(nullable = false)
    var chave: String,
    @field:NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val tipoConta: TipoContaEnum,
    @Embedded val conta: ContaPix
) {
    fun atualizaChave(key: String) {
        this.chave = key
    }

    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    val id: UUID? = null

    val criadoEm: LocalDateTime = LocalDateTime.now()
}