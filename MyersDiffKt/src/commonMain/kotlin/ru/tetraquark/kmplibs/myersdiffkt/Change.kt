package ru.tetraquark.kmplibs.myersdiffkt

sealed class Change {
    class Insert(val toOldListIndex: Int, val fromNewListIndex: Int, val count: Int) : Change()
    class Remove(val fromOldListIndex: Int, val count: Int) : Change()
}
