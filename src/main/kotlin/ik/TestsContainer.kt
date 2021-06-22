@file:Suppress("unused", "UnnecessaryVariable", "DuplicatedCode")

package ik

class TestsContainer {
    //@formatter:off

    fun sumBaseLine(list: List<Int>): Long {
        var ret = 0L
        for (element in list) {
            ret += element
        }
        return ret
    }

    fun sumOfSquaresBaseLine(list: List<Int>): Long {
        var ret = 0L
        for (i in list.indices) {
            ret += list[i] * list[i]
        }
        return ret
    }

    fun sumOfSquaresEvenBaseLine(list: List<Int>): Long {
        var ret = 0L
        for (i in list.indices) {
            if (list[i] % 2 == 0) ret += list[i] * list[i]
        }
        return ret
    }

    fun cartBaseLine(list: List<Int>, list2: List<Int>): Long {
        var ret = 0L
        for (element in list) {
            for (element2 in list2) {
                ret += element * element2
            }
        }
        return ret
    }

    fun mapsMegamorphicBaseLine(list: List<Int>): Long {
        var ret = 0L
        for (element in list) {
            ret += element * 1 * 2 * 3 * 4 * 5 * 6 * 7
        }
        return ret
    }

    fun filtersMegamorphicBaseLine(list: List<Int>): Long {
        var ret = 0L
        for (i in list.indices) {
            if (list[i] > 1 && list[i] > 2 && list[i] > 3 && list[i] > 4 && list[i] > 5 && list[i] > 6 && list[i] > 7) {
                ret += list[i]
            }
        }
        return ret
    }

    fun flatMapTakeBaseLine(vHi: List<Int>, vLo: List<Int>): Long {
        var ret = 0L
        var n = 0
        var flag = true
        var d = 0
        while (d < vHi.size && flag) {
            var dp = 0
            while (dp < vLo.size && flag) {
                ret += vHi[d] * vLo[dp]
                dp++
                n++
                if (n == 20000000) flag = false
            }
            d++
        }
        return ret
    }

    fun dotProductBaseLine(vHi: List<Int>): Long {
        var ret = 0L
        for (counter in vHi.indices) {
            ret += vHi[counter] * vHi[counter]
        }
        return ret
    }

    fun flatMapAfterZipBaseLine(vFaZ: List<Int>): Long {
        var ret = 0L
        for (counter1 in vFaZ.indices) {
            val item1 = (vFaZ[counter1] + vFaZ[counter1]).toLong()
            for (element in vFaZ) {
                val item2 = element.toLong()
                ret += item2 + item1
            }
        }
        return ret
    }

    fun zipAfterFlatMapBaseLine(vZaF: List<Int>): Long {
        var ret = 0L
        var index1 = 0
        var index2 = 0
        var flag1: Boolean = index1 <= vZaF.size - 1
        while (flag1 && index2 <= vZaF.size - 1) {
            val el2 = vZaF[index2].toLong()
            index2 += 1
            var index_zip = 0
            while (flag1 && index_zip <= vZaF.size - 1) {
                val el1 = vZaF[index_zip].toLong()
                index_zip += 1
                val elz = vZaF[index1].toLong()
                index1 += 1
                flag1 = index1 <= vZaF.size - 1
                ret = ret + elz + el1 + el2
            }
        }
        return ret
    }

    fun zipFlatMapFlatMapBaseLine(v: List<Int>, vLo: List<Int>): Long {
        val arr1: List<Int> = v
        val arr2: List<Int> = vLo
        var index11 = 0
        var index12: Int
        var index21 = 0
        var index22 = 0
        var ret = 0L
        var taken = 0
        val toTake = 20000000
        val size1 = arr1.size
        val size2 = arr2.size
        var goon = true
        while (index11 < size1 && taken < toTake && goon) {
            index12 = 0
            while (index12 < size2 && taken < toTake && goon) {
                val el1 = arr1[index11] * arr2[index12]
                if (index22 > size1) {
                    index21++
                    index22 = 0
                }
                if (index21 >= size2) {
                    goon = false
                } else {
                    if (index22 < size1) {
                        ret = ret + el1 + arr2[index21] - arr1[index22]
                        taken++
                        index22++
                    }
                }
                index12++
            }
            index11++
        }
        return ret
    }

    fun sum(list: List<Int>): Int {
        val sum = list
            .transduce(0) {
            { a, b -> a + b } }

        return sum
    }

