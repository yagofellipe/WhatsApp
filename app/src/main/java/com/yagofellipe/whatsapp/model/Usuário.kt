package com.yagofellipe.whatsapp.model

data class Usuário(
    var id: String,
    var nome: String,
    var email: String,
    var foto: String = ""
)
