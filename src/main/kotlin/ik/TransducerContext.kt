@file:Suppress("NOTHING_TO_INLINE")

package ik

typealias Reducer<T,R> = (T, R) -> T
typealias Transducer<Acc,T_in,T_out> = (Reducer<Acc,T_in>) -> Reducer<Acc,T_out>

class TransducerContext<Recv> {
    var exit: Boolean = false

    inline fun <T_out,T_in> mapping(crossinline f: (T_out) -> T_in): Transducer<Recv,T_in,T_out> =
        { step: Reducer<Recv,T_in> ->
            { acc: Recv, arg: T_out -> step(acc, f(arg)) }}

    inline fun <T> filtering(crossinline pred: (T) -> Boolean): Transducer<Recv,T,T> =
        { step: Reducer<Recv,T> ->
            { acc: Recv, arg: T ->
                if (pred(arg))
                    step(acc,arg)
                else
                    acc }}

    inline fun <T> taking(n: Int): Transducer<Recv,T,T> {
        var count = 0
        return { step: Reducer<Recv,T> ->
            { acc: Recv, arg: T ->
                count++
                if (count > n) {
                    acc
                }
                else if (count == n) {
                    exit = true
                    step(acc, arg)
                } else {
                    step(acc, arg)
                }}}
    }

    inline fun <T_in,T_out> mapFlatting(crossinline f: (T_out) -> T_in): Transducer<Recv,T_in,Iterable<T_out>> {
        return { step: Reducer<Recv,T_in> ->
            { acc: Recv, arg: Iterable<T_out> ->
                for (e in arg) {
                    if (exit) break
                    mapping(f)(step)(acc,e)
                }
                acc }}
    }

    inline fun <T_out,T_in> flatMapping(crossinline f: (T_out) -> Iterable<T_in>): Transducer<Recv,T_in,T_out> {
        return { step: Reducer<Recv,T_in> ->
            { acc: Recv, arg: T_out ->
                var _acc = acc
                for (e in f(arg)) {
                    _acc = step(_acc, e)
                    if (exit) break
                }
                _acc }}
    }

    inline fun <T_out, T_in> flatMappingFused(crossinline f: (T_out) -> ExtendedTransducerContext<Recv, T_out, T_in>): Transducer<Recv,T_in,T_out> {
        return { step: Reducer<Recv,T_in> ->
            { acc: Recv, arg: T_out ->
                var _acc = acc
                val extendedTransducerContext = f(arg)

                for (e in extendedTransducerContext.list) {
                    _acc = extendedTransducerContext.transducer(step).invoke(_acc, e)
                    if (exit || extendedTransducerContext.ctx.exit) break
                }

                _acc }}
    }

    inline fun <T_out, V> zipping(list: Iterable<V>): Transducer<Recv, Pair<T_out, V>, T_out> {
        val iter = list.iterator()

        return { step: Reducer<Recv, Pair<T_out, V>> ->
            { acc: Recv, arg: T_out ->
                if (iter.hasNext())
                    step(acc, arg to iter.next())
                else
                    acc.also { exit = true } }}
    }

    inline fun <T_out, V, R> zipping(list: Iterable<V>, crossinline transform: (T_out, V) -> R): Transducer<Recv, R, T_out> {
        val iter = list.iterator()

        return { step: Reducer<Recv, R> ->
            { acc: Recv, arg: T_out ->
                if (iter.hasNext())
                    step(acc, transform(arg, iter.next()))
                else
                    acc.also { exit = true } }}
    }

    inline infix operator fun <T_in,T_out,T_in2> Transducer<Recv,T_in,T_out>.plus(crossinline t: Transducer<Recv,T_in2,T_in>): Transducer<Recv,T_in2,T_out> =
        { arg: Reducer<Recv,T_in2> -> this.invoke(t.invoke(arg)) }
    inline infix operator fun <T_in,T_out,T_in2> Transducer<Recv,T_in,T_out>.minus(crossinline t: Transducer<Recv,T_in2,T_in>): Transducer<Recv,T_in2,T_out> =
        { arg: Reducer<Recv,T_in2> -> this.invoke(t.invoke(arg)) }

    inline infix operator fun <T_in,T_out> Transducer<Recv,T_in,T_out>.plus(noinline r: Reducer<Recv,T_in>) = this.invoke(r)