    fun sumOfSquares(list: List<Int>): Long {
        val sumOfSquares = list
            .transduce(0L) {((
            +mapping<Int, Int> { it * it })
            { a, b -> a + b })}

        return sumOfSquares
    }

    fun sumOfSquaresEven(list: List<Int>): Int {
        val sumOfSquaresEven = list
            .transduce(0) {((
            +filtering<Int> { it % 2 == 0 }
            +mapping { it * it })
            { a, b -> a + b })}

        return sumOfSquaresEven
    }

    fun cart(vHi: List<Int>, vLo: List<Int>): Int {
        val cart = vHi
            .transduce(0){((
            +flatMapping<Int, Int> { d: Int -> vLo.transduce { mapping { it * d } } })
            { a, b -> a + b })}

        return cart
    }

    fun cartFused(vHi: List<Int>, vLo: List<Int>): Int {
        val cartFused = vHi
            .transduce(0){((
            +flatMappingFused { d: Int -> vLo.fuser { mapping { it * d } } })
            { a, b -> a + b })}

        return cartFused
    }

    fun mapsMegamorphic(list: List<Int>): Long {
        val mapsMegamorphic = list
            .transduce(0L) {((
            +mapping<Int, Int> { it * 1 }
            +mapping { it * 2 }
            +mapping { it * 3 }
            +mapping { it * 4 }
            +mapping { it * 5 }
            +mapping { it * 6 }
            +mapping { it * 7 })
            { a, b -> a + b })}

        return mapsMegamorphic
    }

    fun filtersMegamorphic(list: List<Int>): Int {
        val filtersMegamorphic = list
            .transduce(0) {((
            +filtering<Int> { it > 1 }
            +filtering { it > 2 }
            +filtering { it > 3 }
            +filtering { it > 4 }
            +filtering { it > 5 }
            +filtering { it > 6 }
            +filtering { it > 7 })
            { a, b -> a + b })}

        return filtersMegamorphic
    }

    fun flatMapTake(vHi: List<Int>, vLo: List<Int>): Int {
        val flatMapTake = vHi
            .transduce(0) {((
            +flatMapping<Int, Int> { d: Int -> vLo.transduce { mapping { it * d } } }
            +taking(20000000))
            { a, b -> a + b } )}

        return flatMapTake
    }

    fun flatMapTakeFused(vHi: List<Int>, vLo: List<Int>): Int {
        val flatMapTakeFused = vHi
            .transduce(0) {((
            +flatMappingFused { d: Int -> vLo.fuser { mapping { it * d } } }
            +taking(20000000))
            { a, b -> a + b } )}

        return flatMapTakeFused
    }

    fun dotProduct(list: List<Int>): Int {
        val dotProduct = list
            .transduce(0) {((
            +zipping(list) { a: Int, b -> a * b })
            { a, b -> a + b } )}

        return dotProduct
    }

    fun flatMapAfterZip(vFaZ: List<Int>): Long {
        val flatMapAfterZip = vFaZ
            .transduce(0L) {((
            +zipping(vFaZ) { a: Int, b -> a + b }
            +flatMapping<Int, Int> { d: Int -> vFaZ.transduce { mapping { it + d } } })
            { a, b -> a + b } )}

        return flatMapAfterZip
    }

    fun flatMapAfterZipFused(vFaZ: List<Int>): Long {
        val flatMapAfterZipFused = vFaZ
            .transduce(0L) {((
            +zipping(vFaZ) { a: Int, b -> a + b }
            +flatMappingFused { d: Int -> vFaZ.fuser { mapping { it + d } } })
            { a, b -> a + b } )}

        return flatMapAfterZipFused
    }

    fun zipAfterFlatMap(vZaF: List<Int>): Long {
        val zipAfterFlatMap = vZaF
            .transduce(0L) {((
            +flatMapping<Int, Int> { d -> vZaF.transduce { +mapping<Int, Int> { it + d } } }
            +zipping(vZaF) { a: Int, b -> a + b })
            { a, b -> a + b })}

        return zipAfterFlatMap
    }

    fun zipAfterFlatMapFused(vZaF: List<Int>): Long {
        val zipAfterFlatMapFused = vZaF
            .transduce(0L) {((
            +flatMappingFused { d: Int -> vZaF.fuser { +mapping<Int, Int> { it + d } } }
            +zipping(vZaF) { a: Int, b -> a + b })
            { a, b -> a + b })}

        return zipAfterFlatMapFused
    }

