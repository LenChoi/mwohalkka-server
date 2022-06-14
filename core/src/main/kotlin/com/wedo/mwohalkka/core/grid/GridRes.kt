package com.wedo.mwohalkka.core.grid

open class GridRes<E> {

    var page = 0
    var total = 0
    var records = 0
    var rows: List<E> = mutableListOf()

    constructor()
}