    inline operator fun <T_in,T_out> Transducer<Recv,T_in,T_out>.unaryPlus() = this
}

class ExtendedTransducerContext<Recv, In, Out>(
    var list: Iterable<In>,
    var ctx: TransducerContext<Recv>,
    var transducer: Transducer<Recv, Out, In>
)

inline fun <Recv, In, Out> List<In>.fuser(
    operatorChain: TransducerContext<Recv>.() -> Transducer<Recv, Out, In>
): ExtendedTransducerContext<Recv, In, Out> {
    val ctx = TransducerContext<Recv>()

    return ExtendedTransducerContext(this, ctx, ctx.operatorChain())
}

inline fun <Out, In> _transduce(
    initial: In, dataset: List<Out>, ctx: TransducerContext<In>, reducer: Reducer<In, Out>
): In {
    var res = initial
    for (e in dataset) {
        if (ctx.exit) break
        res = reducer(res, e)
    }

    return res
}

@SuperInline inline fun <Out,In> List<Out>.transduce4(
    operatorChain: TransducerContext<MutableList<In>>.() -> Transducer<MutableList<In>,In,Out>
): List<In> {
    val ctx = TransducerContext<MutableList<In>>()
    val res = _transduce(mutableListOf(), this, ctx, ctx.operatorChain().invoke { a, b -> conj(a, b)})

    return res
}

inline fun <Out,In> List<Out>.transduce4NI(
    operatorChain: TransducerContext<MutableList<In>>.() -> Transducer<MutableList<In>,In,Out>
): List<In> {
    val ctx = TransducerContext<MutableList<In>>()
    val res = _transduce(mutableListOf(), this, ctx, ctx.operatorChain().invoke { a, b -> conj(a, b)})

    return res
}

@SuperInline inline fun <Out, In> List<Out>.transduce5(
    initial: In,
    operatorChain: TransducerContext<In>.() -> Reducer<In, Out>
): In {
    val ctx = TransducerContext<In>()
    val res = _transduce(initial, this, ctx, ctx.operatorChain())

    return res
}

inline fun <Out, In> List<Out>.transduce5NI(
    initial: In,
    operatorChain: TransducerContext<In>.() -> Reducer<In, Out>
): In {
    val ctx = TransducerContext<In>()
    val res = _transduce(initial, this, ctx, ctx.operatorChain())

    return res
}

inline fun <Recv> toList(acc: MutableList<Recv>, v: Recv) = acc.apply { this.add(v) }

inline fun <Out, In> List<Out>.lazyTransduce(
    operatorChain: TransducerContext<MutableList<In>>.() -> Transducer<MutableList<In>, In, Out>
): Iterable<In> {
    val ctx = TransducerContext<MutableList<In>>()
    val reducer = ctx.operatorChain().invoke { a, b -> conj(a, b)}

    return LazyIterable( this, ctx, reducer)
}

class LazyIterable<In, Out>(
    val backingData: Iterable<In>,
    val ctx: TransducerContext<MutableList<Out>>,
    val reducer: Reducer<MutableList<Out>, In>
) : Iterable<Out> {
    override fun iterator(): Iterator<Out> {
        return LazyIterator()
    }

    inner class LazyIterator : Iterator<Out> {
        private val backingDataIterator = backingData.iterator()
        private val acc = mutableListOf<Out>()
        private var _nextElement: Out? = null //nextElement
        private var accPointer: Int = 0
        private inline val nextElement
            get() = run {
                if (accPointer < acc.size) {
                    return@run acc[accPointer].also { accPointer++ }
                }

                accPointer = acc.size

                while (backingDataIterator.hasNext() && !ctx.exit && accPointer == acc.size) {
                    reducer(acc, backingDataIterator.next())
                }

                return@run if (accPointer != acc.size)
                    acc[accPointer].also { accPointer++ }
                else
                    null

            }

        override fun hasNext(): Boolean {
            if (_nextElement == null)
                _nextElement = nextElement

            return !ctx.exit && _nextElement != null
        }

        override fun next(): Out {
            val next: Out  = (
                    if (_nextElement == null)
                        nextElement
                    else
                        _nextElement
                    )
                ?: throw NoSuchElementException()

            return next.also { _nextElement = null }
        }
    }

}


inline fun <T> conj(acc: MutableList<T>, el: T): MutableList<T> =
    acc.apply { this.add(el) }