    fun zipFlatMapFlatMap(v: List<Int>, vLo: List<Int>): Int {
        val zipFlatMapFlatMap = vLo
            .transduce(0) {((
            +flatMapping<Int, Int> { d -> v.transduce { +mapping<Int, Int> { d - it } } }
            +zipping(v.lazyTransduce { +flatMapping<Int, Int> { d -> vLo.transduce { +mapping<Int, Int> { it * d } } } }) {
                    a: Int, b: Int -> a + b
            }
            +taking(20000000))
            { a, b -> a + b })}

        return zipFlatMapFlatMap
    }

    fun zipFlatMapFlatMapFused(v: List<Int>, vLo: List<Int>): Int {
        val zipFlatMapFlatMapFused = vLo
            .transduce(0) {((
            +flatMappingFused { d: Int -> v.fuser { +mapping<Int, Int> { d - it } } }
            +zipping(v.lazyTransduce { +flatMappingFused { d: Int -> vLo.fuser { +mapping<Int, Int> { it * d } } } }) {
                    a: Int, b: Int -> a + b
            }
            +taking(20000000))
            { a, b -> a + b })}

        return zipFlatMapFlatMapFused
    }

    fun zipFlatMapFlatMapFusedFused(v: List<Int>, vLo: List<Int>): Int {
        val zipFlatMapFlatMapFused = vLo
            .transduceNI(0) {((
            +flatMappingFused { d: Int -> v.fuser { +mapping<Int, Int> { d - it } } }
            +zipping(v.fuser2 { +flatMappingFused { d: Int -> vLo.fuser { +mapping<Int, Int> { it * d } } } }) {
                    a: Int, b: Int -> a + b
            }
            +taking(20000000))
            { a, b -> a + b })}

        return zipFlatMapFlatMapFused
    }

    fun zipFlatMapFlatMapFusedFusedNew(v: List<Int>, vLo: List<Int>): Int {
        val zipFlatMapFlatMapFused = vLo
            .transduce(0) {((
            +flatMappingFused { d: Int -> v.fuser { mapping { d - it } } }
            +zipping(v.fuser { flatMappingFused { d: Int -> vLo.fuser { mapping { it * d } } } }) {
                    a: Int, b: Int -> a + b
            }
            +taking(20000000))
            { a, b -> a + b })}

        return zipFlatMapFlatMapFused
    }

    fun zipFlatMapFlatMapTransduced(v: List<Int>, vLo: List<Int>): Int {
        val zipFlatMapFlatMapFused = vLo
            .transduceNI(0) {((
            +flatMapping({ v }) { d: Int -> { +mapping<Int, Int> { d - it } } }
            +zipping(v, { flatMapping({ vLo }) { d: Int ->  { mapping { it * d } } } }) {
                    a: Int, b: Int -> a + b
            }
            +taking(20000000))
            { a, b -> a + b })}

        return zipFlatMapFlatMapFused
    }

    fun singleContext(list: List<Int>): Int {
        val v = 42
        val result = list
            .transduce(0) {((
            +mapping<Int, String> { it.toString() }
            +flatMapping { it.toList() }
            +mapping { it.code }
            +filtering { it % 7 == 0 }
            +taking(42)
            +flatMapping { it .. 1234 }
            +mapping { it * it * v }
            +taking(7))
            { a, b -> a + b })}

        return result
    }

     //NOT INLINED

    fun sumNI(list: List<Int>): Int {
        val result = list
            .transduceNI(0) {
            { a, b -> a + b } }

        return result
    }

    fun sumOfSquaresNI(list: List<Int>): Long {
        val result = list
            .transduceNI(0L) {((
            +mapping<Int, Int> { it * it })
            { a, b -> a + b })}

        return result
    }

    fun sumOfSquaresEvenNI(list: List<Int>): Int {
        val result = list
            .transduceNI(0) {((
            +filtering<Int> { it % 2 == 0 }
            +mapping { it * it })
            { a, b -> a + b })}

        return result
    }

    fun cartNI(vHi: List<Int>, vLo: List<Int>): Int {
        val result = vHi
            .transduceNI(0){((
            +flatMapping<Int, Int> { d: Int -> vLo.transduceNI { mapping { it * d } } })
            { a, b -> a + b })}

        return result
    }

    fun cartFusedNI(vHi: List<Int>, vLo: List<Int>): Int {
        val result = vHi
            .transduceNI(0){((
            +flatMappingFused { d: Int -> vLo.fuser { mapping { it * d } } })
            { a, b -> a + b })}

        return result
    }

