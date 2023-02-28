package br.senai.sp.jandira.bmicalculator.model

import java.time.LocalDate

data class Client(
    var id: Int,
    var name: String,
    var birthDay: LocalDate = LocalDate.now()
) {

}