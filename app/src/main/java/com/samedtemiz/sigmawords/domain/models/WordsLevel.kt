package com.samedtemiz.sigmawords.domain.models

sealed class WordsLevel(val name: String){
    class A1 : WordsLevel("A1")
    class A2 : WordsLevel("A2")
    class B1 : WordsLevel("B1")
    class B2 : WordsLevel("B2")
    class C1 : WordsLevel("C1")
}