    fun cartTrandusedNI(vHi: List<Int>, vLo: List<Int>): Int {
        val result = vHi
            .transduceNI(0){((
            +flatMapping({ vLo }) { d: Int ->  { mapping { it * d } } })
            { a, b -> a + b })}

        return result
    }

    fun mapsMegamorphicNI(list: List<Int>): Long {
        val result = list
            .transduceNI(0L) {((
            +mapping<Int, Int> { it * 1 }
            +mapping { it * 2 }
            +mapping { it * 3 }
            +mapping { it * 4 }
            +mapping { it * 5 }
            +mapping { it * 6 }
            +mapping { it * 7 })
            { a, b -> a + b })}

        return result
    }

    fun filtersMegamorphicNI(list: List<Int>): Int {
        val result = list
            .transduceNI(0) {((
            +filtering<Int> { it > 1 }
            +filtering { it > 2 }
            +filtering { it > 3 }
            +filtering { it > 4 }
            +filtering { it > 5 }
            +filtering { it > 6 }
            +filtering { it > 7 })
            { a, b -> a + b })}

        return result
    }

    fun flatMapTakeNI(vHi: List<Int>, vLo: List<Int>): Int {
        val result = vHi
            .transduceNI(0) {((
            +flatMapping<Int, Int> { d: Int -> vLo.transduceNI { mapping { it * d } } }
            +taking(20000000))
            { a, b -> a + b } )}

        return result
    }

    fun flatMapTakeFusedNI(vHi: List<Int>, vLo: List<Int>): Int {
        val result = vHi
            .transduceNI(0) {((
            +flatMappingFused { d: Int -> vLo.fuser { mapping { it * d } } }
            +taking(20000000))
            { a, b -> a + b } )}

        return result
    }

    fun flatMapTakeTrandusedNI(vHi: List<Int>, vLo: List<Int>): Int {
        val result = vHi
            .transduceNI(0) {((
            +flatMapping({ vLo }) { d: Int -> { mapping { it * d } } }
            +taking(20000000))
            { a, b -> a + b } )}

        return result
    }

    fun dotProductNI(list: List<Int>): Int {
        val result = list
            .transduceNI(0) {((
            +zipping(list) { a: Int, b -> a * b })
            { a, b -> a + b } )}

        return result
    }

    fun flatMapAfterZipNI(vFaZ: List<Int>): Long {
        val result = vFaZ
            .transduceNI(0L) {((
            +zipping(vFaZ) { a: Int, b -> a + b }
            +flatMapping<Int, Int> { d: Int -> vFaZ.transduceNI { mapping { it + d } } })
            { a, b -> a + b } )}

        return result
    }

    fun flatMapAfterZipFusedNI(vFaZ: List<Int>): Long {
        val result = vFaZ
            .transduceNI(0L) {((
            +zipping(vFaZ) { a: Int, b -> a + b }
            +flatMappingFused { d: Int -> vFaZ.fuser { mapping { it + d } } })
            { a, b -> a + b } )}

        return result
    }

    fun flatMapAfterZipTrandusedNI(vFaZ: List<Int>): Long {
        val result = vFaZ
            .transduceNI(0L) {((
            +zipping(vFaZ) { a: Int, b -> a + b }
            +flatMapping({ vFaZ }) { d: Int -> { mapping { it + d } } })
            { a, b -> a + b } )}

        return result
    }

    fun zipAfterFlatMapNI(vZaF: List<Int>): Long {
        val result = vZaF
            .transduceNI(0L) {((
            +flatMapping<Int, Int> { d -> vZaF.transduceNI { +mapping<Int, Int> { it + d } } }
            +zipping(vZaF) { a: Int, b -> a + b })
            { a, b -> a + b })}

        return result
    }

    fun zipAfterFlatMapFusedNI(vZaF: List<Int>): Long {
        val result = vZaF
            .transduceNI(0L) {((
            +flatMappingFused { d: Int -> vZaF.fuser { +mapping<Int, Int> { it + d } } }
            +zipping(vZaF) { a: Int, b -> a + b })
            { a, b -> a + b })}

        return result
    }

    fun zipAfterFlatMapTrandusedNI(vZaF: List<Int>): Long {
        val result = vZaF
            .transduceNI(0L) {((
            +flatMapping({ vZaF }) { d: Int -> { +mapping<Int, Int> { it + d } } }
            +zipping(vZaF) { a: Int, b -> a + b })
            { a, b -> a + b })}

        return result
    }

