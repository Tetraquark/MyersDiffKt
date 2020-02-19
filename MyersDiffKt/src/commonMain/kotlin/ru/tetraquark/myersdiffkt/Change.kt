package ru.tetraquark.myersdiffkt

sealed class Change {
    class Insert(val from: Int, val count: Int) : Change()
    class Remove(val from: Int, val count: Int) : Change()
}
