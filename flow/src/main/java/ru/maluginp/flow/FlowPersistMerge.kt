package ru.maluginp.flow

interface FlowPersistMerge<in M1, in M2, out M3> {
    fun merge(m1: M1, m2: M2): M3
}