    fun zipFlatMapFlatMapNI(v: List<Int>, vLo: List<Int>): Int {
        val result = vLo
            .transduceNI(0) {((
            +flatMapping<Int, Int> { d -> v.transduceNI { +mapping<Int, Int> { d - it } } }
            +zipping(v.lazyTransduce { +flatMapping<Int, Int> { d -> vLo.transduceNI { +mapping<Int, Int> { it * d } } } }) {
                    a: Int, b: Int -> a + b
            }
            +taking(20000000))
            { a, b -> a + b })}

        return result
    }

    fun zipFlatMapFlatMapFusedNI(v: List<Int>, vLo: List<Int>): Int {
        val result = vLo
            .transduceNI(0) {((
            +flatMappingFused { d: Int -> v.fuser { +mapping<Int, Int> { d - it } } }
            +zipping(v.lazyTransduce { +flatMappingFused { d: Int -> vLo.fuser { +mapping<Int, Int> { it * d } } } }) {
                    a: Int, b: Int -> a + b
            }
            +taking(20000000))
            { a, b -> a + b })}

        return result
    }

    fun zipFlatMapFlatMapFusedFusedNI(v: List<Int>, vLo: List<Int>): Int {
        val zipFlatMapFlatMapFused = vLo
            .transduceNI(0) {((
            +flatMappingFused { d: Int -> v.fuser { +mapping<Int, Int> { d - it } } }
            +zipping(v.fuser2 { +flatMappingFused { d: Int -> vLo.fuser { +mapping<Int, Int> { it * d } } } }) {
                    a: Int, b: Int -> a + b
            }
            +taking(20000000))
            { a, b -> a + b })}

        return zipFlatMapFlatMapFused
    }

    fun singleContextNI(list: List<Int>): Int {
        val v = 42
        val result = list
            .transduceNI(0) {((
            +mapping<Int, String> { it.toString() }
            +flatMapping { it.toList() }
            +mapping { it.code }
            +filtering { it % 7 == 0 }
            +taking(42)
            +flatMapping { it .. 1234 }
            +mapping { it * it * v }
            +taking(7))
            { a, b -> a + b })}

        return result
    }

    //SEQUENCES

    fun sumSeq(list: List<Int>): Int {
        val result = list.asSequence().sum()

        return result
    }

    fun sumOfSquaresSeq(list: List<Int>): Long {
        val result = list
            .asSequence()
            .map { (it * it).toLong() }
            .sum()

        return result
    }

    fun sumOfSquaresEvenSeq(list: List<Int>): Int {
        val result = list
            .asSequence()
            .filter { it % 2 == 0 }
            .map { it * it }
            .sum()

        return result
    }

    fun cartSeq(vHi: List<Int>, vLo: List<Int>): Int {
        val result = vHi
            .asSequence()
            .flatMap { d -> vLo.asSequence().map { it * d } }
            .sum()

        return result
    }

    fun cartSeqMixed(vHi: List<Int>, vLo: List<Int>): Int {
        val result = vHi
            .asSequence()
            .flatMap { d -> vLo.map { it * d } }
            .sum()

        return result
    }

    fun mapsMegamorphicSeq(list: List<Int>): Long {
        val result = list
            .asSequence()
            .map { it * 1 }
            .map { it * 2 }
            .map { it * 3 }
            .map { it * 4 }
            .map { it * 5 }
            .map { it * 6 }
            .map { (it * 7).toLong() }
            .sum()

        return result
    }

    fun filtersMegamorphicSeq(list: List<Int>): Int {
        val result = list
            .asSequence()
            .filter { it > 1 }
            .filter { it > 2 }
            .filter { it > 3 }
            .filter { it > 4 }
            .filter { it > 5 }
            .filter { it > 6 }
            .filter { it > 7 }
            .sum()

        return result
    }

    fun flatMapTakeSeq(vHi: List<Int>, vLo: List<Int>): Int {
        val result = vHi
            .asSequence()
            .flatMap { d -> vLo.asSequence().map { it * d } }
            .take(20000000)
            .sum()

        return result
    }

    fun flatMapTakeSeqMixed(vHi: List<Int>, vLo: List<Int>): Int {
        val result = vHi
            .asSequence()
            .flatMap { d -> vLo.map { it * d } }
            .take(20000000)
            .sum()

        return result
    }

    fun dotProductSeq(list: List<Int>): Int {
        val result = list
            .asSequence()
            .zip(list.asSequence()) { a, b -> a * b }
            .sum()

        return result
    }

    fun flatMapAfterZipSeq(vFaZ: List<Int>): Long {
        val result = vFaZ
            .asSequence()
            .zip(vFaZ.asSequence()) { a, b -> a + b }
            .flatMap { d -> vFaZ.asSequence().map { (it + d).toLong() } }
            .sum()

        return result
    }

    fun flatMapAfterZipSeqMixed(vFaZ: List<Int>): Long {
        val result = vFaZ
            .asSequence()
            .zip(vFaZ.asSequence()) { a, b -> a + b }
            .flatMap { d -> vFaZ.map { (it + d).toLong() } }
            .sum()

        return result
    }

    fun zipAfterFlatMapSeq(vZaF: List<Int>): Long {
        val result = vZaF
            .asSequence()
            .flatMap { d -> vZaF.asSequence().map { it + d } }
            .zip(vZaF.asSequence()) { a, b -> (a + b).toLong() }
            .sum()

        return result
    }

    fun zipAfterFlatMapSeqMixed(vZaF: List<Int>): Long {
        val result = vZaF
            .asSequence()
            .flatMap { d -> vZaF.map { it + d } }
            .zip(vZaF.asSequence()) { a, b -> (a + b).toLong() }
            .sum()

        return result
    }

    fun zipFlatMapFlatMapSeq(v: List<Int>, vLo: List<Int>): Int {
        val result = vLo
            .asSequence()
            .flatMap { d -> v.map { d - it } }
            .zip(v.asSequence().flatMap { d -> vLo.map { it * d } }) { a, b -> a + b }
            .take(20000000)
            .sum()

        return result
    }

    fun singleContextSeq(list: List<Int>): Int {
        val v = 42
        val result = list
            .asSequence()
            .map { it.toString() }
            .flatMap { it.toList() }
            .map { it.code }
            .filter { it % 7 == 0 }
            .take(42)
            .flatMap { it .. 1234 }
            .map { it * it * v }
            .take(7)
            .sum()

        return result
    }

    fun singleContextBaseline(list: List<Int>): Int {
        val v = 42
        var cnt1 = 0
        var cnt2 = 0
        var result = 0

        for (e in list) {
            for (ee in e.toString().toList()) {
                if (ee.code % 7 == 0 && cnt1 < 42) {
                    cnt1++
                    for (eee in ee.code .. 1234) {
                        if (cnt2 < 7) {
                            cnt2++
                            result += eee * eee * v
                        }
                    }
                }
            }
        }

        return result
    }

    //@formatter:on
/*
   fun inlined(list: MutableList<String>): List<Int> {
         val res = list
            .transduce4<String, Int> { (
            +flatMapping { s: String -> s.toList() }
            +mapping { el: Char -> el.code }
            +filtering { el: Int -> el > 51 }
            +flatMapping { el: Int -> el..1234 }
            +filtering { el: Int -> el in 52..64 }
            +mapping { it / 2 }
         ) }

        return res
    }

    fun notInlined(list: MutableList<String>): List<Int> {
        val res = list
            .transduce4NI<String, Int> { (
            +flatMapping { s: String -> s.toList() }
            +mapping { el: Char -> el.code }
            +filtering { el: Int -> el > 51 }
            +flatMapping { el: Int -> el..1234 }
            +filtering { el: Int -> el in 52..64 }
            +mapping { it / 2 }
            +taking(7)
        ) }

        return res
    }*/

    fun noInnerLoop(list: MutableList<Int>): List<Int> {
        val res = list
            .transduce<Int, Int> {(
            +mapping { el: Int -> el + 2 }
            +filtering { it % 2 == 0 }
            +taking(42)
        ) }

        return res
    }

    fun innerLoop(list: MutableList<Int>): List<Int> {
        val res = list
            .transduce<Int, Int> {(
            +mapping { el: Int -> el + 2 }
            + flatMapping { it .. 100 }
            +filtering { it % 2 == 0 }
            +taking(42)
        )}

        return res
    }



    //@formatter:on

    /*fun sequenceBased(list: MutableList<String>): List<Int> {
        val res = list
            .asSequence()
            .flatMap { it.toList() }
            .map { it.code }
            .filter { it > 51 }
            .flatMap { it..1234 }
            .filter { it in 52..64 }
            .map { it / 2 }
            .toList()

        return res
    }

    fun standardOperators(list: MutableList<String>): List<Int> {
        val res = list
            .flatMap { it.toList() }
            .map { it.code }
            .filter { it > 51 }
            .flatMap { it..1234 }
            .filter { it in 52..64 }
            .map { it / 2 }

        return res
    }

    fun loopBased(list: MutableList<String>): List<Int> {
        val res = mutableListOf<Int>()
        for (e in list) {
            for (ee in e.toList()) {
                val tmp = ee.code
                if (tmp > 51)
                    for (eee in tmp..1234) {
                        if (eee in 52..64)
                            res.add(eee / 2)
                    }
            }
        }

        return res
    }

    fun bar(list: MutableList<String>): List<Int> {
        val res = list
            .flatMap { it.toList() }
            .map { it.code }
            .filter { it > 51 }
            .flatMap { it..1234 }
            .filter { it in 52..64 }
            .map { it / 2 }

        return res
    }

    fun zip(vZaF: List<Int>): Int {
        val zipAfterFlatMap = vZaF
            .transduce5(0) {((
            +flatMapping<Int, Int> { d -> vZaF.transduce4 { mapping { it + d } } }
            +zipping(vZaF) { a: Int, b: Int -> a + b })
            { a, b -> a + b })}

        return zipAfterFlatMap
    }

    fun zip2(vZaF: List<Int>): Int {
        val zipAfterFlatMap = vZaF
            .transduce5(0) {((
            +zipping(vZaF.lazyTransduce { +flatMapping<Int, Int> { d -> vZaF.lazyTransduce { mapping { it + d } } } }) { a: Int, b: Int -> a + b })
            { a, b -> a + b })}

        return zipAfterFlatMap
    }

    fun trickyFlatMap(vZaF: List<Int>): Int {
        val zipAfterFlatMap = vZaF
            .transduce5(0) {((
            +flatMappingFused<Int, Int> { d -> vZaF.fuser { +mapping<Int, Int> { it + d } } }
            +zipping(vZaF) { a: Int, b: Int -> a + b })
            { a, b -> a + b })}

        return zipAfterFlatMap
    }

    fun zipAnother(vZaF: List<Int>): Int {
        val zipAfterFlatMap = vZaF
            .transduce5(0) {((
            +zipping(vZaF.lazyTransduce { +flatMappingFused<Int, Int> { d -> vZaF.fuser { +mapping<Int, Int> { it + d } } } }) { a: Int, b: Int -> a + b })
            { a, b -> a + b })}

        return zipAfterFlatMap
    }

    fun trickyFlatMapWOZip(vZaF: List<Int>): Int {
        val zipAfterFlatMap = vZaF
            .transduce5(0) {((
            +flatMappingFused<Int, Int> { d -> vZaF.fuser { +mapping<Int, Int> { it + d } } })
            { a, b -> a + b })}

        return zipAfterFlatMap
    }

    fun zipp(list: List<Int>): Int {
        return list
            .flatMap { d -> list.map { it + d } }
            .zip(list) { a, b -> a + b}
            .sum()
    }

    fun megamorphicMap(list: List<Int>): Int {
        val megamorphicMap = list
            .transduce5(0) {((
            +mapping<Int, Int> { it * 1 }
            +mapping { it * 2 }
            +mapping { it * 3 }
            +mapping { it * 4 }
            +mapping { it * 5 }
            +mapping { it * 6 }
            +mapping { it * 7 })
            { a, b -> a + b })}

        return megamorphicMap
    }*/
}

fun main() {
    val tc = TestsContainer()
    val sum = 450000000
    val sumOfSquares = 2850000000
    val sumOfSquaresEven = 1200000000
    val cart = 2025000000
    val mapsMegamorphic = 2268000000000
    val filtersMegamorphic = 170000000
    val flatMapTake = 405000000
    val dotProduct = 285000000
    val flatMapAfterZip = 1499850000000
    val zipAfterFlatMap = 99999990000000
    val zipFlatMapFlatMap = 315000000
    
    with(tc) {
        val v    = (0..100000000).map { it % 10 }.dropLast(1)
        val vHi  = (0..10000000).map { (it % 10) }.dropLast(1)
        val vLo  = (0..10).map { (it % 10) }.dropLast(1)
        val vFaZ = (0..10000).toMutableList().dropLast(1)
        val vZaF = (0..10000000).toMutableList().dropLast(1)

        //Inlined
        assert(sum(v) == sum)
        assert(sumOfSquares(v) == sumOfSquares)
        assert(sumOfSquaresEven(v) == sumOfSquaresEven)
        assert(cart(vHi, vLo) == cart)
        assert(cartFused(vHi, vLo) == cart)
        assert(mapsMegamorphic(v) == mapsMegamorphic)
        assert(filtersMegamorphic(v) == filtersMegamorphic)
        assert(flatMapTake(vHi, vLo) == flatMapTake)
        assert(flatMapTakeFused(vHi, vLo) == flatMapTake)
        assert(dotProduct(vHi) == dotProduct)
        assert(flatMapAfterZip(vFaZ) == flatMapAfterZip)
        assert(flatMapAfterZipFused(vFaZ) == flatMapAfterZip)
        assert(zipAfterFlatMap(vZaF) == zipAfterFlatMap)
        assert(zipAfterFlatMapFused(vZaF) == zipAfterFlatMap)
        assert(zipFlatMapFlatMap(v, vLo) == zipFlatMapFlatMap)
        assert(zipFlatMapFlatMapFused(v, vLo) == zipFlatMapFlatMap)

        assert(zipFlatMapFlatMapFusedFused(v, vLo) == zipFlatMapFlatMap)
        assert(zipFlatMapFlatMapTransduced(v, vLo) == zipFlatMapFlatMap)
        assert(zipFlatMapFlatMapFusedFusedNew(v, vLo) == zipFlatMapFlatMap)

        assert(cartTrandusedNI(vHi, vLo) == cart)
        assert(flatMapAfterZipTrandusedNI(vFaZ) == flatMapAfterZip)
        assert(flatMapTakeTrandusedNI(vHi, vLo) == flatMapTake)
        assert(zipAfterFlatMapTrandusedNI(vZaF) == zipAfterFlatMap)

        assert(singleContext(vZaF) == singleContextSeq(vZaF))
        assert(singleContext(vZaF) == singleContextBaseline(vZaF))


      /*  //Not inlined
        assert(sumNI(v) == sum)
        assert(sumOfSquaresNI(v) == sumOfSquares)
        assert(sumOfSquaresEvenNI(v) == sumOfSquaresEven)
        assert(cartNI(vHi, vLo) == cart)
        assert(cartFusedNI(vHi, vLo) == cart)
        assert(mapsMegamorphicNI(v) == mapsMegamorphic)
        assert(filtersMegamorphicNI(v) == filtersMegamorphic)
        assert(flatMapTakeNI(vHi, vLo) == flatMapTake)
        assert(flatMapTakeFusedNI(vHi, vLo) == flatMapTake)
        assert(dotProductNI(vHi) == dotProduct)
        assert(flatMapAfterZipNI(vFaZ) == flatMapAfterZip)
        assert(flatMapAfterZipFusedNI(vFaZ) == flatMapAfterZip)
        assert(zipAfterFlatMapNI(vZaF) == zipAfterFlatMap)
        assert(zipAfterFlatMapFusedNI(vZaF) == zipAfterFlatMap)
        assert(zipFlatMapFlatMapNI(v, vLo) == zipFlatMapFlatMap)
        assert(zipFlatMapFlatMapFusedNI(v, vLo) == zipFlatMapFlatMap)

        //sequences
        assert(sumSeq(v) == sum)
        assert(sumOfSquaresSeq(v) == sumOfSquares)
        assert(sumOfSquaresEvenSeq(v) == sumOfSquaresEven)
        assert(cartSeq(vHi, vLo) == cart)
        assert(cartSeqMixed(vHi, vLo) == cart)
        assert(mapsMegamorphicSeq(v) == mapsMegamorphic)
        assert(filtersMegamorphicSeq(v) == filtersMegamorphic)
        assert(flatMapTakeSeq(vHi, vLo) == flatMapTake)
        assert(flatMapTakeSeqMixed(vHi, vLo) == flatMapTake)
        assert(dotProductSeq(vHi) == dotProduct)
        assert(flatMapAfterZipSeq(vFaZ) == flatMapAfterZip)
        assert(flatMapAfterZipSeqMixed(vFaZ) == flatMapAfterZip)
        assert(zipAfterFlatMapSeq(vZaF) == zipAfterFlatMap)
        assert(zipAfterFlatMapSeqMixed(vZaF) == zipAfterFlatMap)
        assert(zipFlatMapFlatMapSeq(v, vLo) == zipFlatMapFlatMap)*/
    }
}
